package com.theexceptions.digibooky.repository.books;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class BookRepository {
    private final Map<String, Book> books;

    public BookRepository() {
        books = new HashMap<>();
    }
}
