package com.itis403.app.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

@WebServlet("/debug/templates")
public class DebugServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/plain");
        PrintWriter out = resp.getWriter();

        // Проверим доступ к resources
        out.println("=== Checking templates in classpath ===");

        InputStream is = getClass().getClassLoader().getResourceAsStream("templates/auth/login.ftlh");
        if (is != null) {
            out.println("✓ Found: templates/auth/login.ftlh");
            is.close();
        } else {
            out.println("✗ Not found: templates/auth/login.ftlh");
        }

        // Проверим другие возможные пути
        String[] paths = {
                "templates/auth/login.ftlh",
                "/templates/auth/login.ftlh",
                "auth/login.ftlh"
        };

        for (String path : paths) {
            is = getClass().getClassLoader().getResourceAsStream(path);
            out.println(path + ": " + (is != null ? "FOUND" : "NOT FOUND"));
            if (is != null) is.close();
        }
    }
}