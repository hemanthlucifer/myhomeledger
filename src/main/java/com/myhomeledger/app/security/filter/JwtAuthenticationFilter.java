package com.myhomeledger.app.security.filter;

import com.myhomeledger.app.security.config.AuthCookieNames;
import com.myhomeledger.app.security.jwt.JwtService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (isPublicPath(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = extractAccessToken(request);
        if (token == null || token.isBlank()) {
            denyUnauthorized(request, response);
            return;
        }

        try {
            JwtService.ParsedAccessToken parsed = jwtService.parseAccessToken(token);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    parsed.userId(),
                    null,
                    List.of(new SimpleGrantedAuthority("ROLE_USER"))
            );
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (JwtException | IllegalArgumentException e) {
            denyUnauthorized(request, response);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private static String extractAccessToken(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null && header.regionMatches(true, 0, "Bearer ", 0, 7)) {
            String bearer = header.substring(7).trim();
            if (!bearer.isEmpty()) {
                return bearer;
            }
        }
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (AuthCookieNames.ACCESS_TOKEN.equals(c.getName())) {
                    String v = c.getValue();
                    if (v != null && !v.isBlank()) {
                        return v.trim();
                    }
                }
            }
        }
        return null;
    }

    private static void denyUnauthorized(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String accept = request.getHeader(HttpHeaders.ACCEPT);
        if (accept != null && accept.contains("text/html")) {
            String ctx = request.getContextPath();
            String prefix = ctx == null ? "" : ctx;
            response.sendRedirect(prefix + "/");
            return;
        }
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }

    private static boolean isPublicPath(HttpServletRequest request) {
        String path = servletPath(request);
        String method = request.getMethod();
        if (path.startsWith("/api/v1/auth/")) {
            return true;
        }
        if ("/api/v1/users/signup".equals(path) && "POST".equalsIgnoreCase(method)) {
            return true;
        }
        if (path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs") || "/swagger-ui.html".equals(path)) {
            return true;
        }
        if ("/".equals(path)) {
            return true;
        }
        if ("/signup".equals(path)) {
            return true;
        }
        if ("/web/login".equals(path) && "POST".equalsIgnoreCase(method)) {
            return true;
        }
        if ("/web/signup".equals(path) && "POST".equalsIgnoreCase(method)) {
            return true;
        }
        if ("/web/logout".equals(path) && "POST".equalsIgnoreCase(method)) {
            return true;
        }
        return "/error".equals(path);
    }

    private static String servletPath(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String context = request.getContextPath();
        if (context != null && !context.isEmpty() && uri.startsWith(context)) {
            uri = uri.substring(context.length());
        }
        return uri.isEmpty() ? "/" : uri;
    }
}
