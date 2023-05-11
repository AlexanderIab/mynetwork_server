package com.iablonski.mynetwork.security;

import com.iablonski.mynetwork.entity.User;
import com.iablonski.mynetwork.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

public class JWTAuthFilter extends OncePerRequestFilter {

    public static final Logger LOG = LoggerFactory.getLogger(JWTAuthFilter.class);

    private JWTokenProvider provider;
    private UserService userService;

    @Autowired
    public void setProvider(JWTokenProvider provider) {
        this.provider = provider;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = getJWTFromRequest(request);
            if (StringUtils.hasText(jwt) && provider.validateToken(jwt)) {
                Long userId = provider.getUserIdFromToken(jwt);
                User user = userService.loadUserById(userId);
                UsernamePasswordAuthenticationToken authentication
                        = new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception exception) {
            LOG.error(exception.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private String getJWTFromRequest(HttpServletRequest request){
        String bearToken = request.getHeader(SecurityConstants.HEADER_STRING);
        if(StringUtils.hasText(bearToken) && bearToken.startsWith(SecurityConstants.TOKEN_PREFIX)){
            return bearToken.split("")[1];
        }
        return null;
    }
}
