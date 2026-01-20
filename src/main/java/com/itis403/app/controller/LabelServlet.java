package com.itis403.app.controller;

import com.itis403.app.config.FreemarkerConfig;
import com.itis403.app.model.User;
import com.itis403.app.service.ServiceService;
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

@WebServlet("/label/*")
public class LabelServlet extends HttpServlet {

    private ServiceService serviceService;
    private Configuration fm;

    @Override
    public void init() {
        ServletContext ctx = getServletContext();
        serviceService = (ServiceService) ctx.getAttribute("serviceService");
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

        String path = req.getPathInfo();
        Map<String, Object> data = new HashMap<>();
        data.put("contextPath", req.getContextPath());
        data.put("user", user);

        if (path == null || path.equals("/home")) {
            resp.sendRedirect(req.getContextPath() + "/label/dashboard");
        } else if (path.equals("/stats")) {
            Long labelId = serviceService.getLabelProfileId(user.getId());
            data.put("stats", serviceService.getLabelStats(labelId));
            renderTemplate("label/stats.ftlh", data, resp);
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