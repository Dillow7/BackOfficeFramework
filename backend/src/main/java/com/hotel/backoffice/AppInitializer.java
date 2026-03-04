package com.hotel.backoffice;

import mg.framework.init.FrameworkInitializer;
import mg.framework.registry.ControllerRegistry;
import mg.framework.scan.ClasspathScanner;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppInitializer implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ControllerRegistry registry = new ControllerRegistry();
        
        // Scanner le package des contrôleurs
        ClasspathScanner.scanAndRegister("com.hotel.backoffice", registry);
        
        // Mettre le registry dans le contexte serveur
        sce.getServletContext().setAttribute("mg.framework.registry", registry);
        
        // Log les routes
        registry.getExactRoutesSnapshot().forEach((path, methods) -> {
            System.out.println("Route: " + path);
            methods.forEach((method, handlers) -> {
                System.out.println("  " + method + ": " + handlers);
            });
        });
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {}
}
