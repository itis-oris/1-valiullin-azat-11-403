package com.itis403.app.controller;

import com.itis403.app.config.FreemarkerConfig;
import com.itis403.app.model.User;
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

@WebServlet("/user/*")
public class UserServlet extends HttpServlet {

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

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/auth/login");
            return;
        }

        String path = req.getPathInfo();
        Map<String, Object> data = new HashMap<>();
        data.put("contextPath", req.getContextPath());
        data.put("user", user);

        if (path == null || path.equals("/profile")) {
            // Redirect to role-specific profile
            if (user.getRole() == User.UserRole.ARTIST) {
                resp.sendRedirect(req.getContextPath() + "/artist/profile");
            } else {
                resp.sendRedirect(req.getContextPath() + "/label/profile");
            }
        } else if (path.equals("/settings")) {
            renderTemplate("user/settings.ftlh", data, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("user");

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/auth/login");
            return;
        }

        String path = req.getPathInfo();

        if (path.equals("/update-profile")) {
            // Обновление основных данных пользователя
            String email = req.getParameter("email");
            String username = req.getParameter("username");

            userService.updateUserProfile(user.getId(), username, email);
            resp.sendRedirect(req.getContextPath() + "/user/profile?success=1");
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