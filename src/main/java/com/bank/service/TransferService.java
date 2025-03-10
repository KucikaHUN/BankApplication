package com.bank.service;

import java.util.List;

import com.bank.entity.Account;
import com.bank.entity.Transfer;
import com.bank.exception.NotEnoughMoneyToTransfer;

public interface TransferService {

	public List<Transfer> getSenderTransfers(String billNumber);

	public List<Transfer> getReceiverTransfers(String billNumber);

	public void transfer(Account sender, Account receive, double amount, String text) throws NotEnoughMoneyToTransfer;
}
