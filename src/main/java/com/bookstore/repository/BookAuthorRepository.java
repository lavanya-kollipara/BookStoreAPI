package com.bookstore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bookstore.model.BookAuthor;

@Repository
public interface BookAuthorRepository extends JpaRepository<BookAuthor, Long>{
	List<BookAuthor> findByAuthorId(Long authorId);
	List<BookAuthor> findByBookId(Long bookId);
	void deleteByBookId(Long bookId);
}
