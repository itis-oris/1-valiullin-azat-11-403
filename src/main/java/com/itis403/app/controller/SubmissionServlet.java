package com.itis403.app.controller;

import com.itis403.app.config.FreemarkerConfig;
import com.itis403.app.model.User;
import com.itis403.app.model.Submission;
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
import java.util.List;

@WebServlet({"/submissions/*", "/submission/*"})
public class SubmissionServlet extends HttpServlet {

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

        System.out.println("=== SubmissionServlet.doGet() STARTED ===");
        System.out.println("URL: " + req.getRequestURL());
        System.out.println("PathInfo: " + req.getPathInfo());

        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("user");

        if (user == null) {
            System.out.println("❌ User is NULL - redirecting to login");
            resp.sendRedirect(req.getContextPath() + "/auth/login");
            return;
        }

        System.out.println("✅ User: " + user.getUsername() + ", Role: " + user.getRole());

        String path = req.getPathInfo();
        Map<String, Object> data = new HashMap<>();
        data.put("contextPath", req.getContextPath());
        data.put("user", user);

        try {
            if (path == null || path.equals("/create")) {
                System.out.println("✅ Processing /create path");

                if (user.getRole() == User.UserRole.ARTIST) {
                    System.out.println("✅ User is ARTIST - loading form");

                    System.out.println("🔍 Checking serviceService: " + (serviceService != null ? "NOT NULL" : "NULL"));

                    // Логи перед каждым вызовом
                    System.out.println("📝 Calling serviceService.getAllLabels()...");
                    data.put("labels", serviceService.getAllLabels());
                    System.out.println("✅ getAllLabels() completed");

                    System.out.println("📝 Calling serviceService.getAllServices()...");
                    data.put("allServices", serviceService.getAllServices());
                    System.out.println("✅ getAllServices() completed");

                    System.out.println("🎨 Rendering template...");
                    renderTemplate("artist/submission-form.ftlh", data, resp);
                    System.out.println("✅ Template rendered successfully");

                } else {
                    System.out.println("❌ User is not ARTIST - access denied");
                    resp.sendError(403, "Access denied");
                }
            } else if (path.equals("/my")) {
                System.out.println("📋 Processing /my path");
                if (user.getRole() == User.UserRole.ARTIST) {
                    System.out.println("👤 Getting artist profile ID for user: " + user.getId());
                    try {
                        Long artistId = submissionService.getArtistProfileId(user.getId());
                        System.out.println("🎵 Artist ID: " + artistId);

                        System.out.println("📝 Getting submissions for artist...");
                        List<Submission> submissions = submissionService.getSubmissionsByArtist(artistId);
                        System.out.println("✅ Found " + submissions.size() + " submissions");

                        // Логируем каждую submission
                        for (Submission sub : submissions) {
                            System.out.println("  - Submission: " + sub.getTrackTitle() +
                                    ", Status: " + sub.getStatus() +
                                    ", Date: " + sub.getSubmissionDate());
                        }

                        data.put("submissions", submissions);
                        System.out.println("🎨 Rendering template artist/my-submissions.ftlh...");
                        renderTemplate("artist/my-submissions.ftlh", data, resp);
                        System.out.println("✅ Template rendered successfully");

                    } catch (Exception e) {
                        System.err.println("💥 ERROR in /my path: " + e.getMessage());
                        e.printStackTrace();
                        throw e;
                    }
                } else {
                    resp.sendError(403, "Access denied");
                }

            } else if (path.equals("/pending")) {
                System.out.println("⏳ Processing /pending path");
                if (user.getRole() == User.UserRole.LABEL) {
                    Long labelId = serviceService.getLabelProfileId(user.getId());
                    data.put("submissions", submissionService.getPendingSubmissionsByLabel(labelId));
                    renderTemplate("label/pending-submissions.ftlh", data, resp);
                } else {
                    resp.sendError(403, "Access denied");
                }
            } else {
                System.out.println("❓ Unknown path: " + path);
                resp.sendError(404, "Page not found");
            }
        } catch (Exception e) {
            System.err.println("💥 ERROR in doGet(): " + e.getMessage());
            System.err.println("ERROR CLASS: " + e.getClass().getName());
            e.printStackTrace();
            throw new ServletException(e);
        }

        System.out.println("=== SubmissionServlet.doGet() COMPLETED ===");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        System.out.println("=== SubmissionServlet.doPost() STARTED ===");
        System.out.println("URL: " + req.getRequestURL());
        System.out.println("PathInfo: " + req.getPathInfo());
        System.out.println("Method: " + req.getMethod());


        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("user");

        if (user == null) {
            System.out.println("❌ User is NULL - redirecting to login");
            resp.sendRedirect(req.getContextPath() + "/auth/login");
            return;
        }

        System.out.println("✅ User: " + user.getUsername() + ", Role: " + user.getRole());

        String path = req.getPathInfo();
        System.out.println("📝 Processing path: " + path);

        if (path.equals("/create") && user.getRole() == User.UserRole.ARTIST) {
            try {
                System.out.println("📝 Getting form parameters...");
                String trackTitle = req.getParameter("trackTitle");
                String serviceIdParam = req.getParameter("serviceId");

                System.out.println("📦 Form data - Track: " + trackTitle + ", Service ID: " + serviceIdParam);

                if (trackTitle == null || trackTitle.trim().isEmpty()) {
                    throw new IllegalArgumentException("Track title is required");
                }

                if (serviceIdParam == null || serviceIdParam.trim().isEmpty()) {
                    throw new IllegalArgumentException("Service is required");
                }

                Long serviceId = Long.parseLong(serviceIdParam);

                System.out.println("👤 Getting artist profile ID...");
                Long artistId = submissionService.getArtistProfileId(user.getId());
                System.out.println("🎵 Artist ID: " + artistId);

                System.out.println("💾 Creating submission...");
                submissionService.createSubmission(artistId, serviceId, trackTitle.trim(), "demo-track.mp3");
                System.out.println("✅ Submission created successfully");

                System.out.println("🔄 Redirecting to /submission/my...");
                resp.sendRedirect(req.getContextPath() + "/submission/my?success=1");

            } catch (Exception e) {
                System.err.println("💥 ERROR in doPost(): " + e.getMessage());
                System.err.println("ERROR CLASS: " + e.getClass().getName());
                e.printStackTrace();

                Map<String, Object> data = new HashMap<>();
                data.put("contextPath", req.getContextPath());
                data.put("user", user);
                data.put("labels", serviceService.getAllLabels());
                data.put("allServices", serviceService.getAllServices());
                data.put("error", "Error creating submission: " + e.getMessage());
                data.put("trackTitle", req.getParameter("trackTitle"));

                renderTemplate("artist/submission-form.ftlh", data, resp);
            }
        } else if (path.equals("/review")) {
            System.out.println("🔍 Processing /review path");

            if (user.getRole() == User.UserRole.LABEL) {
                System.out.println("✅ User is LABEL - processing review");
                try {
                    System.out.println("📦 Parameters:");
                    java.util.Map<String, String[]> params = req.getParameterMap();
                    for (String key : params.keySet()) {
                        System.out.println("  " + key + ": " + java.util.Arrays.toString(params.get(key)));
                    }

                    Long submissionId = Long.parseLong(req.getParameter("submissionId"));
                    String decision = req.getParameter("decision");
                    String comment = req.getParameter("comment");

                    System.out.println("🎯 Review data - Submission: " + submissionId + ", Decision: " + decision);

                    if ("APPROVE".equals(decision)) {
                        submissionService.approveSubmission(submissionId, comment);
                        System.out.println("✅ Submission approved");
                    } else if ("REJECT".equals(decision)) {
                        submissionService.rejectSubmission(submissionId, comment);
                        System.out.println("❌ Submission rejected");
                    }

                    resp.sendRedirect(req.getContextPath() + "/submission/pending?success=1");
                } catch (Exception e) {
                    System.err.println("💥 Error in review: " + e.getMessage());
                    e.printStackTrace();
                    resp.sendRedirect(req.getContextPath() + "/submission/pending?error=1");
                }
            } else {
                System.out.println("❌ User is not LABEL - access denied. User role: " + user.getRole());
                resp.sendError(403, "Access denied");
            }
        } else {
            System.out.println("❌ Invalid path or role. Path: " + path + ", Role: " + user.getRole());
            resp.sendError(403, "Access denied");
        }

        System.out.println("=== SubmissionServlet.doPost() COMPLETED ===");
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