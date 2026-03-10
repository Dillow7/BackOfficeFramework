package com.hotel.backoffice;

import mg.framework.registry.ControllerRegistry;
import mg.framework.scan.ClasspathScanner;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "InitServlet", urlPatterns = {}, loadOnStartup = 1)
public class InitServlet extends HttpServlet {
    @Override
    public void init() throws ServletException {
        System.out.println("🚀 InitServlet.init() START");
        try {
            ControllerRegistry registry = new ControllerRegistry();
            System.out.println("📦 Scanning package: com.hotel.backoffice");
            
            ClasspathScanner.scanAndRegister("com.hotel.backoffice", registry);
            System.out.println("✅ Scanning completed");
            
            getServletContext().setAttribute("mg.framework.registry", registry);
            System.out.println("✅ Registry set in ServletContext");
            System.out.println("✅ Routes registered (" + registry.getExactRoutesSnapshot().size() + " routes):");
            
            registry.getExactRoutesSnapshot().forEach((path, methods) -> {
                System.out.println("  Route: " + path);
                methods.forEach((method, handlers) -> {
                    System.out.println("    " + method + ": " + handlers);
                });
            });
            System.out.println("✅ InitServlet.init() COMPLETE");
        } catch (Throwable e) {
            System.out.println("❌ ERROR in InitServlet.init():");
            e.printStackTrace(System.out);
            throw new ServletException("Failed to initialize controllers", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendError(404);
    }
}
