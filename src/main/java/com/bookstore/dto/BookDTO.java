package com.bookstore.dto;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.*;
import com.bookstore.model.Author;

public class BookDTO {

	@NotBlank(message = "ISDN cannot be blank")
	private String isbn;
	
	@NotBlank(message = "title cannot be blank")
	private String title;
	
	@NotNull(message = "Authors cannot be blank")
	private List<Author> authors;
	
	private Integer year;
	private BigDecimal price;
	private String genre;
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<Author> getAuthors() {
		return authors;
	}
	public void setAuthors(List<Author> authors) {
		this.authors = authors;
	}
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
	
}
