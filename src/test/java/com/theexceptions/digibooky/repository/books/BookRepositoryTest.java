package com.theexceptions.digibooky.repository.books;

import com.theexceptions.digibooky.exceptions.BookAlreadyExistsException;
import com.theexceptions.digibooky.exceptions.BookAlreadyLentOutException;
import com.theexceptions.digibooky.exceptions.InvalidFilterValueException;
import com.theexceptions.digibooky.repository.dtos.BookDTO;
import com.theexceptions.digibooky.repository.dtos.CreateBookDTO;
import com.theexceptions.digibooky.repository.dtos.UpdateBookDTO;
import com.theexceptions.digibooky.service.books.BookMapper;
import com.theexceptions.digibooky.service.books.BookService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class BookRepositoryTest {
    private BookRepository testBookRepository;
    private BookService testBookService;
    private LentBookRepository lentBookRepository;
    private Book book1;
    private Book book2;
    private BookMapper mapper;

    @BeforeEach
    void setupBookRepository() {
        book1 = new Book("123456", "The DiscWorld",
                "All about wizzzzzards!", "Terry", "Pratchett");
        book2 = new Book("289456", "Good Omens",
                "All about gods.", "Neill", "Gaimon");
        mapper = new BookMapper();
        testBookRepository = new BookRepository();
        lentBookRepository = new LentBookRepository();
        testBookService = new BookService(mapper, testBookRepository, lentBookRepository);
        testBookRepository.addBook(book1);
        testBookRepository.addBook(book2);
    }

    @Test
    void whenHavingARepositoryOfBooks_whenGettingALlBooks_returnListOfBooks() {
        List<Book> expectedList = List.of(book1, book2);

        Assertions.assertTrue(testBookRepository.findAllBooks().containsAll(expectedList));
    }

    @Test
    void whenSearchingBookByISBN_returnCorrectBook() {
        Book expectedBook = book1;

        Assertions.assertEquals(expectedBook, testBookRepository.findByISBN("123456").get());
    }

    @Test
    void whenUpdatingBookByISBN_bookIsCorrectlyUpdated() {

        UpdateBookDTO bookDTO = new UpdateBookDTO("title1", "blablabla", "Jef", "Jefferson");
        Book expectedBook = book1;
        expectedBook.setTitle("title1");
        expectedBook.setSmallSummary("blablabla");
        expectedBook.setAuthorFirstName("Jef");
        expectedBook.setAuthorLastName("Jefferson");

        Assertions.assertEquals(mapper.toDTO(expectedBook), testBookService.updateBook(book1.getIsbn(), bookDTO));
    }

    @Test
    void givenANewBook_whenISBNAlreadyExists_throwBookAlreadyExistsException() {
        CreateBookDTO bookDTO = new CreateBookDTO("123456", "The DiscWorld",
                "All about wizzzzzards!", "Terry", "Pratchett");
        Assertions.assertThrows(BookAlreadyExistsException.class, () -> testBookService.createBook(bookDTO));
    }

    @Test
    void givenAListOfBooks_whenSearchingByISBN_thenReturnCorrectBooks() {
        List<BookDTO> expectedList = new ArrayList<>();
        expectedList.add(mapper.toDTO(book1));
        expectedList.add(mapper.toDTO(book2));

        List<BookDTO> foundBooks = testBookService.findBooksBySearchTerms(new HashMap<>(Map.of("isbn", "456")));

        Assertions.assertTrue(foundBooks.containsAll(expectedList));
    }

    @Test
    void givenAListOfBooks_whenSearchingByISBNAgain_thenReturnCorrectBook() {
        List<BookDTO> expectedList = new ArrayList<>();
        expectedList.add(mapper.toDTO(book1));

        List<BookDTO> foundBooks = testBookService.findBooksBySearchTerms(new HashMap<>(Map.of("isbn", "12")));

        Assertions.assertTrue(foundBooks.containsAll(expectedList));
    }

    @Test
    void givenAListOfBooks_whenSearchingByTitle_thenReturnCorrectBook() {
        List<BookDTO> expectedList = new ArrayList<>();
        expectedList.add(mapper.toDTO(book1));

        List<BookDTO> foundBooks = testBookService.findBooksBySearchTerms(new HashMap<>(Map.of("title", "Disc")));

        Assertions.assertTrue(foundBooks.containsAll(expectedList));
    }

    @Test
    void givenAListOfBooks_whenSearchingByAuthorFirstName_thenReturnCorrectBook() {
        List<BookDTO> expectedList = new ArrayList<>();
        expectedList.add(mapper.toDTO(book1));

        List<BookDTO> foundBooks = testBookService.findBooksBySearchTerms(new HashMap<>(Map.of("authorFirstName", "err")));

        Assertions.assertTrue(foundBooks.containsAll(expectedList));
    }

    @Test
    void givenAListOfBooks_whenSearchingByAuthorFirstNameAndLastName_thenReturnCorrectBook() {
        List<BookDTO> expectedList = new ArrayList<>();
        expectedList.add(mapper.toDTO(book1));

        List<BookDTO> foundBooks = testBookService.findBooksBySearchTerms(new HashMap<>(Map.of("authorFirstName", "err", "authorLastName", "ett")));

        Assertions.assertTrue(foundBooks.containsAll(expectedList));
    }

    @Test
    void givenAListOfBooks_whenSearchingByAuthorLastName_thenReturnCorrectBooks() {
        List<BookDTO> expectedList = new ArrayList<>();
        expectedList.add(mapper.toDTO(book1));
        expectedList.add(mapper.toDTO(book2));

        List<BookDTO> foundBooks = testBookService.findBooksBySearchTerms(new HashMap<>(Map.of("authorLastName", "A")));

        Assertions.assertTrue(foundBooks.containsAll(expectedList));
    }

    @Test
    void givenAListOfBooks_whenSearchingByWrongParam_thenReturnCorrectBooks() {
        Assertions.assertThrows(InvalidFilterValueException.class, () -> testBookService.findBooksBySearchTerms(new HashMap<>(Map.of("test", "456"))));
    }

    @Test
    void givenBookToLend_whenBookNotLentOut_thenBookIsInList() {
        book1 = new Book("123456", "The DiscWorld",
                "All about wizzzzzards!", "Terry", "Pratchett");
        testBookRepository.addBook(book1);
        testBookService.createLendBook("123456", "489464");

        Assertions.assertTrue(lentBookRepository.getAllLendBooks().stream().anyMatch(lentBook -> lentBook.getIsbn().equals(book1.getIsbn())));
    }

    @Test
    void givenBookToLend_whenBookAlreadyLentOut_thenExceptionIsThrown() {
        book1 = new Book("123456", "The DiscWorld",
                "All about wizzzzzards!", "Terry", "Pratchett");
        testBookRepository.addBook(book1);
        testBookService.createLendBook("123456", "489464");

        Assertions.assertThrows(BookAlreadyLentOutException.class, () -> testBookService.createLendBook("123456", "4846464"));
    }

    @Test
    void givenLendedBook_whenReturning_thenExceptionIsThrown() {
        book1 = new Book("123456", "The DiscWorld",
                "All about wizzzzzards!", "Terry", "Pratchett");
        testBookRepository.addBook(book1);
        testBookService.createLendBook("123456", "489464");

        Assertions.assertThrows(BookAlreadyLentOutException.class, () -> testBookService.createLendBook("123456", "4846464"));
    }


}