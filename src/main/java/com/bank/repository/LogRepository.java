package com.bank.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bank.entity.Log;

@Repository
public interface LogRepository extends CrudRepository<Log, Long> {

	List<Log> findAll();

}
