package com.bookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bookstore.model.Author;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long>{
	
	Author findByName(@Param("name") String name);
}
