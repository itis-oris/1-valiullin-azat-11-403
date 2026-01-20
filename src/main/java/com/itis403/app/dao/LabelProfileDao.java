package com.itis403.app.dao;

import com.itis403.app.config.DataSourceProvider;
import com.itis403.app.model.LabelProfile;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LabelProfileDao {

    private final DataSource dataSource;

    public LabelProfileDao() {
        this.dataSource = DataSourceProvider.getDataSource();
    }

    public Optional<LabelProfile> findByUserId(Long userId) {
        String sql = "SELECT * FROM label_profiles WHERE user_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapLabelProfile(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding label profile by user id", e);
        }
        return Optional.empty();
    }

    public void save(LabelProfile profile) {
        System.out.println("=== LABEL PROFILE DAO SAVE DETAILED DEBUG ===");
        System.out.println("User ID: " + profile.getUserId());
        System.out.println("Label Name: " + profile.getLabelName());
        System.out.println("Description: " + profile.getDescription());
        System.out.println("Contact Email: " + profile.getContactEmail());
        System.out.println("Website: " + profile.getWebsite());

        String sql = "INSERT INTO label_profiles (user_id, label_name, description, contact_email, website) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Логируем параметры
            stmt.setLong(1, profile.getUserId());
            stmt.setString(2, profile.getLabelName());
            stmt.setString(3, profile.getDescription());
            stmt.setString(4, profile.getContactEmail());
            stmt.setString(5, profile.getWebsite());

            System.out.println("Parameters set: " +
                    profile.getUserId() + ", " +
                    profile.getLabelName() + ", " +
                    profile.getDescription() + ", " +
                    profile.getContactEmail() + ", " +
                    profile.getWebsite());

            int rows = stmt.executeUpdate();
            System.out.println("Rows affected: " + rows);

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    long id = keys.getLong(1);
                    profile.setId(id);
                    System.out.println("Generated ID: " + id);
                } else {
                    System.out.println("No generated keys returned");
                }
            }

            System.out.println("✅ Label profile saved successfully in DAO");
        } catch (SQLException e) {
            System.out.println("❌ SQL Error in DAO: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            e.printStackTrace();
            throw new RuntimeException("Error saving label profile", e);
        }
    }

    private LabelProfile mapLabelProfile(ResultSet rs) throws SQLException {
        LabelProfile profile = new LabelProfile();
        profile.setId(rs.getLong("id"));
        profile.setUserId(rs.getLong("user_id"));
        profile.setLabelName(rs.getString("label_name"));
        profile.setDescription(rs.getString("description"));
        profile.setContactEmail(rs.getString("contact_email"));
        profile.setWebsite(rs.getString("website"));
        return profile;
    }
    public Optional<Long> findProfileIdByUserId(Long userId) {
        String sql = "SELECT id FROM label_profiles WHERE user_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(rs.getLong("id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding label profile id by user id", e);
        }
        return Optional.empty();
    }
    public List<LabelProfile> findAll() {
        String sql = "SELECT * FROM label_profiles";
        List<LabelProfile> labels = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                labels.add(mapLabelProfile(rs));
            }
            System.out.println("Found " + labels.size() + " labels in DB");
        } catch (SQLException e) {
            System.out.println("Error finding all labels: " + e.getMessage());
            throw new RuntimeException("Error finding all labels", e);
        }
        return labels;
    }
}