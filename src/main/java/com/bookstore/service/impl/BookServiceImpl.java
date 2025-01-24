package com.bookstore.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.dto.BookDTO;
import com.bookstore.exception.DuplicateResourceException;
import com.bookstore.exception.NotFoundException;
import com.bookstore.model.Author;
import com.bookstore.model.Book;
import com.bookstore.model.BookAuthor;
import com.bookstore.repository.AuthorRepository;
import com.bookstore.repository.BookAuthorRepository;
import com.bookstore.repository.BookRepository;
import com.bookstore.service.BookService;

@Service
public class BookServiceImpl implements BookService {

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private AuthorRepository authorRepository;

	@Autowired
	private BookAuthorRepository bookAuthorRepository;

	@Autowired
	private ModelMapper mapper;

	@Override
	public List<BookDTO> getBookByTitle(String title) {
		List<Book> books = bookRepository.findByTitle(title);
		if (books.isEmpty()) {
			return null;
		}
		List<BookDTO> bookDTOList = new ArrayList<>();
		for (Book book : books) {
			List<BookAuthor> bookAuthorList = bookAuthorRepository.findByBookId(book.getId());
			List<Author> authorList = bookAuthorList.stream().map(a -> a.getAuthor()).collect(Collectors.toList());

			BookDTO bookdto = mapper.map(book, BookDTO.class);
			bookdto.setAuthors(authorList);
			bookDTOList.add(bookdto);
		}

		return bookDTOList;
	}

	@Override
	public List<BookDTO> getBookByAuthor(String authorName) {
		Author author = authorRepository.findByName(authorName);
		if (author == null) {
			return null;
		}
		List<BookDTO> bookDTOList = new ArrayList<>();

		List<BookAuthor> bookAuthorList = bookAuthorRepository.findByAuthorId(author.getId());
		for (BookAuthor bookAuthor : bookAuthorList) {
			List<BookAuthor> bookAuthorForBook = bookAuthorRepository.findByBookId(bookAuthor.getBook().getId());

			List<Author> authors = bookAuthorForBook.stream().map(a -> a.getAuthor()).collect(Collectors.toList());
			BookDTO bookdto = mapper.map(bookAuthor.getBook(), BookDTO.class);
			bookdto.setAuthors(authors);
			bookDTOList.add(bookdto);
		}

		return bookDTOList;
	}

	@Override
	public List<BookDTO> getAll() {
		List<Book> book = bookRepository.findAll();
		return mapBookListDTO(book);
	}

	@Override
	public BookDTO updateBook(Book book, List<Author> authorList) {
		Book dbBook = bookRepository.findByIsbn(book.getIsbn());
		if (dbBook == null) {
			throw new NotFoundException("Book with ISBN " + book.getIsbn() + " not found!");
		}
		dbBook.setPrice(book.getPrice());
		dbBook.setGenre(book.getGenre());
		dbBook.setTitle(book.getTitle());
		dbBook.setYear(book.getYear());
		dbBook.setGenre(book.getGenre());

		Book updatedBook = bookRepository.save(dbBook);
		List<BookAuthor> bookAuthorList = bookAuthorRepository.findByBookId(updatedBook.getId());
		List<Author> updateAuthorList = new ArrayList<>();

		for (BookAuthor bookAuthor : bookAuthorList) {

			if (authorList.stream().filter(a -> a.getName().equals(bookAuthor.getAuthor().getName())).count() == 0) {
				bookAuthorRepository.deleteById(bookAuthor.getId());
			}
		}
		for (Author author : authorList) {
			Author authorDB = authorRepository.findByName(author.getName());
			Author updateAuthor = new Author();
			if (authorDB == null) {
				updateAuthor = authorRepository.save(author);
			} else {
				authorDB.setBirthday(author.getBirthday());
				updateAuthor = authorRepository.save(authorDB);
			}
			bookAuthorRepository.save(new BookAuthor(updatedBook, updateAuthor));

			updateAuthorList.add(updateAuthor);
		}
		BookDTO bookDTO = mapper.map(updatedBook, BookDTO.class);
		bookDTO.setAuthors(updateAuthorList);
		return bookDTO;
	}

	@Override
	public BookDTO addNewBook(Book book, List<Author> authorList) {
		Book dbBook = bookRepository.findByIsbn(book.getIsbn());
		if (dbBook != null) {
			throw new DuplicateResourceException("Book with ISBN" + book.getIsbn() + " already Exists!");
		}
		Book updateBook = bookRepository.save(book);
		List<Author> updateAuthorList = new ArrayList<>();

		for (Author author : authorList) {
			Author authorDB = authorRepository.findByName(author.getName());
			System.out.println(authorDB == null ? "author is null" : "Author existing :" + authorDB.getName());

			Author updateAuthor = authorRepository.save(authorDB == null ? author : authorDB);

			bookAuthorRepository.save(new BookAuthor(updateBook, updateAuthor));
			updateAuthorList.add(updateAuthor);
		}
		BookDTO bookDTO = mapper.map(updateBook, BookDTO.class);
		bookDTO.setAuthors(updateAuthorList);
		return bookDTO;
	}

	@Transactional
	@Override
	public void deleteBook(String isbn) {
		Book book = bookRepository.findByIsbn(isbn);
		if (book == null) {
			throw new NotFoundException("Book with ISBN " + isbn + " not found!");
		}
		bookAuthorRepository.deleteByBookId(book.getId());

		bookRepository.delete(book);
	}

	private List<BookDTO> mapBookListDTO(List<Book> books) {
		return books.stream().map(book -> mapper.map(book, BookDTO.class)).collect(Collectors.toList());
	}

}
