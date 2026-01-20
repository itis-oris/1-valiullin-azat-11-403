package com.itis403.app.dao;

import com.itis403.app.config.DataSourceProvider;
import com.itis403.app.model.Service;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ServiceDao {

    private final DataSource dataSource;

    public ServiceDao() {
        this.dataSource = DataSourceProvider.getDataSource();
    }

    public List<Service> findByLabelId(Long labelId) {
        String sql = "SELECT * FROM services WHERE label_id = ? ORDER BY name";
        List<Service> services = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, labelId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                services.add(mapService(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding services by label id", e);
        }
        return services;
    }

    public List<Service> findAll() {
        String sql = "SELECT s.*, lp.label_name FROM services s " +
                "JOIN label_profiles lp ON s.label_id = lp.id " +
                "ORDER BY lp.label_name, s.name";
        List<Service> services = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Service service = mapService(rs);
                service.setLabelName(rs.getString("label_name"));
                services.add(service);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all services", e);
        }
        return services;
    }

    public Optional<Service> findById(Long id) {
        String sql = "SELECT * FROM services WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapService(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding service by id", e);
        }
        return Optional.empty();
    }

    public void save(Service service) {
        String sql = "INSERT INTO services (label_id, name, description, base_price) VALUES (?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setLong(1, service.getLabelId());
            stmt.setString(2, service.getName());
            stmt.setString(3, service.getDescription());
            stmt.setBigDecimal(4, service.getBasePrice());

            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    service.setId(keys.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving service", e);
        }
    }

    public void update(Service service) {
        String sql = "UPDATE services SET name = ?, description = ?, base_price = ? WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, service.getName());
            stmt.setString(2, service.getDescription());
            stmt.setBigDecimal(3, service.getBasePrice());
            stmt.setLong(4, service.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating service", e);
        }
    }

    public void delete(Long id) {
        String sql = "DELETE FROM services WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting service", e);
        }
    }

    private Service mapService(ResultSet rs) throws SQLException {
        Service service = new Service();
        service.setId(rs.getLong("id"));
        service.setLabelId(rs.getLong("label_id"));
        service.setName(rs.getString("name"));
        service.setDescription(rs.getString("description"));
        service.setBasePrice(rs.getBigDecimal("base_price"));
        return service;
    }
}