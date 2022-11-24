package com.theexceptions.digibooky.repository.books;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class BookRepository {
    private final Map<String, Book> books;

    public BookRepository() {
        books = new HashMap<>();
    }


    public Optional<Book> findByISBN(String isbn) {
        return Optional.ofNullable(books.get(isbn));
    }

    public List<Book> findAllBooks() {
        return books.values().stream().toList();
    }

    // In functie van test
    public void addBook(Book book) {
        books.put(book.getIsbn(), book);
    }


}
