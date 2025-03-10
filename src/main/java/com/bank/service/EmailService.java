package com.bank.service;

import com.bank.entity.User;

public interface EmailService {

	public void sendActivationMessage(User user);
}
