package com.example.demop.security.auth;
import com.example.demop.security.CustomUserDetailsService;
import com.example.demop.security.TokenUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private TokenUtils tokenUtils;

    private CustomUserDetailsService userDetailsService;

    public TokenAuthenticationFilter(TokenUtils tokenHelper, CustomUserDetailsService userDetailsService) {
        this.tokenUtils = tokenHelper;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        System.out.println("in  doFilterInternal");

        String authToken = tokenUtils.getToken(request);
        String username;

        if (authToken != null) {
            username = tokenUtils.getUsernameFromToken(authToken);

            if (username != null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // check if token is valid or not, and create authentification
                if (tokenUtils.validateToken(authToken, userDetails)) {
                    TokenBasedAuthentication authentication = new TokenBasedAuthentication(userDetails);
                    authentication.setToken(authToken);
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                }
            }
        }
        filterChain.doFilter(request, response);
    }

}
