package com.itis403.app.util;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class PasswordHasher {

    public static String hashPassword(String password) {
        System.out.println("=== PASSWORD HASHING DEBUG ===");
        System.out.println("Original password: '" + password + "'");
        System.out.println("Password length: " + (password != null ? password.length() : "null"));

        if (password == null || password.trim().isEmpty()) {
            System.out.println("❌ Cannot hash null or empty password");
            return null;
        }

        try {
            String hashedPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray());
            System.out.println("Hashed password: '" + hashedPassword + "'");
            System.out.println("Hash length: " + hashedPassword.length());
            System.out.println("Hash prefix: " + hashedPassword.substring(0, 10) + "...");
            System.out.println("✅ PASSWORD HASHING SUCCESS");
            return hashedPassword;
        } catch (Exception e) {
            System.out.println("❌ ERROR during password hashing: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static boolean verifyPassword(String plainPassword, String storedHash) {
        System.out.println("=== PASSWORD VERIFICATION DEBUG ===");
        System.out.println("Plain password: '" + plainPassword + "'");
        System.out.println("Plain password length: " + (plainPassword != null ? plainPassword.length() : "null"));
        System.out.println("Stored hash: '" + storedHash + "'");
        System.out.println("Stored hash length: " + (storedHash != null ? storedHash.length() : "null"));

        if (storedHash == null) {
            System.out.println("❌ Stored hash is NULL!");
            return false;
        }

        if (plainPassword == null) {
            System.out.println("❌ Plain password is NULL!");
            return false;
        }

        if (storedHash.trim().isEmpty()) {
            System.out.println("❌ Stored hash is EMPTY!");
            return false;
        }

        try {
            BCrypt.Result result = BCrypt.verifyer().verify(plainPassword.toCharArray(), storedHash);
            boolean verified = result.verified;

            System.out.println("BCrypt verification result: " + verified);
            if (!verified) {
                System.out.println("❌ BCrypt verification FAILED");
                System.out.println("Possible reasons:");
                System.out.println("  - Wrong password");
                System.out.println("  - Different salt");
                System.out.println("  - Corrupted hash format");
            } else {
                System.out.println("✅ BCrypt verification SUCCESS");
            }

            return verified;
        } catch (Exception e) {
            System.out.println("❌ EXCEPTION during password verification: " + e.getMessage());
            System.out.println("Exception type: " + e.getClass().getName());
            e.printStackTrace();
            return false;
        }
    }
}