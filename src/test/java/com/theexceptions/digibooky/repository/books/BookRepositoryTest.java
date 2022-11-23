package com.theexceptions.digibooky.repository.books;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BookRepositoryTest {
    @Autowired
    private BookRepository testBookRepository;
    private Book book1;
    private Book book2;

    @BeforeEach
    void setupBookRepository() {
        book1 = new Book("1", "The DiscWorld",
                "All about wizzzzzards!", "Terry", "Pratchett");
        book2 = new Book("2", "Good Omens",
                "All about gods.", "Neill", "Gaimon");

        testBookRepository = new BookRepository();
        testBookRepository.addBook(book1);
        testBookRepository.addBook(book2);
    }

    @Test
    void whenHavingARepositoryOfBooks_whenGettingALlBooks_returnListOfBooks() {
        //        given

        List<Book> expectedList = List.of(book1, book2);
//        BookRepository testBookRepository = new BookRepository;

        Assertions.assertEquals(expectedList, testBookRepository.findAllBooks());
    }

}