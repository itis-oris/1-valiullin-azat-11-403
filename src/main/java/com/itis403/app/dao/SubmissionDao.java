package com.itis403.app.dao;

import com.itis403.app.config.DataSourceProvider;
import com.itis403.app.model.Submission;
import com.itis403.app.model.SubmissionStatus;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SubmissionDao {

    private final DataSource dataSource;

    public SubmissionDao() {
        this.dataSource = DataSourceProvider.getDataSource();
    }

    public List<Submission> findByArtistId(Long artistId) {
        String sql = "SELECT s.*, sv.name as service_name, lp.label_name, ap.artist_name " +
                "FROM submissions s " +
                "JOIN services sv ON s.service_id = sv.id " +
                "JOIN label_profiles lp ON sv.label_id = lp.id " +
                "JOIN artist_profiles ap ON s.artist_id = ap.id " +
                "WHERE s.artist_id = ? ORDER BY s.submission_date DESC";
        List<Submission> submissions = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, artistId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                submissions.add(mapSubmission(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding submissions by artist id", e);
        }
        return submissions;
    }

    public List<Submission> findPendingByLabelId(Long labelId) {
        String sql = "SELECT s.*, sv.name as service_name, lp.label_name, ap.artist_name " +
                "FROM submissions s " +
                "JOIN services sv ON s.service_id = sv.id " +
                "JOIN label_profiles lp ON sv.label_id = lp.id " +
                "JOIN artist_profiles ap ON s.artist_id = ap.id " +
                "WHERE sv.label_id = ? AND s.status = 'PENDING' " +
                "ORDER BY s.submission_date DESC";
        List<Submission> submissions = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, labelId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                submissions.add(mapSubmission(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding pending submissions by label id", e);
        }
        return submissions;
    }

    public Optional<Submission> findById(Long id) {
        String sql = "SELECT s.*, sv.name as service_name, lp.label_name, ap.artist_name " +
                "FROM submissions s " +
                "JOIN services sv ON s.service_id = sv.id " +
                "JOIN label_profiles lp ON sv.label_id = lp.id " +
                "JOIN artist_profiles ap ON s.artist_id = ap.id " +
                "WHERE s.id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapSubmission(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding submission by id", e);
        }
        return Optional.empty();
    }

    public void save(Submission submission) {
        String sql = "INSERT INTO submissions (artist_id, service_id, track_title, track_file_url, status) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setLong(1, submission.getArtistId());
            stmt.setLong(2, submission.getServiceId());
            stmt.setString(3, submission.getTrackTitle());
            stmt.setString(4, submission.getTrackFileUrl());
            stmt.setString(5, submission.getStatus().name());

            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    submission.setId(keys.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving submission", e);
        }
    }

    public void updateStatus(Long id, SubmissionStatus status, String comment) {
        String sql = "UPDATE submissions SET status = ?, label_comment = ?, administered_date = CURRENT_TIMESTAMP " +
                "WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status.name());
            stmt.setString(2, comment);
            stmt.setLong(3, id);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating submission status", e);
        }
    }

    public int countByArtistId(Long artistId) {
        String sql = "SELECT COUNT(*) FROM submissions WHERE artist_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, artistId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error counting submissions by artist id", e);
        }
        return 0;
    }

    public int countPendingByArtistId(Long artistId) {
        String sql = "SELECT COUNT(*) FROM submissions WHERE artist_id = ? AND status = 'PENDING'";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, artistId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error counting pending submissions by artist id", e);
        }
        return 0;
    }

    public int countApprovedByArtistId(Long artistId) {
        String sql = "SELECT COUNT(*) FROM submissions WHERE artist_id = ? AND status = 'APPROVED'";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, artistId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error counting approved submissions by artist id", e);
        }
        return 0;
    }

    private Submission mapSubmission(ResultSet rs) throws SQLException {
        Submission submission = new Submission();
        submission.setId(rs.getLong("id"));
        submission.setArtistId(rs.getLong("artist_id"));
        submission.setServiceId(rs.getLong("service_id"));
        submission.setTrackTitle(rs.getString("track_title"));
        submission.setTrackFileUrl(rs.getString("track_file_url"));
        submission.setStatus(SubmissionStatus.valueOf(rs.getString("status")));
        submission.setSubmissionDate(rs.getTimestamp("submission_date").toLocalDateTime());
        submission.setLabelComment(rs.getString("label_comment"));

        if (rs.getTimestamp("administered_date") != null) {
            submission.setAdministeredDate(rs.getTimestamp("administered_date").toLocalDateTime());
        }

        // Joined fields
        submission.setArtistName(rs.getString("artist_name"));
        submission.setServiceName(rs.getString("service_name"));
        submission.setLabelName(rs.getString("label_name"));

        return submission;
    }
}