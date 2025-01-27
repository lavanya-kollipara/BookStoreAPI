package com.bookstore.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;
import com.bookstore.exception.BadRequestException;

import com.bookstore.dto.BookDTO;
import com.bookstore.model.Author;
import com.bookstore.model.Book;
import com.bookstore.service.BookService;

@EnableMethodSecurity
@RestController
@Validated
@RequestMapping("/bookstore")
@PreAuthorize("hasRole('USER')")
public class BookController {

	@Autowired
	private BookService bookService;

	@Autowired
	private ModelMapper mapper;

	@PostMapping("/addBook")
	public ResponseEntity<BookDTO> addNewBook(@Valid @RequestBody BookDTO bookDTO) {
		return new ResponseEntity<>(
				bookService.addNewBook(mapper.map(bookDTO, Book.class), mapAuthorList(bookDTO.getAuthors())),
				HttpStatus.CREATED);
	}

	@PutMapping("/updateBook")
	public ResponseEntity<BookDTO> updateBook(@Valid @RequestBody BookDTO bookDTO) {
		return new ResponseEntity<>(
				bookService.updateBook(mapper.map(bookDTO, Book.class), mapAuthorList(bookDTO.getAuthors())),
				HttpStatus.OK);

	}

	@GetMapping("/getBookBy")
	public ResponseEntity<List<BookDTO>> getBookBy(@RequestParam(value = "title", required = false) String title,
			@RequestParam(value = "author", required = false) String author) {
		if (title == null && author == null) {
			throw new BadRequestException("Insufficient Paramameters for the API ");
		}
		List<BookDTO> books = bookService.getBookByTitleAndOrAuthor(title, author);
		return new ResponseEntity<>(books, HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("delete/{isbn}")
	public ResponseEntity<Boolean> deleteBook(@PathVariable(name = "isbn") String isbn) {
		bookService.deleteBook(isbn);
		return new ResponseEntity<>(true, HttpStatus.OK);

	}

	private List<Author> mapAuthorList(List<Author> authors) {
		return authors.stream().map(author -> mapper.map(author, Author.class)).collect(Collectors.toList());
	}
}
