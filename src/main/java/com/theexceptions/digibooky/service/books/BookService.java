package com.theexceptions.digibooky.service.books;

import com.theexceptions.digibooky.exceptions.BookNotFoundException;
import com.theexceptions.digibooky.repository.books.Book;
import com.theexceptions.digibooky.repository.books.BookRepository;
import com.theexceptions.digibooky.repository.dtos.BookDTO;
import com.theexceptions.digibooky.repository.dtos.UpdateBookDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    private final BookMapper bookMapper;
    private final BookRepository bookRepository;

    public BookService(BookMapper bookMapper, BookRepository bookRepository) {
        this.bookMapper = bookMapper;
        this.bookRepository = bookRepository;
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

}
