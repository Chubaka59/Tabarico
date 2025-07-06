package com.gtarp.tabarico.config; // Ou ton package de filtres

import jakarta.servlet.*;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(1) // Assure que ce filtre s'exécute tôt dans la chaîne de filtres
public class MdcLogEnhancerFilter implements Filter {

    public static final String USER_KEY = "user"; // Clé que tu utilises dans logback-spring.xml (%X{user})

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String username = "anonymous"; // Valeur par défaut si aucun utilisateur n'est connecté

        // Tente de récupérer l'utilisateur connecté via Spring Security
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String && "anonymousUser".equals(authentication.getPrincipal()))) {
            username = authentication.getName(); // Le nom d'utilisateur ou le principal
        }

        // Place le nom d'utilisateur dans le MDC
        MDC.put(USER_KEY, username);

        try {
            // Poursuit l'exécution de la chaîne de filtres
            chain.doFilter(request, response);
        } finally {
            // Très important : retire la valeur du MDC après le traitement de la requête
            // pour éviter la pollution des threads (les threads sont souvent réutilisés)
            MDC.remove(USER_KEY);
        }
    }

    // Tu peux laisser ces méthodes vides pour un filtre simple
    @Override
    public void init(jakarta.servlet.FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}