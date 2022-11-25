package com.theexceptions.digibooky.repository.books;

import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class BookArchiveRepository {
    private final Map<String, Book> books = new HashMap<>();



    public List<Book> findAllBooks() {
        return new ArrayList<>(books.values());
    }
    public Optional<Book> findByISBN(String isbn) {
        return Optional.ofNullable(books.get(isbn));
    }

    public void addBook(Book book) {
        books.put(book.getIsbn(), book);
    }

    public void deleteBookByISBN(String isbn) {
        books.remove(isbn);
    }






}
