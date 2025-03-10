package com.bank.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.bank.entity.Account;
import com.bank.exception.EntityException;
import com.bank.exchange.Currency;
import com.bank.repository.AccountRepository;

@Service
public class AccountServiceImpl implements AccountService {

	private final String BANK_NUMBER_START = "11223344-";

	private AccountRepository accountRepository;

	private LoggerService loggerService;

	private static Random random;

	static {
		random = new Random();
	}

	public AccountServiceImpl(AccountRepository accountRepository, LoggerService loggerService) {
		this.accountRepository = accountRepository;
		this.loggerService = loggerService;
	}

	@Override
	public Account findByBillNumber(String billNumber) {
		return accountRepository.findByBillNumber(billNumber);
	}

	@Override
	public Account generateAccount(Currency currency) throws EntityException {

		Account account = new Account();
		account.setBalance(0);
		account.setBillNumber(generateNotExistBillNumber());

		account.setCurrency(currency);

		return account;
	}

	private String generateNotExistBillNumber() {
		String billNumber = generateBillNumber();
		while (isExistBillNumber(billNumber)) {
			billNumber = generateBillNumber();
		}
		return billNumber;
	}

	private String generateBillNumber() {
		String result = BANK_NUMBER_START;
		int randomNumber;
		for (int i = 0; i < Account.getBillNumberSectionLength(); i++) {
			randomNumber = random.nextInt(0, 10);
			result += randomNumber;
		}

		return result;
	}

	private boolean isExistBillNumber(String billNumber) {
		boolean result = false;
		Account account = findByBillNumber(billNumber);
		if (account != null) {
			result = true;
		}
		return result;
	}

	@Override
	public List<Account> getScaledAccountsDeepCopy(List<Account> accounts, int scale) {
		List<Account> result = new ArrayList<>();
		for (Account account : accounts) {
			Account newAccount = new Account(account);
			newAccount.setBalance(account.getScaledDoubleValue(scale));
			result.add(newAccount);
		}
		return result;
	}

	@Override
	public void registerAccount(Account account) {
		accountRepository.save(account);
		loggerService.log(LocalDateTime.now(), "Register Account Success", String.valueOf(account.getId()));
	}

	@Override
	public List<Account> findAllByUserId(Long id) {
		return accountRepository.findAllByUserIdOrderById(id);
	}
}
