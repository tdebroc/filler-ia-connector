package com.tdebroc.myapp.repository;

import com.tdebroc.myapp.domain.Content;
import com.tdebroc.myapp.domain.Customer;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ContentRepository extends CrudRepository<Content, Long> {

}
