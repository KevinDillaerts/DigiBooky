//package com.theexceptions.digibooky.repository.books;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class BookRepositoryTest {
//
//    private Map<String, Book> books;
//
//
//    @BeforeEach
//    void setupBookRepository(){
//        BookRepository testBookRepository = new BookRepository();
//        Book book1 = new Book("1", "The DiscWorld",
//                "All about wizzzzzards!", "Terry", "Pratchett");
//        Book book2 = new Book("2", "Good Omens",
//                "All about gods.", "Neill", "Gaimon");
//
//        books = new HashMap<>();
//        books.put(book1.getIsbn(), book1);
//        books.put(book2.getIsbn(), book2);
//    }
//
//    @Test
//    void whenHavingARepositoryOfBooks_whenGettingALlBooks_returnListOfBooks(){
//        BookRepository bookRepository = new BookRepository();
//        BookRepository testBookRepository = new BookRepository();
//        testBookRepository.findAllBooks();
//
//    }
//
//}