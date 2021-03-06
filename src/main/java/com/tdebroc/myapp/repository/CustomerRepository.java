package com.tdebroc.myapp.repository;

import java.util.List;

import com.tdebroc.myapp.domain.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, Long> {

    List<Customer> findByLastName(String lastName);
}
