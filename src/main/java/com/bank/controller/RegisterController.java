package com.bank.controller;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bank.entity.User;
import com.bank.exception.EmailAlreadyExistsException;
import com.bank.service.EmailService;
import com.bank.service.UserService;

@Controller
@RequestMapping("/registration")
public class RegisterController {

	private UserService userService;
	private EmailService emailService;

	public RegisterController(UserService userService, EmailService emailService) {
		this.userService = userService;
		this.emailService = emailService;
	}

	@GetMapping
	public String registration(Model model) {
		model.addAttribute("user", new User());
		return "beforelogin/registration";
	}

	@Transactional
	@PostMapping("/reg")
	public String reg(@ModelAttribute User user, BindingResult result, RedirectAttributes redirectAttributes) {

		if (result.hasErrors()) {
			return redirectWithError(redirectAttributes,
					"Bad Datas (E-mail format, Password(min. 7 character) or Full name(Can't be empty)");
		}

		try {
			userService.registerUser(user);
		} catch (EmailAlreadyExistsException e) {
			return redirectWithError(redirectAttributes, "Email is exist");
		}

		emailService.sendActivationMessage(user);

		return "redirect:/login";
	}

	private String redirectWithError(RedirectAttributes redirectAttributes, String error) {
		redirectAttributes.addFlashAttribute("errorMessage", error);
		return "redirect:/registration";
	}
}
