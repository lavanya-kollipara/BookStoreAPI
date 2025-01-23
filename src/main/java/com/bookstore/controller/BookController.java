package com.bookstore.controller;



import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.bookstore.service.BookService;


import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.bookstore.model.Author;
import com.bookstore.model.Book;
import org.springframework.web.bind.annotation.*;
import com.bookstore.dto.BookDTO;
import org.springframework.http.HttpStatus;

@RestController
@Validated
@RequestMapping("/BookStore")
public class BookController {

	@Autowired 
	private BookService bookService;
	
	@Autowired
	private ModelMapper mapper;
	
	@PostMapping("/addBook")
	public  ResponseEntity<BookDTO> addNewBook( @Valid @RequestBody BookDTO bookDTO){
		return  new ResponseEntity<>(bookService.addNewBook(mapper.map(bookDTO, Book.class)
				,mapAuthorList (bookDTO.getAuthors())),HttpStatus.CREATED);
	}
	
	@PutMapping("/updateBook")
	public ResponseEntity<BookDTO> updateBook(@Valid @RequestBody BookDTO bookDTO) {
		return  new ResponseEntity<>(bookService.updateBook(mapper.map(bookDTO, Book.class)
				,mapAuthorList (bookDTO.getAuthors())),HttpStatus.OK);

	}
	
	 @GetMapping("/getBook")
	    public ResponseEntity<List<BookDTO> > getBook() {
	    	List<BookDTO> books =  bookService.getAll();
	        return new ResponseEntity<>(books,HttpStatus.OK);
	    }

	
    @GetMapping("/getBookByTitle/{title}")
    public ResponseEntity<List<BookDTO> > getBookByTitle(@PathVariable(name="title")  String title) {
    	List<BookDTO> books =  bookService.getBookByTitle(title);
        return new ResponseEntity<>(books,HttpStatus.OK);
    }
    
    @GetMapping("/getBookByAuthor/{author}")
    public ResponseEntity<List<BookDTO>> getBookByAuthor(@PathVariable(name="author")  String author) {
    	List<BookDTO> books =  bookService.getBookByAuthor(author);
    	return new ResponseEntity<>(books,HttpStatus.OK);
    }
    
    
  	@DeleteMapping("delete/{isbn}")
  	public ResponseEntity<Boolean> deleteBook(@PathVariable(name="isbn") String isbn)
  	{
  		bookService.deleteBook(isbn);
  		return new ResponseEntity<>(true,HttpStatus.OK);
  		
  	}

    
	private List<Author> mapAuthorList(List<Author> authors) {
        return authors.stream().map(author -> mapper.map(author, Author.class))
                .collect(Collectors.toList());
	}
}
