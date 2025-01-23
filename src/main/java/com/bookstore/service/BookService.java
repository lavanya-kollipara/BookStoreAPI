package com.bookstore.service;

import java.util.List;

import com.bookstore.dto.BookDTO;
import com.bookstore.model.Author;
import com.bookstore.model.Book;

public interface BookService {
	
	public List<BookDTO>  getBookByTitle(String title);
	
	public List<BookDTO> getBookByAuthor(String author);
	
	public BookDTO updateBook(Book book,List<Author> authorList);
	
	public BookDTO addNewBook(Book book, List<Author> authorList);
	
	public void deleteBook(String title);
	public List<BookDTO> getAll();

}
