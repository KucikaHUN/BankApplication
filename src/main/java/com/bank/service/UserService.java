package com.bank.service;

import com.bank.entity.User;

public interface UserService {

	public void registerUser(User user);

	public User findByEmail(String email);

	public void userActivation(String code);

}
