package com.theexceptions.digibooky.service.books;

import com.theexceptions.digibooky.exceptions.BookAlreadyExistsException;
import com.theexceptions.digibooky.exceptions.BookNotFoundException;
import com.theexceptions.digibooky.exceptions.InvalidFilterValueException;
import com.theexceptions.digibooky.repository.books.Book;
import com.theexceptions.digibooky.repository.books.BookRepository;
import com.theexceptions.digibooky.repository.books.LentBookRepository;
import com.theexceptions.digibooky.repository.dtos.BookDTO;
import com.theexceptions.digibooky.repository.dtos.CreateBookDTO;
import com.theexceptions.digibooky.repository.dtos.UpdateBookDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;


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

    public List<BookDTO> findBooksBySearchTerms(Map<String, String> params) {
        List<Book> books = bookRepository.findAllBooks();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            params.put(entry.getKey(), entry.getValue().toLowerCase());
            books = books.stream().filter(getFilterPredicate(entry)).toList();
        }
        return bookMapper.toDTO(books);
    }

    public Predicate<Book> getFilterPredicate(Map.Entry<String, String> entry) {
        return switch (entry.getKey()) {
            case "isbn" -> book -> book.getIsbn().contains(entry.getValue());
            case "title" -> book -> book.getTitle().toLowerCase().contains(entry.getValue());
            case "authorFirstName" -> book -> book.getAuthorFirstName().toLowerCase().contains(entry.getValue());
            case "authorLastName" -> book -> book.getAuthorLastName().toLowerCase().contains(entry.getValue());
            default -> throw new InvalidFilterValueException("The provided filter is not valid");
        };
    }
}
