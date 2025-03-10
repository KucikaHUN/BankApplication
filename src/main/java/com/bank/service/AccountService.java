package com.bank.service;

import java.util.List;

import com.bank.entity.Account;
import com.bank.exchange.Currency;

public interface AccountService {

	public Account findByBillNumber(String billNumber);

	public Account generateAccount(Currency currency);

	public List<Account> findAllByUserId(Long id);

	public void registerAccount(Account account);

	public List<Account> getScaledAccountsDeepCopy(List<Account> accounts, int scale);
}
