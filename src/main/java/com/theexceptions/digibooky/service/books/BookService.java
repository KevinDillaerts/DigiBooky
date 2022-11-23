package com.theexceptions.digibooky.service.books;

import com.theexceptions.digibooky.repository.books.BookRepository;
import com.theexceptions.digibooky.repository.dtos.BookDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    private final BookMapper bookMapper;
    private final BookRepository bookRepository;

    public BookService(BookMapper bookMapper, BookRepository bookRepository) {
        this.bookMapper = bookMapper;
        this.bookRepository = bookRepository;
    }

    public List<BookDTO> findAllBooks(){
        return bookMapper.toDTO(bookRepository.findAllBooks());
    }

}
