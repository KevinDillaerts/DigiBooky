package com.theexceptions.digibooky.API;

import com.theexceptions.digibooky.repository.dtos.*;
import com.theexceptions.digibooky.repository.users.Role;
import com.theexceptions.digibooky.repository.users.User;
import com.theexceptions.digibooky.service.SecurityService;
import com.theexceptions.digibooky.service.books.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(path = "/books")
public class BookController {
    private final BookService bookservice;
    private final SecurityService securityService;

    public BookController(BookService bookservice, SecurityService securityService) {
        this.bookservice = bookservice;
        this.securityService = securityService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<BookDTO> getAllBooks(@RequestParam(required = false) Map<String, String> params) {
        if (!params.isEmpty()) {
            return bookservice.findBooksBySearchTerms(params);
        }
        return bookservice.findAllBooks();
    }

    @GetMapping(path = "{isbn}", produces = MediaType.APPLICATION_JSON_VALUE)
    public BookDTO getBookByISBN(@PathVariable String isbn, @RequestHeader(required = false) String authorization) {
        if (authorization != null) {
            securityService.validateAuthorization(authorization, Role.MEMBER);
            return bookservice.enhancedFindBookByISBN(isbn);
        }
        return bookservice.findBookByISBN(isbn);
    }

    @PutMapping(path = "{isbn}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public BookDTO updateBookByISBN(@PathVariable String isbn, @RequestBody UpdateBookDTO bookToUpdate) {
        return bookservice.updateBook(isbn, bookToUpdate);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public BookDTO createBook(@RequestBody CreateBookDTO bookToCreate, @RequestHeader String authorization) {
        securityService.validateAuthorization(authorization, Role.LIBRARIAN);
        return bookservice.createBook(bookToCreate);
    }

    @PostMapping(path = "/lend", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public LentBookDTO lendBook(@RequestHeader String authorization, @RequestBody LentBookIdDTO lentBookIdDTO) {
        User user = securityService.validateAuthorization(authorization, Role.MEMBER);
        return bookservice.createLendBook(lentBookIdDTO.id(), user.getId());
    }@ResponseStatus(HttpStatus.OK)


    @PostMapping(path = "/return/", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)

    public String returnBook(@RequestHeader String authorization, @RequestBody LentBookIdDTO bookToReturn) {
        User user = securityService.validateAuthorization(authorization, Role.MEMBER);
        return bookservice.returnLendBook(bookToReturn.id(), user.getId());
    }


    @GetMapping(path = "/lent/{memberId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<LentBookDTO> getListOfLentBooksByMemberId(@RequestHeader String authorization, @PathVariable String memberId) {
        securityService.validateAuthorization(authorization, Role.LIBRARIAN);
        return bookservice.librarianRequestListOfLentBooksPerMember(memberId);
    }

    @GetMapping(path = "/overdues")
    @ResponseStatus(HttpStatus.OK)
    public List<LentBookDTO> getOverDueBooks(@RequestHeader String authorization) {
        securityService.validateAuthorization(authorization, Role.LIBRARIAN);
        return bookservice.getOverdues();
    }

    @DeleteMapping(path = "/{isbn}")
    @ResponseStatus(HttpStatus.OK)
    public void archiveBook(@RequestHeader String authorization, @PathVariable String isbn) {
        securityService.validateAuthorization(authorization, Role.LIBRARIAN);
        bookservice.archiveBook(isbn);
    }

    @PostMapping(path = "/restore", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public BookDTO restoreBook(@RequestHeader String authorization, @RequestBody RestoreBookDTO bookToRestore) {
        securityService.validateAuthorization(authorization, Role.LIBRARIAN);
        return bookservice.restoreBook(bookToRestore.isbn());
    }
}
