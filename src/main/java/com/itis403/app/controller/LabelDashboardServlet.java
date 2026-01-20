package com.itis403.app.controller;

import com.itis403.app.config.FreemarkerConfig;
import com.itis403.app.model.User;
import com.itis403.app.service.SubmissionService;
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

@WebServlet("/label/dashboard")
public class LabelDashboardServlet extends HttpServlet {

    private SubmissionService submissionService;
    private ServiceService serviceService;
    private Configuration fm;

    @Override
    public void init() {
        ServletContext ctx = getServletContext();
        submissionService = (SubmissionService) ctx.getAttribute("submissionService");
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

        Long labelId = null;
        try {
            labelId = serviceService.getLabelProfileId(user.getId());
        } catch (RuntimeException e) {
            // Label профиль не найден - перенаправляем на создание профиля
            System.out.println("Label profile not found for user " + user.getId() + ", redirecting to profile creation");
            resp.sendRedirect(req.getContextPath() + "/label/profile");
            return;
        }

        Map<String, Object> data = new HashMap<>();
        data.put("contextPath", req.getContextPath());
        data.put("user", user);

        if (labelId != null) {
            data.put("pendingSubmissionsCount", submissionService.getPendingSubmissionsCountByLabel(labelId));
            data.put("totalSubmissionsCount", submissionService.getTotalSubmissionsCountByLabel(labelId));
            data.put("servicesCount", serviceService.getServicesCountByLabel(labelId));
            data.put("approvalRate", Math.round(submissionService.getApprovalRateByLabel(labelId)));
            data.put("recentSubmissions", submissionService.getRecentSubmissionsByLabel(labelId, 5));
            data.put("labelServices", serviceService.getServicesByLabel(labelId, 3));
        }

        renderTemplate("label/dashboard.ftlh", data, resp);
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