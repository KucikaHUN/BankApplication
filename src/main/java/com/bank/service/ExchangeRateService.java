package com.bank.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bank.exchange.ExchangeRateResponse;

@Service
public class ExchangeRateService {

	private static final String API_URL = "https://api.exchangerate-api.com/v4/latest/";

	public Double getExchangeRate(String baseCurrency, String targetCurrency) {
		RestTemplate restTemplate = new RestTemplate();
		String url = API_URL + baseCurrency;

		ExchangeRateResponse response = restTemplate.getForObject(url, ExchangeRateResponse.class);

		Double result = null;

		if (response != null && response.getRates().containsKey(targetCurrency)) {
			result = response.getRates().get(targetCurrency);
		}
		return result;
	}
}
