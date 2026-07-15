package com.binhtv.authservice.configuration;

import com.binhtv.authservice.security.jwt.JwtAuthenticationEntryPoint;
import com.binhtv.authservice.security.jwt.JwtAuthenticationFilter;
import com.binhtv.authservice.security.oauth.OAuthAuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	private final JwtAuthenticationEntryPoint unauthorizedHandler;
	private final OAuthAuthenticationSuccessHandler oauthSuccessHandler;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		//@formatter:off

		return http
				.csrf(CsrfConfigurer::disable)
				.cors(CorsConfigurer::disable)
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
				.authorizeHttpRequests(request -> request.requestMatchers(
															  "/api/auth/register",
													  "/api/auth/login",
													  "/api/auth/forgot-password",
													  "/api/auth/reset-password",
													  "/api/auth/oauth2/**",
														      "/v3/api-docs/**",
												          "/swagger-ui/**",
													      "/swagger-ui.html",
													      "/actuator/**")
											   .permitAll()
											   .anyRequest()
											   .authenticated())
				.sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
				.oauth2Login(oauth -> oauth
						.authorizationEndpoint(endpoint -> endpoint.baseUri("/api/auth/oauth2/authorization"))
						.redirectionEndpoint(endpoint -> endpoint.baseUri("/api/auth/oauth2/callback/*"))
						.successHandler(oauthSuccessHandler))
				.exceptionHandling(handler -> handler.authenticationEntryPoint(unauthorizedHandler))
				.build();

		//@formatter:on
	}

}
