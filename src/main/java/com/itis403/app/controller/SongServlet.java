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

@WebServlet("/song/*")
public class SongServlet extends HttpServlet {

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

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/auth/login");
            return;
        }

        String path = req.getPathInfo();
        Map<String, Object> data = new HashMap<>();
        data.put("contextPath", req.getContextPath());
        data.put("user", user);

        if (path != null && path.startsWith("/review/")) {
            if (user.getRole() == User.UserRole.LABEL) {
                try {
                    Long submissionId = Long.parseLong(path.substring(8));
                    var submission = submissionService.getSubmissionById(submissionId);
                    data.put("submission", submission);
                    renderTemplate("label/review-song.ftlh", data, resp);
                } catch (Exception e) {
                    resp.sendRedirect(req.getContextPath() + "/submission/pending");
                }
            } else {
                resp.sendError(403, "Access denied");
            }
        }
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

        String path = req.getPathInfo();

        if (path != null && path.startsWith("/review/")) {
            try {
                Long submissionId = Long.parseLong(path.substring(8));
                String decision = req.getParameter("decision");
                String comment = req.getParameter("comment");

                if ("approve".equals(decision)) {
                    submissionService.approveSubmission(submissionId, comment);
                } else {
                    submissionService.rejectSubmission(submissionId, comment);
                }

                resp.sendRedirect(req.getContextPath() + "/submission/pending?success=1");
            } catch (Exception e) {
                resp.sendRedirect(req.getContextPath() + "/submission/pending?error=1");
            }
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