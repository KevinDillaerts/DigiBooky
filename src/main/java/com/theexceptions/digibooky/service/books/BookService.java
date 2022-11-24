package com.theexceptions.digibooky.service.books;

import com.theexceptions.digibooky.exceptions.BookNotFoundException;
import com.theexceptions.digibooky.repository.books.Book;
import com.theexceptions.digibooky.repository.books.BookRepository;
import com.theexceptions.digibooky.repository.books.LentBookRepository;
import com.theexceptions.digibooky.repository.dtos.BookDTO;
import com.theexceptions.digibooky.repository.dtos.UpdateBookDTO;
import com.theexceptions.digibooky.repository.users.User;
import com.theexceptions.digibooky.repository.users.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    private final BookMapper bookMapper;
    private final BookRepository bookRepository;
    private final LentBookRepository lentBookRepository;
    private final UserRepository userRepository;

    public BookService(BookMapper bookMapper, BookRepository bookRepository, LentBookRepository lentBookRepository, UserRepository userRepository) {
        this.bookMapper = bookMapper;
        this.bookRepository = bookRepository;
        this.lentBookRepository = lentBookRepository;
        this.userRepository = userRepository;
    }

    public List<BookDTO> findAllBooks(){
        return bookMapper.toDTO(bookRepository.findAllBooks());
    }

    public BookDTO findBookByISBN(String isbn){
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

    public void createLendBook(String isbn, String id){
        Book lentBook = bookRepository.findByISBN(isbn).orElseThrow(() -> new BookNotFoundException("Book not found."));
        lentBook.setBookToLentOutIsTrue();
        //new lent book
    }
}
