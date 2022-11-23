package com.theexceptions.digibooky.API;

import com.theexceptions.digibooky.exceptions.BookNotFoundException;
import com.theexceptions.digibooky.repository.dtos.BookDTO;
import com.theexceptions.digibooky.service.books.BookService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping(path = "/books")
public class BookController {

    Logger logger = LoggerFactory.getLogger(BookController.class);
    private final BookService bookservice;

    public BookController(BookService bookservice) {
        this.bookservice = bookservice;
    }

    @GetMapping(produces = "application/json")
    public List<BookDTO> getAllBooks() {
        return bookservice.findAllBooks();
    }

    @GetMapping(path = "{isbn}", produces = "application/json")
    public BookDTO getBookByISBN(@PathVariable String isbn) {
        return bookservice.findBookByISBN(isbn);
    }

    @ExceptionHandler(BookNotFoundException.class)
    protected void bookNotFoundException(BookNotFoundException ex, HttpServletResponse respons) throws IOException {
        logger.info("Book not found.");
        respons.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
    }

}
