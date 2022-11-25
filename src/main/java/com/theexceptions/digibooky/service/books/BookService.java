package com.theexceptions.digibooky.service.books;

import com.theexceptions.digibooky.exceptions.*;
import com.theexceptions.digibooky.repository.books.*;
import com.theexceptions.digibooky.repository.dtos.BookDTO;
import com.theexceptions.digibooky.repository.dtos.CreateBookDTO;
import com.theexceptions.digibooky.repository.dtos.LentBookDTO;
import com.theexceptions.digibooky.repository.dtos.UpdateBookDTO;
import com.theexceptions.digibooky.repository.users.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;


@Service
public class BookService {
    private final BookMapper bookMapper;
    private final BookRepository bookRepository;
    private final LentBookRepository lentBookRepository;
    private final UserRepository userRepository;

    private final BookArchiveRepository bookArchiveRepository;


    public BookService(BookMapper bookMapper, BookRepository bookRepository, LentBookRepository lentBookRepository, BookArchiveRepository bookArchiveRepository, UserRepository userRepository) {
        this.bookMapper = bookMapper;
        this.bookRepository = bookRepository;
        this.lentBookRepository = lentBookRepository;
        this.bookArchiveRepository = bookArchiveRepository;
        this.userRepository = userRepository;
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

    public void createLendBook(String isbn, String userId) {
        Book lentBook = bookRepository.findByISBN(isbn).orElseThrow(() -> new BookNotFoundException("Book not found."));
        lentBook.setBookToLentOutIsTrue();
        LentBook newLentBookEntry = new LentBook(isbn, userId);
        lentBookRepository.addLentBook(newLentBookEntry);
    }

    public String returnLendBook(String lendBookId, String userId) {
        LentBook returnedBook = lentBookRepository.getLentBookByLentBookId(lendBookId);
        if (!returnedBook.getUserId().equals(userId)) {
            throw new UnauthorizedException("This book return is not linked to you.");
        }
        Book book = bookRepository.findByISBN(returnedBook.getIsbn()).stream().findFirst().orElseThrow(() -> new BookNotFoundException("Book is not found."));
        book.setBookToLentOutIsFalse();
        lentBookRepository.deleteReturnedBook(lendBookId);
        if (!LocalDate.now().isAfter(returnedBook.getReturnDate())) {
            return "Thank you for returning your book.";
        }
        return "Your book is overdue.";
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

    public List<LentBookDTO> librarianRequestListOfLentBooksPerMember(String userId) {
        List<LentBookDTO> listRentals = bookMapper.toLentBookDTOList((lentBookRepository.findLentBookByUserId(userId)));
        if (listRentals.isEmpty()) {
            throw new UserNotFoundException("User has no books rented out.");
        } else {
            return listRentals;
        }
    }

    public void archiveBook(String isbn) {
        Book book = bookRepository.findByISBN(isbn).orElseThrow(() -> new BookNotFoundException("Book not found"));
        if (book.isLentOut()) {
            throw new BookAlreadyLentOutException("Book is lent out, can't archive");
        }
        bookRepository.deleteBookByISBN(isbn);
        bookArchiveRepository.addBook(book);
    }

    public BookDTO restoreBook(String isbn) {
        Book book = bookArchiveRepository.findByISBN(isbn).orElseThrow(() -> new BookNotFoundException("Book is not in archive"));
        bookRepository.addBook(book);
        bookArchiveRepository.deleteBookByISBN(isbn);
        return bookMapper.toDTO(book);
    }

    public List<LentBookDTO> getOverdues() {
        return bookMapper.toLentBookDTOList(lentBookRepository.getOverdueLentBooks());
    }

    public BookDTO enhancedFindBookByISBN(String isbn) {
        Book bookToEnhance = bookRepository.findByISBN(isbn)
                .orElseThrow(() -> new BookNotFoundException("Book not found."));
        if (!bookToEnhance.isLentOut()) {
            return bookMapper.toDTO(bookToEnhance);
        } else {
            String userID = lentBookRepository.findLentBookByISBN(isbn).getUserId();
            return bookMapper.toEnhancedBookDTO(bookToEnhance, userRepository.findUserById(userID).getFullName());
        }
    }
}
