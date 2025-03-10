package com.bank.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.bank.filter.AfterLoginFilter;
import com.bank.page.Page;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final AfterLoginFilter afterLoginFilter;

	public SecurityConfig(AfterLoginFilter loginPageFilter) {
		this.afterLoginFilter = loginPageFilter;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(authz -> authz.requestMatchers(Page.getBeforeLoginUrls()).permitAll()
				.requestMatchers("/css/**").permitAll().requestMatchers(Page.getAfterLoginUrls()).hasRole("USER")
				.requestMatchers(Page.getDefaultUrls()).permitAll().anyRequest().denyAll())
				.formLogin(login -> login.loginPage("/login").permitAll().defaultSuccessUrl("/index", true))
				.logout(logout -> logout.logoutSuccessUrl("/login?logout").permitAll())
				.addFilterBefore(afterLoginFilter, UsernamePasswordAuthenticationFilter.class);
		http.csrf(csrf -> csrf.disable());
		http.exceptionHandling(exception -> exception.accessDeniedPage("/error"));
		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
