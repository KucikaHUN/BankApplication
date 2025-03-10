package com.bank.service;

import java.time.LocalDateTime;

public interface LoggerService {

	public void log(LocalDateTime date, String actionName, String text);
}
