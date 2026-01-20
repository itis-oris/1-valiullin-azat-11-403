package com.itis403.app.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DataSourceProvider {
    private static final HikariDataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();

        // Основные настройки PostgreSQL
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/music_label");
        config.setUsername("postgres");
        config.setPassword("postgres");

        // Настройки пула HikariCP
        config.setDriverClassName("org.postgresql.Driver");
        config.setMaximumPoolSize(AppConfig.DB_MAX_POOL_SIZE);
        config.setMinimumIdle(AppConfig.DB_MIN_IDLE);
        config.setIdleTimeout(AppConfig.DB_IDLE_TIMEOUT);
        config.setConnectionTimeout(AppConfig.DB_CONNECTION_TIMEOUT);
        config.setMaxLifetime(AppConfig.DB_MAX_LIFETIME);

        // Дополнительные настройки
        config.setLeakDetectionThreshold(60000);
        config.setConnectionTestQuery("SELECT 1");
        config.setValidationTimeout(5000);

        dataSource = new HikariDataSource(config);
        initializeDatabase();
    }

    private static void initializeDatabase() {
        String[] createTables = {
                """
            CREATE TABLE IF NOT EXISTS users (
                id BIGSERIAL PRIMARY KEY,
                username VARCHAR(50) UNIQUE NOT NULL,
                email VARCHAR(100) UNIQUE NOT NULL,
                password_hash VARCHAR(255) NOT NULL,
                role VARCHAR(10) NOT NULL CHECK (role IN ('ARTIST', 'LABEL')),
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """,
                """
            CREATE TABLE IF NOT EXISTS artist_profiles (
                id BIGSERIAL PRIMARY KEY,
                user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                artist_name VARCHAR(100) NOT NULL,
                description TEXT,
                genre VARCHAR(50),
                UNIQUE(user_id)
            )
            """,
                """
            CREATE TABLE IF NOT EXISTS label_profiles (
                id BIGSERIAL PRIMARY KEY,
                user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                label_name VARCHAR(100) NOT NULL,
                description TEXT,
                contact_email VARCHAR(255),
                website VARCHAR(255),
                UNIQUE(user_id)
            )
            """,
                """
            CREATE TABLE IF NOT EXISTS services (
                id BIGSERIAL PRIMARY KEY,
                label_id BIGINT NOT NULL REFERENCES label_profiles(id) ON DELETE CASCADE,
                name VARCHAR(100) NOT NULL,
                description TEXT,
                base_price DECIMAL(10,2) DEFAULT 0.00
            )
            """,
                """
            CREATE TABLE IF NOT EXISTS submissions (
                id BIGSERIAL PRIMARY KEY,
                artist_id BIGINT NOT NULL REFERENCES artist_profiles(id) ON DELETE CASCADE,
                service_id BIGINT NOT NULL REFERENCES services(id) ON DELETE CASCADE,
                track_title VARCHAR(200) NOT NULL,
                track_file_url VARCHAR(500),
                status VARCHAR(10) DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'APPROVED', 'REJECTED')),
                submission_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                label_comment TEXT,
                administered_date TIMESTAMP
            )
            """,
                """
            CREATE TABLE IF NOT EXISTS songs (
                id BIGSERIAL PRIMARY KEY,
                artist_id BIGINT NOT NULL REFERENCES artist_profiles(id) ON DELETE CASCADE,
                title VARCHAR(200) NOT NULL,
                genre VARCHAR(50),
                file_url VARCHAR(500) NOT NULL,
                duration INTEGER,
                file_size BIGINT,
                uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                is_approved BOOLEAN DEFAULT FALSE,
                approved_at TIMESTAMP
            )
            """
        };

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            for (String sql : createTables) {
                try {
                    stmt.execute(sql);
                } catch (SQLException e) {
                    System.out.println("Table might already exist: " + e.getMessage());
                }
            }

            System.out.println("Database tables verified using HikariCP");

        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }

    public static DataSource getDataSource() {
        return dataSource;
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static void close() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            System.out.println("HikariCP connection pool closed");
        }
    }
}