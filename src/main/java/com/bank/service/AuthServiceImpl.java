package com.bank.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.bank.entity.User;

@Service
public class AuthServiceImpl implements AuthService {

	private UserService userService;

	public AuthServiceImpl(UserService userService) {
		this.userService = userService;
	}

	public User getAuthUser() {

		User result = null;

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null && authentication.isAuthenticated()) {

			String email = authentication.getName();

			result = userService.findByEmail(email);
		}

		return result;
	}
}
