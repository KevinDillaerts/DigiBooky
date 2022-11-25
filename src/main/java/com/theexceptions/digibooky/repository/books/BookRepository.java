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
        books.put("10-10", new Book("10-10", "Hello 1", "Hello Im book 1", "Hel", "Lo"));
        books.put("10-20", new Book("10-20", "Bye bYe", "I say good bye", "good", "bye"));
        books.put("10-30", new Book("10-30", "Book 3", "Book 3", "Book", "3"));

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

    public void deleteBookByISBN(String isbn) {
        books.remove(isbn);
    }


}
