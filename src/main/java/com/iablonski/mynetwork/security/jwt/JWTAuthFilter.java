package com.iablonski.mynetwork.security.jwt;

import com.iablonski.mynetwork.entity.User;
import com.iablonski.mynetwork.security.SecurityConstants;
import com.iablonski.mynetwork.security.service.UserDetailsServiceImpl;
import com.iablonski.mynetwork.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

public class JWTAuthFilter extends OncePerRequestFilter {

    public static final Logger LOG = LoggerFactory.getLogger(JWTAuthFilter.class);

    private JWTUtils jwtUtils;
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    public void setJwtUtils(JWTUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Autowired
    public void setUserDetailsService(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = parseJWT(request);
            if (StringUtils.hasText(jwt) && jwtUtils.validateJWToken(jwt)) {
                String username = jwtUtils.getUsernameFromJWToken(jwt);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication
                        = new UsernamePasswordAuthenticationToken(userDetails,
                        null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception exception) {
            LOG.error(exception.getMessage());
        }
        filterChain.doFilter(request, response);
    }

    private String parseJWT(HttpServletRequest request){
        String headerAuth = request.getHeader(SecurityConstants.HEADER_STRING);
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            return headerAuth.substring(7);
        }
        return null;
    }
}
