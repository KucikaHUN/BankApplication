package com.bank.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.bank.entity.Log;
import com.bank.repository.LogRepository;

@Service
public class LoggerServiceImpl implements LoggerService {

	private LogRepository logRepository;

	public LoggerServiceImpl(LogRepository logRepository) {
		this.logRepository = logRepository;
	}

	@Override
	public void log(LocalDateTime date, String actionName, String text) {
		Log log = new Log(date, actionName, text);
		logRepository.save(log);
	}

}
