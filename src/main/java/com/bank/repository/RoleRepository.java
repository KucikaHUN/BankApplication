package com.bank.repository;
import org.springframework.data.repository.CrudRepository;

import com.bank.entity.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {

	Role findByRole(String role);
	
}