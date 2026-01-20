package com.itis403.app.dao;

import com.itis403.app.config.DataSourceProvider;
import com.itis403.app.model.ArtistProfile;
import javax.sql.DataSource;
import java.sql.*;
import java.util.Optional;

public class ArtistProfileDao {

    private final DataSource dataSource;

    public ArtistProfileDao() {
        this.dataSource = DataSourceProvider.getDataSource();
    }

    public Optional<ArtistProfile> findByUserId(Long userId) {
        String sql = "SELECT * FROM artist_profiles WHERE user_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapArtistProfile(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding artist profile by user id", e);
        }
        return Optional.empty();
    }

    public void save(ArtistProfile profile) {
        String sql = "INSERT INTO artist_profiles (user_id, artist_name, description, genre) VALUES (?, ?, ?, ?) " +
                "ON CONFLICT (user_id) DO UPDATE SET artist_name = ?, description = ?, genre = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setLong(1, profile.getUserId());
            stmt.setString(2, profile.getArtistName());
            stmt.setString(3, profile.getDescription());
            stmt.setString(4, profile.getGenre());
            stmt.setString(5, profile.getArtistName());
            stmt.setString(6, profile.getDescription());
            stmt.setString(7, profile.getGenre());

            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    profile.setId(keys.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving artist profile", e);
        }
    }

    private ArtistProfile mapArtistProfile(ResultSet rs) throws SQLException {
        ArtistProfile profile = new ArtistProfile();
        profile.setId(rs.getLong("id"));
        profile.setUserId(rs.getLong("user_id"));
        profile.setArtistName(rs.getString("artist_name"));
        profile.setDescription(rs.getString("description"));
        profile.setGenre(rs.getString("genre"));
        return profile;
    }
    public Optional<Long> findProfileIdByUserId(Long userId) {
        System.out.println("=== ArtistProfileDao.findProfileIdByUserId() ===");
        System.out.println("🔍 Searching artist profile ID for user: " + userId);

        String sql = "SELECT id FROM artist_profiles WHERE user_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Long id = rs.getLong("id");
                System.out.println("✅ Found artist profile ID: " + id);
                return Optional.of(id);
            } else {
                System.out.println("❌ No artist profile found for user: " + userId);
                return Optional.empty();
            }
        } catch (SQLException e) {
            System.err.println("💥 SQL Error in findProfileIdByUserId: " + e.getMessage());
            throw new RuntimeException("Error finding artist profile id by user id", e);
        }
    }
}