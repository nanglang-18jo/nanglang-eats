package com.sparta.nanglangeats.global.config;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.sparta.nanglangeats.domain.auth.repository.RefreshTokenRepository;
import com.sparta.nanglangeats.domain.user.service.UserService;
import com.sparta.nanglangeats.global.config.security.entrypoint.CustomAuthenticationEntryPoint;
import com.sparta.nanglangeats.global.config.security.filter.CustomAuthenticationFilter;
import com.sparta.nanglangeats.global.config.security.filter.GlobalExceptionFilter;
import com.sparta.nanglangeats.global.config.security.filter.JwtAuthenticationFilter;
import com.sparta.nanglangeats.global.config.security.handler.CustomAccessDeniedHandler;
import com.sparta.nanglangeats.global.config.security.handler.CustomAuthenticationFailureHandler;
import com.sparta.nanglangeats.global.config.security.handler.CustomAuthenticationSuccessHandler;
import com.sparta.nanglangeats.global.config.security.jwt.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final AuthenticationConfiguration authenticationConfiguration;
	private final UserService userService;
	private final JwtTokenProvider jwtTokenProvider;
	private final RefreshTokenRepository refreshTokenRepository;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
			.httpBasic(AbstractHttpConfigurer::disable)
			.csrf(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable)
			.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

			.authorizeHttpRequests(requests -> requests
				.requestMatchers("/v3/**", "/swagger-ui/**").permitAll()
				.requestMatchers(GET, "/api/users/me").authenticated()
				.requestMatchers(GET, "/api/users/delivery-address/**").authenticated()
				.requestMatchers("/api/auth/**").permitAll()
				.requestMatchers("/api/users/signup").permitAll()
				.requestMatchers(GET, "/api/users/**").permitAll()
				.anyRequest().authenticated())

			.exceptionHandling(exception -> exception
				.accessDeniedHandler(new CustomAccessDeniedHandler())
				.authenticationEntryPoint(new CustomAuthenticationEntryPoint()))

			.addFilterBefore(new GlobalExceptionFilter(), UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(customAuthenticationFilter(authenticationManager(authenticationConfiguration)), UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
			.build();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

	@Bean
	public RoleHierarchy roleHierarchy() {
		return fromHierarchy(
			"ROLE_MANAGER > ROLE_CUSTOMER\n" +
			"ROLE_MANAGER > ROLE_OWNER\n" +
			"ROLE_MASTER > ROLE_MANAGER");
	}


	private CustomAuthenticationFilter customAuthenticationFilter(AuthenticationManager authenticationManager) {
		CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter();
		customAuthenticationFilter.setAuthenticationSuccessHandler(new CustomAuthenticationSuccessHandler(jwtTokenProvider, refreshTokenRepository));
		customAuthenticationFilter.setAuthenticationFailureHandler(new CustomAuthenticationFailureHandler());
		customAuthenticationFilter.setAuthenticationManager(authenticationManager);
		return customAuthenticationFilter;
	}

	private JwtAuthenticationFilter jwtAuthenticationFilter() {
		return new JwtAuthenticationFilter(jwtTokenProvider, userService);
	}
}
