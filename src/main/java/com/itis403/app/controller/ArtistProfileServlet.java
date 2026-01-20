package com.itis403.app.controller;

import com.itis403.app.config.FreemarkerConfig;
import com.itis403.app.model.User;
import com.itis403.app.model.ArtistProfile;
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

@WebServlet({"/artist/profile", "/profile/artist"})
public class ArtistProfileServlet extends HttpServlet {

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

        if (user == null || user.getRole() != User.UserRole.ARTIST) {
            resp.sendRedirect(req.getContextPath() + "/auth/login");
            return;
        }

        ArtistProfile profile = userService.getArtistProfile(user.getId());

        Map<String, Object> data = new HashMap<>();
        data.put("contextPath", req.getContextPath());
        data.put("user", user);
        data.put("profile", profile);

        renderTemplate("artist/profile.ftlh", data, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("user");

        if (user == null || user.getRole() != User.UserRole.ARTIST) {
            resp.sendRedirect(req.getContextPath() + "/auth/login");
            return;
        }

        String artistName = req.getParameter("artistName");
        String genre = req.getParameter("genre");
        String description = req.getParameter("description");

        ArtistProfile profile = new ArtistProfile();
        profile.setUserId(user.getId());
        profile.setArtistName(artistName);
        profile.setGenre(genre);
        profile.setDescription(description);

        boolean success = userService.saveArtistProfile(profile);

        if (success) {
            resp.sendRedirect(req.getContextPath() + "/artist/profile?success=1");
        } else {
            resp.sendRedirect(req.getContextPath() + "/artist/profile?error=1");
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