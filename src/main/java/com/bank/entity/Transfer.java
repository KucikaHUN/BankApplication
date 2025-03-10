package com.bank.entity;

import java.time.LocalDateTime;

import com.bank.exception.TransferException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "transfers")
public class Transfer {

	@Id
	@GeneratedValue
	private Long id;
	@Column(nullable = false)
	private LocalDateTime date;
	@Column(nullable = true)
	private String text;
	@Column(nullable = false)
	private double sendAmount;
	@Column(nullable = false)
	private double receiveAmount;
	@ManyToOne
	@JoinColumn(name = "sender")
	private Account sender;

	@ManyToOne
	@JoinColumn(name = "receiver")
	private Account receiver;

	public Transfer() {
	}

	public Account getSender() {
		return sender;
	}

	public void setSender(Account sender) {
		if (sender == null) {
			throw new TransferException("Sender can't be null");
		}
		this.sender = sender;
	}

	public Account getReceiver() {
		return receiver;
	}

	public void setReceiver(Account receiver) {
		if (receiver == null) {
			throw new TransferException("Receiver can't be null");
		}
		this.receiver = receiver;
	}

	public double getSendAmount() {
		return sendAmount;
	}

	public void setSendAmount(double sendAmount) {
		if (sendAmount < 0) {
			throw new TransferException("Send amount can't below zero");
		}
		this.sendAmount = sendAmount;
	}

	public double getReceiveAmount() {
		return receiveAmount;
	}

	public void setReceiveAmount(double receiveAmount) {
		if (receiveAmount < 0) {
			throw new TransferException("Receive amount can't below zero");
		}
		this.receiveAmount = receiveAmount;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setDate(LocalDateTime date) {
		if (date == null) {
			throw new TransferException("Date can't be null");
		}
		this.date = date;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		if (text == null) {
			text = "";
		}
		this.text = text;
	}

}
