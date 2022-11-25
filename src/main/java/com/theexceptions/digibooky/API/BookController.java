package com.theexceptions.digibooky.API;

import com.theexceptions.digibooky.exceptions.BookNotFoundException;
import com.theexceptions.digibooky.exceptions.UnauthorizedException;
import com.theexceptions.digibooky.exceptions.UserNotFoundException;
import com.theexceptions.digibooky.repository.dtos.*;
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

    @GetMapping(produces = "application/json")
    public List<BookDTO> getAllBooks(@RequestParam(required = false) Map<String, String> params) {
        if (!params.isEmpty()) {
            return bookservice.findBooksBySearchTerms(params);
        }
        return bookservice.findAllBooks();
    }

    @GetMapping(path = "{isbn}", produces = "application/json")
    public BookDTO getBookByISBN(@PathVariable String isbn,@RequestHeader (required = false) String authorization) {
        if(authorization != null){
            securityService.validateAuthorization(authorization, Role.MEMBER);
            return bookservice.enhancedFindBookByISBN(isbn);
        }
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

    @PostMapping(path = "/lend", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public void lendBook(@RequestHeader String authorization, @RequestBody LentBookIdDTO lentBookIdDTO) {
        User user = securityService.validateAuthorization(authorization, Role.MEMBER);
        bookservice.createLendBook(lentBookIdDTO.id(), user.getId());
    }


    @GetMapping(path = "/return/{lentBookId}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public String returnBook(@RequestHeader String authorization, @PathVariable String lentBookId) {
        User user = securityService.validateAuthorization(authorization, Role.MEMBER);
        return bookservice.returnLendBook(lentBookId, user.getId());
    }


    @GetMapping(path = "/lent/{memberId}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public List<LentBookDTO> getListOfLentBooksByMemberId(@RequestHeader String authorization, @PathVariable String memberId) {
        securityService.validateAuthorization(authorization, Role.LIBRARIAN);
        return bookservice.librarianRequestListOfLentBooksPerMember(memberId);
    }


    @ExceptionHandler({BookNotFoundException.class, UserNotFoundException.class})
    protected void bookNotFoundException(RuntimeException ex, HttpServletResponse response) throws IOException {
        logger.warn(ex.getMessage());
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    protected void UnauthorizedException(UnauthorizedException ex, HttpServletResponse response) throws IOException {
        logger.warn(ex.getMessage());
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
    }
}
