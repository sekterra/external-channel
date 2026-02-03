package com.skens.tsms.external_channel.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.authorizeHttpRequests(requests -> requests
				.requestMatchers("/api/**").permitAll()
				.requestMatchers("/bridge", "/bridge/**").permitAll()
				.requestMatchers("/", "/sample", "/forms", "/form-table", "/component-demo", "/login", "/css/**", "/vendor/**", "/error").permitAll()
				.anyRequest().authenticated())
			.formLogin(form -> form.loginPage("/login"))
			.csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"));
		return http.build();
	}
}
