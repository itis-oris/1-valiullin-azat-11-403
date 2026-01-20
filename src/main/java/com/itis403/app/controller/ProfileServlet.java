package com.itis403.app.controller;

import com.itis403.app.config.FreemarkerConfig;
import com.itis403.app.model.User;
import com.itis403.app.service.UserService;
import com.itis403.app.dao.UserDao;
import com.itis403.app.dao.ArtistProfileDao;
import com.itis403.app.dao.LabelProfileDao;
import freemarker.template.Configuration;
import freemarker.template.Template;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {

    private Configuration fm;
    private UserService userService;

    @Override
    public void init() {
        fm = FreemarkerConfig.getConfiguration();

        // Создаем DAO объекты
        UserDao userDao = new UserDao();
        ArtistProfileDao artistProfileDao = new ArtistProfileDao();
        LabelProfileDao labelProfileDao = new LabelProfileDao();

        // Создаем UserService с зависимостями
        userService = new UserService(userDao, artistProfileDao, labelProfileDao);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("user");

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/auth/login");
            return;
        }

        Map<String, Object> data = new HashMap<>();
        data.put("contextPath", req.getContextPath());
        data.put("user", user);
        data.put("title", "User Profile");
        data.put("message", req.getParameter("message"));
        data.put("error", req.getParameter("error"));

        // Добавляем профиль в зависимости от роли
        if (user.getRole() == User.UserRole.ARTIST) {
            data.put("artistProfile", userService.getArtistProfile(user.getId()));
        } else if (user.getRole() == User.UserRole.LABEL) {
            data.put("labelProfile", userService.getLabelProfile(user.getId()));
        }

        renderTemplate("profile.ftlh", data, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        HttpSession session = req.getSession(false);
        User currentUser = (User) session.getAttribute("user");

        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/auth/login");
            return;
        }

        String email = req.getParameter("email");
        String username = req.getParameter("username");

        // Базовая валидация
        if (email == null || email.trim().isEmpty() || username == null || username.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/profile?error=Email and username are required");
            return;
        }

        email = email.trim();
        username = username.trim();

        try {
            // Обновляем данные пользователя через сервис
            userService.updateUserProfile(currentUser.getId(), username, email);

            // Обновляем объект пользователя в сессии
            currentUser.setUsername(username);
            currentUser.setEmail(email);
            session.setAttribute("user", currentUser);

            resp.sendRedirect(req.getContextPath() + "/profile?message=Profile updated successfully");

        } catch (RuntimeException e) {
            // Обрабатываем ошибки из сервиса
            resp.sendRedirect(req.getContextPath() + "/profile?error=" + e.getMessage());
        } catch (Exception e) {
            resp.sendRedirect(req.getContextPath() + "/profile?error=Unexpected error occurred");
        }
    }

    private void renderTemplate(String templateName, Map<String, Object> data, HttpServletResponse resp)
            throws IOException, ServletException {

        resp.setContentType("text/html;charset=UTF-8");
        try {
            Template tpl = fm.getTemplate(templateName);
            tpl.process(data, resp.getWriter());
        } catch (Exception e) {
            throw new ServletException("Template processing error: " + templateName, e);
        }
    }
}