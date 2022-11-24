package com.theexceptions.digibooky.API;

import com.theexceptions.digibooky.exceptions.UnauthorizatedException;
import com.theexceptions.digibooky.repository.dtos.BookDTO;
import com.theexceptions.digibooky.repository.dtos.CreateBookDTO;
import com.theexceptions.digibooky.repository.dtos.UpdateBookDTO;
import com.theexceptions.digibooky.repository.users.Role;
import com.theexceptions.digibooky.service.SecurityService;
import com.theexceptions.digibooky.service.books.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;


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

//    params = {"isbn", "title", "authorFirstName", "authorLastName"}

    @GetMapping(produces = "application/json")
    public List<BookDTO> getAllBooks(@RequestParam(required = false) Map<String, String> params) {
        if (!params.isEmpty()) {
            return bookservice.findBooksBySearchTerms(params);
        }
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

    @ExceptionHandler(UnauthorizatedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    protected void unAuthorizedException(UnauthorizatedException ex, HttpServletResponse response) throws IOException {
        logger.info(ex.getMessage());
        response.sendError(HttpServletResponse.SC_FORBIDDEN, ex.getMessage());
    }
}
