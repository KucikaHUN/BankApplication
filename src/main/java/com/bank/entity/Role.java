package com.bank.entity;

import java.util.HashSet;
import java.util.Set;

import com.bank.exception.RoleException;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "roles")
public class Role {

	@Id
	@GeneratedValue
	private Long id;

	private String role;

	private Role() {
	}

	@ManyToMany(mappedBy = "roles")
	private Set<User> users = new HashSet<User>();

	public Role(String role) {
		this.role = role;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		if (role == null || role.isEmpty()) {
			throw new RoleException("Role can't be null or empty");
		}
		this.role = role;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	@Override
	public String toString() {
		return "Role [id=" + id + ", role=" + role + "]";
	}

}
