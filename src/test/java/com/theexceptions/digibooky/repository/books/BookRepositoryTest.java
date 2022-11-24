package com.theexceptions.digibooky.repository.books;

import com.theexceptions.digibooky.repository.dtos.UpdateBookDTO;
import com.theexceptions.digibooky.repository.users.UserRepository;
import com.theexceptions.digibooky.service.books.BookMapper;
import com.theexceptions.digibooky.service.books.BookService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

class BookRepositoryTest {
    @Autowired
    private BookRepository testBookRepository;
    private BookService testBookService;

    @Autowired
    private LentBookRepository lentBookRepository;
    private Book book1;
    private Book book2;
    private BookMapper mapper;

    @BeforeEach
    void setupBookRepository() {
        book1 = new Book("1", "The DiscWorld",
                "All about wizzzzzards!", "Terry", "Pratchett");
        book2 = new Book("2", "Good Omens",
                "All about gods.", "Neill", "Gaimon");
        mapper = new BookMapper();
        testBookRepository = new BookRepository();
        testBookService = new BookService(mapper, testBookRepository, lentBookRepository);
        testBookRepository.addBook(book1);
        testBookRepository.addBook(book2);
    }

    @Test
    void whenHavingARepositoryOfBooks_whenGettingALlBooks_returnListOfBooks() {
        //        given

        List<Book> expectedList = List.of(book1, book2);

        Assertions.assertEquals(expectedList, testBookRepository.findAllBooks());
    }

    @Test
    void whenSearchingBookByISBN_returnCorrectBook(){
        Book expectedBook = book1;

        Assertions.assertEquals(expectedBook, testBookRepository.findByISBN("1").get());
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

}