package com.project.userservice.config;

import com.project.userservice.models.DBUser;
import com.project.userservice.repositories.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter implements Ordered {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        if (!request.getServletPath().startsWith("/api/v1/auth/register") && !request.getServletPath().startsWith("/api/v1/auth/authenticate") && (authHeader == null || !authHeader.startsWith("Bearer "))) {
            throw new RuntimeException("Invalid request");
        } else if (request.getServletPath().startsWith("/api/v1/auth/register") || request.getServletPath().startsWith("/api/v1/auth/authenticate")) {
            filterChain.doFilter(request, response);
            return;
        }
        jwt = authHeader.substring(7);
        userEmail = jwtService.extractSubject(jwt);
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            DBUser user = userRepository.findByEmail(userEmail).orElseThrow(RuntimeException::new);
            String storedToken = user.getToken(); // Retrieve the token from the user object
            if (storedToken != null && jwtService.isTokenValid(jwt, userEmail) && jwt.equals(storedToken)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        null
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
                filterChain.doFilter(request, response);
                return;
            }
        }
        throw  new RuntimeException("User email is not valid");
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE - 100;
    }
}

