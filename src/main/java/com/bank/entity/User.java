package com.bank.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import com.bank.exception.UserException;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue
	private Long id;
	@Column(unique = true, nullable = false)
	private String email;
	@Column(nullable = false)
	private String password;
	@Column(nullable = false)
	private String fullName;

	@Column()
	private String activationCode;
	@Column(nullable = false)
	private boolean enabled;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "users_roles", joinColumns = { @JoinColumn(name = "user_id"), }, inverseJoinColumns = {
			@JoinColumn(name = "role_id") })
	private Set<Role> roles = new HashSet<Role>();

	@OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
	@OrderBy("id ASC")
	private List<Account> accounts = new ArrayList<Account>();

	public User() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		if (email == null) {
			throw new UserException("E-mail can't be null");
		} else if (!checkEmailValid(email)) {
			throw new UserException("Bad E-mail format");
		}
		this.email = email;
	}

	private boolean checkEmailValid(String email) {
		String regexString = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})";

		return Pattern.compile(regexString).matcher(email).matches();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		if (password == null) {
			throw new UserException("Password can't be null");
		} else if (password.length() <= 7) {
			throw new UserException("Password min. need 7 character");
		}
		this.password = password;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		if (fullName == null || fullName.isEmpty()) {
			throw new UserException("Full Name can't be null or empty");
		}
		this.fullName = fullName;
	}

	public String getActivationCode() {
		return activationCode;
	}

	public void setActivationCode(String activationCode) {
		if (activationCode == null) {
			throw new UserException("Activation Code can't be null");
		}
		this.activationCode = activationCode;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public List<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
	}

	public void addAccount(Account account) {
		if (this.accounts == null || this.accounts.isEmpty()) {
			this.accounts = new ArrayList<>();
		}
		this.accounts.add(account);
	}

	public void addRoles(String roleName) {
		if (this.roles == null || this.roles.isEmpty()) {
			this.roles = new HashSet<>();
		}
		this.roles.add(new Role(roleName));
	}

	public boolean haveAccessToAccount(String billNumber) {
		String actualBillNumber;
		for (Account account : accounts) {
			actualBillNumber = account.getBillNumber();
			if (billNumber.equals(actualBillNumber)) {
				return true;
			}
		}
		return false;
	}

}