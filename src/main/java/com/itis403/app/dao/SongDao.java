package com.itis403.app.dao;

import com.itis403.app.config.DataSourceProvider;
import com.itis403.app.model.Song;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SongDao {

    private final DataSource dataSource;

    public SongDao() {
        this.dataSource = DataSourceProvider.getDataSource();
    }

    public List<Song> findByArtistId(Long artistId) {
        String sql = "SELECT s.*, ap.artist_name FROM songs s " +
                "JOIN artist_profiles ap ON s.artist_id = ap.id " +
                "WHERE s.artist_id = ? ORDER BY s.uploaded_at DESC";
        List<Song> songs = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, artistId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                songs.add(mapSong(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding songs by artist id", e);
        }
        return songs;
    }

    public List<Song> findPendingSongs() {
        String sql = "SELECT s.*, ap.artist_name FROM songs s " +
                "JOIN artist_profiles ap ON s.artist_id = ap.id " +
                "WHERE s.is_approved = false ORDER BY s.uploaded_at DESC";
        List<Song> songs = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                songs.add(mapSong(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding pending songs", e);
        }
        return songs;
    }

    public Optional<Song> findById(Long id) {
        String sql = "SELECT s.*, ap.artist_name FROM songs s " +
                "JOIN artist_profiles ap ON s.artist_id = ap.id " +
                "WHERE s.id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapSong(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding song by id", e);
        }
        return Optional.empty();
    }

    public void save(Song song) {
        String sql = "INSERT INTO songs (artist_id, title, genre, file_url, duration, file_size) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setLong(1, song.getArtistId());
            stmt.setString(2, song.getTitle());
            stmt.setString(3, song.getGenre());
            stmt.setString(4, song.getFileUrl());
            stmt.setInt(5, song.getDuration());
            stmt.setLong(6, song.getFileSize());

            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    song.setId(keys.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving song", e);
        }
    }

    public void approveSong(Long songId) {
        String sql = "UPDATE songs SET is_approved = true, approved_at = CURRENT_TIMESTAMP WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, songId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error approving song", e);
        }
    }

    public void delete(Long id) {
        String sql = "DELETE FROM songs WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting song", e);
        }
    }

    private Song mapSong(ResultSet rs) throws SQLException {
        Song song = new Song();
        song.setId(rs.getLong("id"));
        song.setArtistId(rs.getLong("artist_id"));
        song.setTitle(rs.getString("title"));
        song.setGenre(rs.getString("genre"));
        song.setFileUrl(rs.getString("file_url"));
        song.setDuration(rs.getInt("duration"));
        song.setFileSize(rs.getLong("file_size"));
        song.setUploadedAt(rs.getTimestamp("uploaded_at").toLocalDateTime());
        song.setIsApproved(rs.getBoolean("is_approved"));

        if (rs.getTimestamp("approved_at") != null) {
            song.setApprovedAt(rs.getTimestamp("approved_at").toLocalDateTime());
        }

        // Joined field
        song.setArtistName(rs.getString("artist_name"));

        return song;
    }
}