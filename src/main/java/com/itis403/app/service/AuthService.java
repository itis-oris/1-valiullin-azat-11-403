package com.itis403.app.service;

import com.itis403.app.dao.UserDao;
import com.itis403.app.model.User;
import com.itis403.app.util.PasswordHasher;
import java.util.Optional;

public class AuthService {

    private final UserDao userDao;

    public AuthService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User authenticate(String email, String password) {
        System.out.println("=== AUTH SERVICE DEBUG ===");
        System.out.println("Email: " + email);
        System.out.println("Password: " + (password != null ? "[HIDDEN]" : "null"));
        System.out.println("Password length: " + (password != null ? password.length() : "null"));

        try {
            Optional<User> userOpt = userDao.findByEmail(email);
            System.out.println("User found in DB: " + userOpt.isPresent());

            if (userOpt.isPresent()) {
                User user = userOpt.get();
                System.out.println("User details:");
                System.out.println("  - ID: " + user.getId());
                System.out.println("  - Email: " + user.getEmail());
                System.out.println("  - Username: " + user.getUsername());
                System.out.println("  - Role: " + user.getRole());
                System.out.println("  - Stored hash: " + user.getPasswordHash());
                System.out.println("  - Stored hash length: " + (user.getPasswordHash() != null ? user.getPasswordHash().length() : "null"));
                System.out.println("  - Hash prefix: " + (user.getPasswordHash() != null && user.getPasswordHash().length() > 10 ?
                        user.getPasswordHash().substring(0, 10) + "..." : "null"));

                boolean passwordMatches = PasswordHasher.verifyPassword(password, user.getPasswordHash());
                System.out.println("Password verification result: " + passwordMatches);

                if (passwordMatches) {
                    System.out.println("✅ AUTHENTICATION SUCCESS for user: " + user.getEmail());
                    return user;
                } else {
                    System.out.println("❌ PASSWORD MISMATCH for user: " + user.getEmail());
                    System.out.println("Possible reasons:");
                    System.out.println("  - Wrong password entered");
                    System.out.println("  - Hash algorithm mismatch");
                    System.out.println("  - Corrupted hash in database");
                }
            } else {
                System.out.println("❌ USER NOT FOUND with email: " + email);
            }
        } catch (Exception e) {
            System.out.println("❌ EXCEPTION during authentication:");
            e.printStackTrace();
        }

        System.out.println("❌ AUTHENTICATION FAILED for email: " + email);
        return null;
    }
}