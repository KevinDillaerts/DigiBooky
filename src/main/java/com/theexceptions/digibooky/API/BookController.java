package com.theexceptions.digibooky.API;

import com.theexceptions.digibooky.repository.dtos.BookDTO;
import com.theexceptions.digibooky.service.books.BookService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/books")
public class BookController {

    private final BookService bookservice;

    public BookController(BookService bookservice) {
        this.bookservice = bookservice;
    }

    @GetMapping(produces = "application/json")
    public List<BookDTO> getAllBooks(){
        return bookservice.findAllBooks();
    }
}
