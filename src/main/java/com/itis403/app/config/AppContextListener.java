package com.itis403.app.config;

import com.itis403.app.dao.*;
import com.itis403.app.service.*;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("=== Initializing Music Label Application ===");

        ServletContext context = sce.getServletContext();

        try {
            // 1. Инициализация FreeMarker
            FreemarkerConfig.init(context);

            // 2. DataSource уже инициализирован статическим блоком
            // 3. Инициализация DAO
            UserDao userDao = new UserDao();
            ArtistProfileDao artistProfileDao = new ArtistProfileDao();
            LabelProfileDao labelProfileDao = new LabelProfileDao();
            ServiceDao serviceDao = new ServiceDao();
            SubmissionDao submissionDao = new SubmissionDao();
            SongDao songDao = new SongDao();

            // 4. Инициализация сервисов
            UserService userService = new UserService(userDao, artistProfileDao, labelProfileDao);
            SubmissionService submissionService = new SubmissionService(submissionDao, serviceDao, artistProfileDao);
            ServiceService serviceService = new ServiceService(serviceDao, labelProfileDao);
            AuthService authService = new AuthService(userDao);
            SongService songService = new SongService(songDao);

            // 5. Сохраняем сервисы в контекст
            context.setAttribute("userService", userService);
            context.setAttribute("submissionService", submissionService);
            context.setAttribute("serviceService", serviceService);
            context.setAttribute("authService", authService);
            context.setAttribute("songService", songService);

            System.out.println("=== Music Label Application Initialized Successfully ===");

        } catch (Exception e) {
            System.err.println("Application initialization failed: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Application initialization failed", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("=== Shutting down Music Label Application ===");
        DataSourceProvider.close();
    }
}