package com.itis403.app.controller;

import com.itis403.app.config.FreemarkerConfig;
import com.itis403.app.model.User;
import com.itis403.app.service.SubmissionService;
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

@WebServlet("/artist/dashboard")
public class ArtistDashboardServlet extends HttpServlet {

    private SubmissionService submissionService;
    private Configuration fm;

    @Override
    public void init() {
        ServletContext ctx = getServletContext();
        submissionService = (SubmissionService) ctx.getAttribute("submissionService");
        fm = FreemarkerConfig.getConfiguration();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("user");

        if (user == null || user.getRole() != User.UserRole.ARTIST) {
            resp.sendRedirect(req.getContextPath() + "/auth/login");
            return;
        }

        Long artistId = submissionService.getArtistProfileId(user.getId());

        Map<String, Object> data = new HashMap<>();
        data.put("contextPath", req.getContextPath());
        data.put("user", user);

        if (artistId != null) {
            data.put("submissionsCount", submissionService.getSubmissionsCountByArtist(artistId));
            data.put("pendingSubmissions", submissionService.getPendingSubmissionsCountByArtist(artistId));
            data.put("approvedSubmissions", submissionService.getApprovedSubmissionsCountByArtist(artistId));
            data.put("recentSubmissions", submissionService.getRecentSubmissionsByArtist(artistId, 5));
        }

        renderTemplate("artist/dashboard.ftlh", data, resp);
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