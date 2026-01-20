package com.itis403.app.util;

import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class FileStorage {

    public static String saveFile(Part filePart, String uploadPath) throws IOException {
        String fileName = generateFileName(filePart);
        Path filePath = Paths.get(uploadPath, fileName);

        Files.createDirectories(filePath.getParent());
        filePart.write(filePath.toString());

        return fileName;
    }

    public static boolean deleteFile(String filePath) {
        try {
            return Files.deleteIfExists(Paths.get(filePath));
        } catch (IOException e) {
            return false;
        }
    }

    private static String generateFileName(Part filePart) {
        String originalName = getFileName(filePart);
        String extension = "";

        if (originalName != null && originalName.contains(".")) {
            extension = originalName.substring(originalName.lastIndexOf("."));
        }

        return UUID.randomUUID().toString() + extension;
    }

    private static String getFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        String[] tokens = contentDisposition.split(";");

        for (String token : tokens) {
            if (token.trim().startsWith("filename")) {
                return token.substring(token.indexOf("=") + 2, token.length() - 1);
            }
        }
        return null;
    }

    public static boolean isValidAudioFile(Part filePart) {
        String contentType = filePart.getContentType();
        return contentType != null &&
                (contentType.equals("audio/mpeg") ||
                        contentType.equals("audio/wav") ||
                        contentType.equals("audio/mp3"));
    }

    public static boolean isValidFileSize(Part filePart, long maxSize) {
        return filePart.getSize() <= maxSize;
    }
}