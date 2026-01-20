package com.itis403.app.service;

import com.itis403.app.dao.ServiceDao;
import com.itis403.app.dao.LabelProfileDao;
import com.itis403.app.model.LabelProfile;
import com.itis403.app.model.Service;

import java.util.ArrayList;
import java.util.List;

public class ServiceService {

    private final ServiceDao serviceDao;
    private final LabelProfileDao labelProfileDao;

    public ServiceService(ServiceDao serviceDao, LabelProfileDao labelProfileDao) {
        this.serviceDao = serviceDao;
        this.labelProfileDao = labelProfileDao;
    }

    public Long getLabelProfileId(Long userId) {
        return labelProfileDao.findProfileIdByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Label profile not found for user: " + userId));
    }

    public List<Service> getServicesByLabel(Long labelId) {
        return serviceDao.findByLabelId(labelId);
    }

    public List<Service> getServicesByLabel(Long labelId, int limit) {
        List<Service> services = serviceDao.findByLabelId(labelId);
        return services.stream().limit(limit).toList();
    }

    public List<Service> getAllServices() {
        return serviceDao.findAll();
    }

    public List<LabelProfile> getAllLabels() {
        System.out.println("=== GET ALL LABELS ===");
        try {
            List<LabelProfile> labels = labelProfileDao.findAll();
            System.out.println("Found " + labels.size() + " labels");
            return labels;
        } catch (Exception e) {
            System.out.println("Error getting labels: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void createService(Long labelId, String name, String description, double price) {
        Service service = new Service(labelId, name, description, java.math.BigDecimal.valueOf(price));
        serviceDao.save(service);
    }

    public void updateService(Long serviceId, String name, String description, double price) {
        Service service = serviceDao.findById(serviceId).orElseThrow(() -> new RuntimeException("Service not found"));
        service.setName(name);
        service.setDescription(description);
        service.setBasePrice(java.math.BigDecimal.valueOf(price));
        serviceDao.update(service);
    }

    public void deleteService(Long serviceId) {
        serviceDao.delete(serviceId);
    }

    public int getServicesCountByLabel(Long labelId) {
        return serviceDao.findByLabelId(labelId).size();
    }

    public Service getServiceById(Long serviceId) {
        return serviceDao.findById(serviceId).orElse(null);
    }

    public Object getLabelStats(Long labelId) {

        return new Object() {
            public int totalServices = getServicesCountByLabel(labelId);
            public int activeSubmissions = 10; // Demo value
            public double revenue = 1500.0; // Demo value
        };
    }
}