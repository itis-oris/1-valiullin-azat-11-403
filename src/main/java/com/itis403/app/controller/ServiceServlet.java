package com.itis403.app.controller;

import com.itis403.app.config.FreemarkerConfig;
import com.itis403.app.model.Service;
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

@WebServlet("/service/*")
public class ServiceServlet extends HttpServlet {

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

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/auth/login");
            return;
        }

        String path = req.getPathInfo();
        Map<String, Object> data = new HashMap<>();
        data.put("contextPath", req.getContextPath());
        data.put("user", user);

        if (user.getRole() == User.UserRole.LABEL) {
            Long labelId = serviceService.getLabelProfileId(user.getId());

            if (path == null || path.equals("/manage")) {
                data.put("services", serviceService.getServicesByLabel(labelId));
                renderTemplate("label/services.ftlh", data, resp);
            } else if (path.equals("/create")) {
                // ДОБАВЬТЕ ЭТУ СТРОКУ: создаем пустой объект service для новой формы
                data.put("service", new Service()); // или data.put("service", null);
                data.put("isEdit", false); // флаг для определения режима (создание/редактирование)
                renderTemplate("label/service-form.ftlh", data, resp);
            } else if (path.startsWith("/edit/")) {
                try {
                    Long serviceId = Long.parseLong(path.substring(6));
                    var service = serviceService.getServiceById(serviceId);
                    data.put("service", service);
                    data.put("isEdit", true); // флаг для режима редактирования
                    renderTemplate("label/service-form.ftlh", data, resp);
                } catch (Exception e) {
                    resp.sendRedirect(req.getContextPath() + "/service/manage");
                }
            }
        } else if (user.getRole() == User.UserRole.ARTIST) {
            if (path == null || path.equals("/browse")) {
                data.put("services", serviceService.getAllServices());
                renderTemplate("artist/browse-services.ftlh", data, resp);
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
        Long labelId = serviceService.getLabelProfileId(user.getId());

        if (path.equals("/create")) {
            String name = req.getParameter("name");
            String description = req.getParameter("description");
            String priceStr = req.getParameter("basePrice");

            double price = 0.0;
            if (priceStr != null && !priceStr.isEmpty()) {
                price = Double.parseDouble(priceStr);
            }

            serviceService.createService(labelId, name, description, price);
            resp.sendRedirect(req.getContextPath() + "/service/manage?success=1");
        } else if (path.startsWith("/edit/")) {
            try {
                Long serviceId = Long.parseLong(path.substring(6));
                String name = req.getParameter("name");
                String description = req.getParameter("description");
                String priceStr = req.getParameter("basePrice");

                double price = 0.0;
                if (priceStr != null && !priceStr.isEmpty()) {
                    price = Double.parseDouble(priceStr);
                }

                serviceService.updateService(serviceId, name, description, price);
                resp.sendRedirect(req.getContextPath() + "/service/manage?success=1");
            } catch (Exception e) {
                resp.sendRedirect(req.getContextPath() + "/service/manage?error=1");
            }
        } else if (path.startsWith("/delete/")) {
            try {
                Long serviceId = Long.parseLong(path.substring(8));
                serviceService.deleteService(serviceId);
                resp.sendRedirect(req.getContextPath() + "/service/manage?success=1");
            } catch (Exception e) {
                resp.sendRedirect(req.getContextPath() + "/service/manage?error=1");
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