package com.bank.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bank.exception.UserNotFoundException;
import com.bank.service.UserService;

import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/activation")
public class ActivationController {

	private UserService userService;

	public ActivationController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/{code}")
	public String activation(@PathVariable("code") String code, HttpServletResponse response) {

		try {
			userService.userActivation(code);
		} catch (UserNotFoundException e) {
			return "beforelogin/activationfail";
		}

		return "beforelogin/activationsuccess";
	}
}
