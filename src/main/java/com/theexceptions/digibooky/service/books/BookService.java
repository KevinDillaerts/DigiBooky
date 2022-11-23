package com.theexceptions.digibooky.service.books;

import com.theexceptions.digibooky.repository.books.BookRepository;
import org.springframework.stereotype.Service;

@Service
public class BookService {
    private final BookMapper bookMapper;
    private final BookRepository bookRepository;

    public BookService(BookMapper bookMapper, BookRepository bookRepository) {
        this.bookMapper = bookMapper;
        this.bookRepository = bookRepository;
    }
}
