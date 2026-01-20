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

@WebServlet("/auth/register")
public class RegisterServlet extends HttpServlet {

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

        Map<String, Object> data = new HashMap<>();
        data.put("contextPath", req.getContextPath());

        renderTemplate("/auth/register.ftlh", data, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        String username = req.getParameter("username");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String role = req.getParameter("role");

        try {
            User user = userService.registerUser(username, email, password, User.UserRole.valueOf(role));

            if (user != null) {
                HttpSession session = req.getSession(true);
                session.setAttribute("user", user);

                switch (user.getRole()) {
                    case ARTIST -> resp.sendRedirect(req.getContextPath() + "/artist/dashboard");
                    case LABEL -> resp.sendRedirect(req.getContextPath() + "/label/dashboard");
                }
            } else {
                resp.sendRedirect(req.getContextPath() + "/auth/register?error=1");
            }

        } catch (Exception e) {
            resp.sendRedirect(req.getContextPath() + "/auth/register?error=1");
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