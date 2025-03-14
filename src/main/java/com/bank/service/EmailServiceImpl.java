package com.bank.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.bank.entity.User;

@Service
public class EmailServiceImpl implements EmailService {

	@Value("${spring.mail.username}")
	private String MESSAGE_FROM;

	private JavaMailSender javaMailSender;

	private LoggerService loggerService;

	public EmailServiceImpl(JavaMailSender javaMailSender, LoggerService loggerService) {
		this.javaMailSender = javaMailSender;
		this.loggerService = loggerService;
	}

	@Override
	public void sendActivationMessage(User user) {
		SimpleMailMessage message = null;

		try {
			message = new SimpleMailMessage();
			message.setFrom(MESSAGE_FROM);
			message.setTo(user.getEmail());
			message.setSubject("Success register");
			message.setText("Welcome " + user.getFullName() + "! \n \nThank you, for joining to the Bank!\n"
					+ "Please activate your profile: https://bankapplication-vuik.onrender.com/activation/" + user.getActivationCode());

			javaMailSender.send(message);

			loggerService.log(LocalDateTime.now(), "Activation Message Sending Success", String.valueOf(user.getId()));

		} catch (Exception e) {
			loggerService.log(LocalDateTime.now(), "Activation Message Sending Fail", String.valueOf(user.getId()));
		}
	}

}
