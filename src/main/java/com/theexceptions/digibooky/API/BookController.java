package com.theexceptions.digibooky.API;

import com.theexceptions.digibooky.exceptions.BookAlreadyExistsException;
import com.theexceptions.digibooky.exceptions.BookNotFoundException;
import com.theexceptions.digibooky.repository.dtos.BookDTO;
import com.theexceptions.digibooky.repository.dtos.CreateBookDTO;
import com.theexceptions.digibooky.repository.dtos.UpdateBookDTO;
import com.theexceptions.digibooky.repository.users.Role;
import com.theexceptions.digibooky.repository.users.User;
import com.theexceptions.digibooky.service.SecurityService;
import com.theexceptions.digibooky.service.books.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping(path = "/books")
public class BookController {

    Logger logger = LoggerFactory.getLogger(BookController.class);
    private final BookService bookservice;
    private final SecurityService securityService;

    public BookController(BookService bookservice, SecurityService securityService) {
        this.bookservice = bookservice;
        this.securityService = securityService;
    }

    @GetMapping(produces = "application/json")
    public List<BookDTO> getAllBooks() {
        return bookservice.findAllBooks();
    }

    @GetMapping(path = "{isbn}", produces = "application/json")
    public BookDTO getBookByISBN(@PathVariable String isbn) {
        return bookservice.findBookByISBN(isbn);
    }

    @PutMapping(path = "{isbn}", consumes = "application/json", produces = "application/json")
    public BookDTO updateBookByISBN(@PathVariable String isbn, @RequestBody UpdateBookDTO bookToUpdate) {
        return bookservice.updateBook(isbn, bookToUpdate);
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public BookDTO createBook(@RequestBody CreateBookDTO bookToCreate, @RequestHeader String authorization) {
        securityService.validateAuthorization(authorization, Role.LIBRARIAN);
        return bookservice.createBook(bookToCreate);
    }
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/lend", consumes = "application/json")
    public void lendBook(@RequestHeader String authorization, @RequestBody String isbn) {
        User user = securityService.validateAuthorization(authorization, Role.MEMBER);
        bookservice.createLendBook(isbn, user.getId());
    }

    @ExceptionHandler(BookNotFoundException.class)
    protected void bookNotFoundException(BookNotFoundException ex, HttpServletResponse response) throws IOException {
        logger.info("Book not found.");
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(BookAlreadyExistsException.class)
    protected void bookAlreadyExists (BookAlreadyExistsException ex, HttpServletResponse response) throws IOException {
        logger.info("Book already exists.");
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
    }


}
