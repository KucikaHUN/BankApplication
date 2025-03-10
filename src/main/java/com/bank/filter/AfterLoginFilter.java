package com.bank.filter;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.bank.page.Page;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AfterLoginFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		boolean canContinue = true;

		String url = request.getRequestURI();

		if (thisBeforeLoginUrl(url)) {

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

			if (authentication != null && authentication.isAuthenticated()) {

				response.sendRedirect("/index");
				canContinue = false;
			}
		}
		if (canContinue) {
			filterChain.doFilter(request, response);
		}
	}

	private boolean thisBeforeLoginUrl(String url) {
		boolean result = false;
		AntPathMatcher pathMatcher = new AntPathMatcher();
		String[] beforeLoginUrls = Page.getBeforeLoginUrls();
		String beforeLoginUrl;
		int length = beforeLoginUrls.length;
		for (int i = 0; i < length; i++) {
			beforeLoginUrl = beforeLoginUrls[i];
			if (pathMatcher.match(beforeLoginUrl, url)) {
				result = true;
				i = length;
			}
		}
		return result;
	}
}