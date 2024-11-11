package com.sparta.nanglangeats.global.config.security.filter;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.sparta.nanglangeats.domain.user.service.UserService;
import com.sparta.nanglangeats.global.config.security.jwt.JwtTokenProvider;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeaderValue = request.getHeader(JwtTokenProvider.AUTHORIZATION_HEADER);

        if (StringUtils.hasText(authorizationHeaderValue)) {
            String token = jwtTokenProvider.substringToken(authorizationHeaderValue);

            if (jwtTokenProvider.isValidToken(token)) {
                String userId = jwtTokenProvider.getSubject(token);
                String authority = jwtTokenProvider.getAuthority(token);
                setAuthentication(userId, authority);
            }
        }

        filterChain.doFilter(request, response);
    }

    private void setAuthentication(String userId, String authority) {
        Authentication authentication = createAuthentication(Long.valueOf(userId), authority);
        SecurityContextHolder.getContextHolderStrategy().getContext().setAuthentication(authentication);
    }

    private Authentication createAuthentication(Long userId, String authority) {
        return new UsernamePasswordAuthenticationToken(userService.getUserById(userId), null, List.of(new SimpleGrantedAuthority(authority)));
    }
}
