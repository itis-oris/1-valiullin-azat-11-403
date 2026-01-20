package com.itis403.app.filter;

import com.itis403.app.model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class AuthenticationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());


        if (isPublicPath(path)) {
            chain.doFilter(request, response);
            return;
        }


        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");


            if (hasAccess(user, path)) {
                chain.doFilter(request, response);
            } else {
                httpResponse.sendError(403, "Access denied");
            }
        } else {

            httpResponse.sendRedirect(httpRequest.getContextPath() + "/auth/login");
        }
    }

    private boolean isPublicPath(String path) {
        return path.equals("/auth/login") ||
                path.equals("/auth/register") ||
                path.equals("/auth/logout") ||
                path.equals("/logout") ||
                path.equals("/register") ||
                path.startsWith("/css/") ||
                path.startsWith("/js/") ||
                path.startsWith("/uploads/") ||
                path.startsWith("/assets/") ||
                path.equals("/") ||
                path.equals("/favicon.ico");
    }

    private boolean hasAccess(User user, String path) {

        if (path.equals("/profile")) {
            return true;
        }

        if (path.equals("/profile/artist") && user.getRole() == User.UserRole.ARTIST) {
            return true;
        }

        if (path.equals("/profile/label") && user.getRole() == User.UserRole.LABEL) {
            return true;
        }
        // ARTIST paths
        if (path.startsWith("/artist/") && user.getRole() == User.UserRole.ARTIST) {
            return true;
        }

        // LABEL paths
        if (path.startsWith("/label/") && user.getRole() == User.UserRole.LABEL) {
            return true;
        }


        if (path.startsWith("/service/")) {

            if (user.getRole() == User.UserRole.LABEL) {
                return true;
            }
            // ARTIST имеет доступ только к browse
            if (user.getRole() == User.UserRole.ARTIST) {
                return path.equals("/service/browse") || path.equals("/service/");
            }
            return false;
        }

        if (path.startsWith("/submission/") && user.getRole() == User.UserRole.ARTIST) {
            return true;
        }
        if ((path.equals("/submission/pending") ||
                path.equals("/submissions/pending") ||
                path.equals("/submission/review")) &&
                user.getRole() == User.UserRole.LABEL) {
            return true;
        }



        return path.startsWith("/user/") || path.equals("/dashboard");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }
}