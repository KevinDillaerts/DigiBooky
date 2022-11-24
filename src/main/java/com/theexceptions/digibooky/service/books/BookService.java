package com.theexceptions.digibooky.service.books;

import com.theexceptions.digibooky.exceptions.BookAlreadyExistsException;
import com.theexceptions.digibooky.exceptions.BookNotFoundException;
import com.theexceptions.digibooky.repository.books.*;
import com.theexceptions.digibooky.repository.dtos.BookDTO;
import com.theexceptions.digibooky.repository.dtos.CreateBookDTO;
import com.theexceptions.digibooky.repository.dtos.UpdateBookDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    private final BookMapper bookMapper;
    private final BookRepository bookRepository;
    private final LentBookRepository lentBookRepository;


    public BookService(BookMapper bookMapper, BookRepository bookRepository, LentBookRepository lentBookRepository) {
        this.bookMapper = bookMapper;
        this.bookRepository = bookRepository;
        this.lentBookRepository = lentBookRepository;


    }

    public List<BookDTO> findAllBooks() {
        return bookMapper.toDTO(bookRepository.findAllBooks());
    }

    public BookDTO findBookByISBN(String isbn) {
        return bookMapper.toDTO(bookRepository.findByISBN(isbn)
                .orElseThrow(() -> new BookNotFoundException("Book not found.")));
    }

    public BookDTO updateBook(String isbn, UpdateBookDTO bookToUpdate) {
        Book book = bookRepository.findByISBN(isbn).orElseThrow(() -> new BookNotFoundException("Book not found"));
        book.setTitle(bookToUpdate.getTitle());
        book.setAuthorFirstName(bookToUpdate.getAuthorFirstName());
        book.setAuthorLastName(bookToUpdate.getAuthorLastName());
        book.setSmallSummary(bookToUpdate.getSmallSummary());
        return bookMapper.toDTO(book);
    }

    public BookDTO createBook(CreateBookDTO bookToCreate) {
        if (bookRepository.findAllBooks().stream().anyMatch(book -> book.getIsbn().equals(bookToCreate.getIsbn()))) {
            throw new BookAlreadyExistsException("Book already exists");
        }
        Book bookToAdd = new Book(bookToCreate.getIsbn(), bookToCreate.getTitle(), bookToCreate.getSmallSummary(), bookToCreate.getAuthorFirstName(), bookToCreate.getAuthorLastName());
        bookRepository.addBook(bookToAdd);
        return bookMapper.toDTO(bookToAdd);
    }

    public void createLendBook(String isbn, String id) {
        Book lentBook = bookRepository.findByISBN(isbn).orElseThrow(() -> new BookNotFoundException("Book not found."));
    public void createLendBook(String lendBookId, String userId){
        Book lentBook = bookRepository.findByISBN(lendBookId).orElseThrow(() -> new BookNotFoundException("Book not found."));
        lentBook.setBookToLentOutIsTrue();
        LentBook newLentBookEntry = new LentBook(lendBookId, userId);
        lentBookRepository.addLentBook(newLentBookEntry);
    }

    public List<BookDTO> findBooksBySearchTerm(String isbn) {
        return findAllBooks().stream().filter(book -> book.getIsbn().contains(isbn)).toList();
    }
}
