package com.itis403.app.config;

public class AppConfig {
    // Конфигурационные константы приложения

    // Настройки HikariCP
    public static final int DB_MAX_POOL_SIZE = 10;
    public static final int DB_MIN_IDLE = 2;
    public static final int DB_CONNECTION_TIMEOUT = 30000;
    public static final int DB_IDLE_TIMEOUT = 30000;
    public static final int DB_MAX_LIFETIME = 1800000;


    // Пути, не требующие аутентификации
    public static final String[] PUBLIC_PATHS = {
            "/login",
            "/register",
            "/css/",
            "/js/",
            "/assets/",
            "/error"
    };

    // Пути только для ARTIST
    public static final String[] ARTIST_PATHS = {
            "/artist/",
            "/submission/create",
            "/submission/my"
    };

    // Пути только для LABEL
    public static final String[] LABEL_PATHS = {
            "/label/",
            "/service/",
            "/submissions/pending"
    };

    // Регулярные выражения для валидации
    public static final String USERNAME_PATTERN = "^[a-zA-Z0-9_]{3,50}$";
    public static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)$";
    public static final String PASSWORD_PATTERN = "^.{6,100}$";
}