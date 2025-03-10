package com.bank.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bank.entity.Account;
import com.bank.entity.Transfer;
import com.bank.entity.User;
import com.bank.exception.EntityException;
import com.bank.exception.NotEnoughMoneyToTransfer;
import com.bank.service.AccountService;
import com.bank.service.AuthService;
import com.bank.service.LoggerService;
import com.bank.service.TransferService;

@Controller
@RequestMapping("/transfer")
public class TransferController {

	private TransferService transferService;
	private AccountService accountService;
	private LoggerService loggerService;
	private AuthService authService;

	public TransferController(TransferService transferService, AccountService accountService,
			LoggerService loggerService, AuthService authService) {
		this.transferService = transferService;
		this.accountService = accountService;
		this.loggerService = loggerService;
		this.authService = authService;
	}

	@GetMapping("/newtransfer")
	public String transfer(@RequestParam String senderBillNumber, Model model) {

		Account sender = accountService.findByBillNumber(senderBillNumber);

		if (sender == null) {
			return "redirect:/error";
		}

		model.addAttribute("sender", sender);
		return "afterlogin/transfer";
	}

	@GetMapping("/transferhistory")
	public String getTransferHistory(@RequestParam String billNumber, Model model) {

		if (!haveAccessToAccount(billNumber)) {
			return "redirect:/error";
		}

		List<Transfer> senderList = transferService.getSenderTransfers(billNumber);
		List<Transfer> receiverList = transferService.getReceiverTransfers(billNumber);
		model.addAttribute("senderList", senderList);
		model.addAttribute("receiverList", receiverList);

		return "afterlogin/transferhistory";
	}

	@Transactional
	@PostMapping("/maketransfer")
	public String makeTransfer(@RequestParam String senderBillNumber, @RequestParam String receiverBillNumber,
			@RequestParam double amount, @RequestParam String text, Model model) {

		Account sender = accountService.findByBillNumber(senderBillNumber);
		Account receiver = accountService.findByBillNumber(receiverBillNumber);

		if (sender == null || !haveAccessToAccount(senderBillNumber)) {
			return "redirect:/error";
		}

		if (receiver == null) {
			model.addAttribute("sender", sender);
			model.addAttribute("errorMessage", "Bill not found, try again");
			return "afterlogin/transfer";
		}

		try {
			transferService.transfer(sender, receiver, amount, text);
		} catch (NotEnoughMoneyToTransfer | EntityException e) {
			model.addAttribute("sender", sender);
			setErrorMessage(e, model);
			return "afterlogin/transfer";
		}

		return "redirect:/index";
	}

	private void setErrorMessage(RuntimeException e, Model model) {
		if (e instanceof NotEnoughMoneyToTransfer) {
			model.addAttribute("errorMessage", "Not enought money, try again");
		}
		if (e instanceof EntityException) {
			loggerService.log(LocalDateTime.now(), "Transfer method problem",
					"Check the TransferController.makeTransfer method");
			model.addAttribute("errorMessage", "Call the developer: Transfer method problem");
		}
	}

	private boolean haveAccessToAccount(String billNumber) {
		User user = authService.getAuthUser();
		if (user == null) {
			return false;
		}
		return user.haveAccessToAccount(billNumber);
	}
}
