package com.maahir.researchlnkapi.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SaveOrcidEmailFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURI().equals("/oauth2/authorization/orcid")) {
            String email = request.getParameter("email");
            if (email != null && !email.isBlank()) {
                request.getSession().setAttribute("ORCID_EMAIL", email);
            }
        }
        filterChain.doFilter(request, response);
    }
}
