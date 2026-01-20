package com.itis403.app.controller;

import com.itis403.app.config.FreemarkerConfig;
import com.itis403.app.model.User;
import com.itis403.app.model.LabelProfile;
import com.itis403.app.service.UserService;
import freemarker.template.Configuration;
import freemarker.template.Template;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet({"/label/profile", "/profile/label"})
public class LabelProfileServlet extends HttpServlet {

    private UserService userService;
    private Configuration fm;

    @Override
    public void init() {
        ServletContext ctx = getServletContext();
        userService = (UserService) ctx.getAttribute("userService");
        fm = FreemarkerConfig.getConfiguration();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("user");

        if (user == null || user.getRole() != User.UserRole.LABEL) {
            resp.sendRedirect(req.getContextPath() + "/auth/login");
            return;
        }

        LabelProfile profile = userService.getLabelProfile(user.getId());

        Map<String, Object> data = new HashMap<>();
        data.put("contextPath", req.getContextPath());
        data.put("user", user);
        data.put("profile", profile);

        // Добавляем сообщения об успехе/ошибке
        String success = req.getParameter("success");
        String error = req.getParameter("error");

        if (success != null) {
            data.put("successMessage", "Profile saved successfully!");
        }
        if (error != null) {
            data.put("errorMessage", "Error saving profile. Please try again.");
        }

        renderTemplate("label/profile.ftlh", data, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("user");

        if (user == null || user.getRole() != User.UserRole.LABEL) {
            resp.sendRedirect(req.getContextPath() + "/auth/login");
            return;
        }

        // Получаем параметры формы
        String labelName = req.getParameter("labelName");
        String description = req.getParameter("description");
        String contactEmail = req.getParameter("contactEmail");
        String website = req.getParameter("website");

        System.out.println("=== LABEL PROFILE SAVE DEBUG ===");
        System.out.println("User ID: " + user.getId());
        System.out.println("Label Name: " + labelName);
        System.out.println("Description: " + description);
        System.out.println("Contact Email: " + contactEmail);
        System.out.println("Website: " + website);

        // Валидация
        if (labelName == null || labelName.trim().isEmpty()) {
            System.out.println("❌ Validation failed: labelName is empty");
            resp.sendRedirect(req.getContextPath() + "/label/profile?error=1");
            return;
        }

        try {
            // Получаем существующий профиль или создаем новый
            LabelProfile profile = userService.getLabelProfile(user.getId());
            System.out.println("Existing profile: " + profile);

            if (profile == null) {
                // Создаем новый профиль
                profile = new LabelProfile();
                profile.setUserId(user.getId());
                System.out.println("Creating NEW label profile");
            } else {
                System.out.println("Updating EXISTING label profile");
            }

            // Обновляем данные
            profile.setLabelName(labelName.trim());
            profile.setDescription(description != null ? description.trim() : null);
            profile.setContactEmail(contactEmail != null ? contactEmail.trim() : null);
            profile.setWebsite(website != null ? website.trim() : null);

            System.out.println("Profile to save: " + profile);

            // Сохраняем профиль
            boolean success = userService.saveLabelProfile(profile);
            System.out.println("Save result: " + success);

            if (success) {
                System.out.println("✅ Label profile saved successfully for user: " + user.getId());
                resp.sendRedirect(req.getContextPath() + "/label/profile?success=1");
            } else {
                System.out.println("❌ Failed to save label profile for user: " + user.getId());
                resp.sendRedirect(req.getContextPath() + "/label/profile?error=1");
            }
        } catch (Exception e) {
            System.out.println("❌ Error saving label profile: " + e.getMessage());
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/label/profile?error=1");
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