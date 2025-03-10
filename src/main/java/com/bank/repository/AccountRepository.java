package com.bank.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bank.entity.Account;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {

	Account findByBillNumber(String billNumber);

	List<Account> findAllByUserIdOrderById(Long id);

}
