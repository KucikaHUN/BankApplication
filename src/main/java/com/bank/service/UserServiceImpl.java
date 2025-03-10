package com.bank.service;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bank.entity.Role;
import com.bank.entity.User;
import com.bank.exception.EmailAlreadyExistsException;
import com.bank.exception.UserNotFoundException;
import com.bank.repository.RoleRepository;
import com.bank.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

	private final UserRepository userRepository;

	private final RoleRepository roleRepository;

	private LoggerService loggerService;

	private final PasswordEncoder passwordEncoder;

	private final String USER_ROLE = "USER";

	private static Random random;

	static {
		random = new Random();
	}

	public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
			PasswordEncoder passwordEncoder, LoggerService loggerService) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
		this.loggerService = loggerService;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = findByEmail(username);
		if (user == null) {
			throw new UsernameNotFoundException(username);
		}

		return new UserDetailsImpl(user);
	}

	@Override
	public void registerUser(User user) throws EmailAlreadyExistsException {

		User userCheck = userRepository.findByEmail(user.getEmail());

		if (userCheck != null) {
			throw new EmailAlreadyExistsException("Exist Email");
		}
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);

		Role userRole = roleRepository.findByRole(USER_ROLE);
		if (userRole != null) {
			user.getRoles().add(userRole);
		} else {
			user.addRoles(USER_ROLE);
		}

		user.setEnabled(false);
		user.setActivationCode(generateKey());
		userRepository.save(user);

		loggerService.log(LocalDateTime.now(), "Register User Success", String.valueOf(user.getId()));
	}

	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public String generateKey() {

		char[] word = new char[16];
		for (int i = 0; i < word.length; i++) {
			word[i] = (char) ('a' + random.nextInt(26));
		}
		return new String(word);
	}

	@Override
	public void userActivation(String code) {
		User user = userRepository.findByActivationCode(code);
		if (user == null) {
			throw new UserNotFoundException("User not found");
		}
		user.setEnabled(true);
		user.setActivationCode("");
		userRepository.save(user);

		loggerService.log(LocalDateTime.now(), "Activation User Success", String.valueOf(user.getId()));

	}
}
