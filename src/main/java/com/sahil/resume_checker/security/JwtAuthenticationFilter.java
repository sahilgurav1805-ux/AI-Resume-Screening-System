package com.sahil.resume_checker.security;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService ;
    private final UserDetailsService userDetailsService ;

    public JwtAuthenticationFilter(
            JwtService jwtService ,
            UserDetailsService userDetailsService
    ) {
        this.jwtService = jwtService ;
        this.userDetailsService = userDetailsService ;
    }


    @Override
    protected void doFilterInternal(
            HttpServletRequest request ,
            HttpServletResponse response ,
            FilterChain filterChain
    ) throws ServletException, IOException {
        System.out.println("Request URI: " + request.getRequestURI());
        System.out.println("Request Mthod: " + request.getMethod());

        final String authHeader = request.getHeader("Authorization");

        System.out.println("Authorization header received: " +(authHeader != null));
        System.out.println("Bearer token received: " +
                (authHeader != null && authHeader.startsWith("Bearer ")));

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {

    filterChain.doFilter(request ,response);
    return;
        }

        String token = authHeader.substring(7);

        String email ;

        try {
            email = jwtService.extractEmail(token) ;
            System.out.println("Extracted email: " + email);

        } catch (JwtException | IllegalArgumentException e) {
            filterChain.doFilter(request , response);
            return;
        }

        if (email != null &&
                SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails =
                    userDetailsService.loadUserByUsername(email);
            System.out.println("User authorities: " +
                    userDetails.getAuthorities());

            System.out.println("JWT is valid: " +
                    jwtService.isTokenValid(token , userDetails));

            if (jwtService.isTokenValid(token , userDetails)) {

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails , null , userDetails.getAuthorities()
                        ) ;

                authentication.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);

                System.out.println("Authentication set: "+
                        SecurityContextHolder.getContext().getAuthentication().isAuthenticated());

                System.out.println("Authorities: "+
                        SecurityContextHolder.getContext().getAuthentication().getAuthorities());
            }
        }
        filterChain.doFilter(request ,response);
        System.out.println("Jwt filter completed");
        System.out.println("final authentication: " +
                SecurityContextHolder.getContext().getAuthentication());
    }



}
