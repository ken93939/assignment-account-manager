package com.acmebank.accountmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests((authz) -> authz
			.requestMatchers(
				new AntPathRequestMatcher("/h2-console/**")
			).permitAll()
			.anyRequest().authenticated()
		);
		http.csrf((csrf) -> csrf.disable()
		);
		http.headers((headers) -> headers
			.frameOptions(
				HeadersConfigurer.FrameOptionsConfig::disable
			)
		);
		http.httpBasic(Customizer.withDefaults());
		return http.build();
	}
}
