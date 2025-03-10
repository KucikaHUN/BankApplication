package com.bank.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bank.entity.Transfer;

@Repository
public interface TransferRepository extends CrudRepository<Transfer, Long> {

	List<Transfer> findAllBySender_BillNumber(String billNumber);

	List<Transfer> findAllByReceiver_BillNumber(String billNumber);
}
