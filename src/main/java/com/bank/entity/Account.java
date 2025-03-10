package com.bank.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.bank.exception.AccountException;
import com.bank.exchange.Currency;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "accounts")
public class Account {

	private final static int billNumberSectionLength = 8;

	@Id
	@GeneratedValue
	private Long id;
	@Column(nullable = false)
	private double balance;
	@Column(unique = true, nullable = false)
	private String billNumber;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Currency currency;
	@OneToMany(mappedBy = "sender")
	private List<Transfer> sentTransfers = new ArrayList<>();
	@OneToMany(mappedBy = "receiver")
	private List<Transfer> receivedTransfers = new ArrayList<>();

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	private User user;

	public Account() {
	}

	public Account(Long id, double balance, String billNumber, Currency currency, List<Transfer> sentTransfers,
			List<Transfer> receivedTransfers, User user) {
		super();
		this.id = id;
		this.balance = balance;
		this.billNumber = billNumber;
		this.currency = currency;
		this.sentTransfers = sentTransfers;
		this.receivedTransfers = receivedTransfers;
		this.user = user;
	}

	public Account(Account old) {
		this(old.getId(), old.getBalance(), old.getBillNumber(), old.getCurrency(), old.getSentTransfers(),
				old.getReceivedTransfers(), old.getUser());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Transfer> getSentTransfers() {
		return sentTransfers;
	}

	public void setSentTransfers(List<Transfer> sentTransfers) {
		this.sentTransfers = sentTransfers;
	}

	public List<Transfer> getReceivedTransfers() {
		return receivedTransfers;
	}

	public void setReceivedTransfers(List<Transfer> receivedTransfers) {
		this.receivedTransfers = receivedTransfers;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		if (balance < 0.0) {
			throw new AccountException("Balance can't be below zero");
		}
		this.balance = balance;
	}

	public String getBillNumber() {
		return billNumber;
	}

	public void setBillNumber(String billNumber) {
		if (!checkValidBillNumber(billNumber)) {
			throw new AccountException("Invalid Bill Number Format");
		} else if (billNumber == null || billNumber.length() < billNumberSectionLength) {
			throw new AccountException(
					"Bill Number can't be null or min need " + billNumberSectionLength + " number length");
		}
		this.billNumber = billNumber;
	}

	private boolean checkValidBillNumber(String billNumber) {

		String regex = "^\\d{8}-?(\\d{8}-?)(\\d{8}-?)?$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(billNumber);
		return matcher.matches();
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		if (currency == null) {
			throw new AccountException("Currency can't be null");
		}
		this.currency = currency;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		if (user == null) {
			throw new AccountException("User can't be null");
		}
		this.user = user;
	}

	public double getScaledDoubleValue(int scale) {
		BigDecimal bd = new BigDecimal(balance).setScale(scale, RoundingMode.HALF_UP);
		return bd.doubleValue();

	}

	public static int getBillNumberSectionLength() {
		return billNumberSectionLength;
	}

	@Override
	public String toString() {
		return "Account [id=" + id + ", balance=" + balance + ", billNumber=" + billNumber + ", currency=" + currency
				+ "]";
	}

}
