package com.bank.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.bank.entity.Account;
import com.bank.entity.Transfer;
import com.bank.exception.EntityException;
import com.bank.exception.NotEnoughMoneyToTransfer;
import com.bank.repository.AccountRepository;
import com.bank.repository.TransferRepository;

@Service
public class TransferServiceImpl implements TransferService {

	private TransferRepository transferRepository;

	private ExchangeRateService exchangeRateService;

	private AccountRepository accountRepository;

	private LoggerService loggerService;

	public TransferServiceImpl(TransferRepository transferRepository, AccountRepository accountRepository,
			ExchangeRateService exchangeRateService, LoggerService loggerService) {
		this.transferRepository = transferRepository;
		this.accountRepository = accountRepository;
		this.exchangeRateService = exchangeRateService;
		this.loggerService = loggerService;
	}

	@Override
	public List<Transfer> getSenderTransfers(String billNumber) {
		return transferRepository.findAllBySender_BillNumber(billNumber);
	}

	@Override
	public List<Transfer> getReceiverTransfers(String billNumber) {
		return transferRepository.findAllByReceiver_BillNumber(billNumber);
	}

	@Override
	public void transfer(Account sender, Account receiver, double amount, String text)
			throws NotEnoughMoneyToTransfer, EntityException {

		String senderCurrency = sender.getCurrency().toString();
		String receiverCurrency = receiver.getCurrency().toString();
		double rate = exchangeRateService.getExchangeRate(senderCurrency, receiverCurrency);
		double exchangeAmount = amount * rate;

		if (sender.getBalance() >= amount) {

			sender.setBalance(sender.getBalance() - amount);
			receiver.setBalance(receiver.getBalance() + exchangeAmount);

			accountRepository.save(sender);
			accountRepository.save(receiver);

			Transfer transfer = new Transfer();
			transfer.setDate(LocalDateTime.now());
			transfer.setSender(sender);
			transfer.setReceiver(receiver);
			transfer.setSendAmount(amount);
			transfer.setReceiveAmount(exchangeAmount);
			transfer.setText(text);

			transferRepository.save(transfer);

			loggerService.log(LocalDateTime.now(), "Transfer Complete", String.valueOf(transfer.getId()));

		} else {
			throw new NotEnoughMoneyToTransfer("Not enought money to transfer");
		}

	}

}
