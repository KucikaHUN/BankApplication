package com.bank.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bank.entity.Account;
import com.bank.entity.User;
import com.bank.exception.EntityException;
import com.bank.exchange.Currency;
import com.bank.service.AccountService;
import com.bank.service.AuthService;
import com.bank.service.LoggerService;

@Controller
@RequestMapping("/billregistration")
public class BillRegistrationController {

	private AccountService accountService;
	private AuthService authService;
	private LoggerService loggerService;

	public BillRegistrationController(AccountService accountService, AuthService authService,
			LoggerService loggerService) {
		this.accountService = accountService;
		this.authService = authService;
		this.loggerService = loggerService;
	}

	@GetMapping
	public String billRegistration(Model model) {

		Currency[] currencies = Currency.values();

		List<String> currenciesList = changeArrayToListCurrency(currencies);

		model.addAttribute("currencies", currenciesList);

		return "afterlogin/billregistration";
	}

	private List<String> changeArrayToListCurrency(Currency[] currencies) {

		List<String> result = new ArrayList<>();
		String currencyString;

		for (int i = 0; i < currencies.length; i++) {
			currencyString = currencies[i].toString();
			result.add(currencyString);
		}
		return result;
	}

	@Transactional
	@PostMapping("/createaccount")
	public String createAccount(@RequestParam(required = false) String currency,
			RedirectAttributes redirectAttributes) {

		User user = authService.getAuthUser();
		if (user == null) {
			return redirectWithError(redirectAttributes, "Please choose currency");
		}

		Currency currencyEnum = parseCurrency(currency);
		if (currencyEnum == null) {
			loggerService.log(LocalDateTime.now(), "Invalid Currency Format",
					"Check the BillRegistrationController.createAccount method");
			return redirectWithError(redirectAttributes, "Call the developer: Invalid Currency Format");
		}

		Account account = createAccountWithBalance(currencyEnum);
		if (account == null) {
			loggerService.log(LocalDateTime.now(), "Failed to create account",
					"Check the BillRegistrationController.createAccount method");
			return redirectWithError(redirectAttributes, "Call the developer: Failed to create account");
		}

		try {
			registerAccount(user, account);
		} catch (EntityException e) {
			loggerService.log(LocalDateTime.now(), "Failed to register account",
					"Check the BillRegistrationController.createAccount method");
			return redirectWithError(redirectAttributes, "Call the developer: Failed to register account");
		}

		return "redirect:/index";
	}

	private String redirectWithError(RedirectAttributes redirectAttributes, String errorMessage) {
		redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
		return "redirect:/billregistration";
	}

	private void registerAccount(User user, Account account) throws EntityException {
		account.setUser(user);
		user.addAccount(account);
		accountService.registerAccount(account);
	}

	private Account createAccountWithBalance(Currency currencyEnum) {
		try {
			Account account = accountService.generateAccount(currencyEnum);
			account.setBalance(1000);
			return account;
		} catch (EntityException e) {
			return null;
		}
	}

	private Currency parseCurrency(String currency) {
		try {
			return Currency.valueOf(currency);
		} catch (IllegalArgumentException | NullPointerException e) {
			return null;
		}
	}
}
