package com.bank.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bank.entity.Account;
import com.bank.entity.User;
import com.bank.service.AccountService;
import com.bank.service.AuthService;

@Controller
@RequestMapping("/index")
public class IndexController {

	private AccountService accountService;
	private AuthService authService;

	public IndexController(AccountService accountService, AuthService authService) {
		this.accountService = accountService;
		this.authService = authService;
	}

	@GetMapping
	public String index(Model model) {

		User user = authService.getAuthUser();

		if (user == null) {
			return "redirect:/error";
		}

		List<Account> scaledAccounts = accountService.getScaledAccountsDeepCopy(user.getAccounts(), 2);
		model.addAttribute("accounts", scaledAccounts);

		return "afterlogin/index";
	}

}
