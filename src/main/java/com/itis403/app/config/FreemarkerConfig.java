package com.itis403.app.config;

import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;

import jakarta.servlet.ServletContext;
import java.io.IOException;

public class FreemarkerConfig {
    private static Configuration configuration;

    public static void init(ServletContext context) throws IOException {
        configuration = new Configuration(Configuration.VERSION_2_3_32);

        // Для шаблонов в resources (classpath)
        configuration.setClassLoaderForTemplateLoading(
                Thread.currentThread().getContextClassLoader(),
                "templates"
        );

        // Настройки FreeMarker
        configuration.setDefaultEncoding("UTF-8");
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
        configuration.setLogTemplateExceptions(false);
        configuration.setWrapUncheckedExceptions(true);
        configuration.setFallbackOnNullLoopVariable(false);

        // Дополнительные настройки для безопасности
        configuration.setBooleanFormat("c");
        configuration.setNumberFormat("computer");

        System.out.println("FreeMarker configured for .ftlh templates from resources/templates/");
    }

    public static Configuration getConfiguration() {
        if (configuration == null) {
            throw new IllegalStateException("FreeMarker not initialized. Call init() first.");
        }
        return configuration;
    }
}