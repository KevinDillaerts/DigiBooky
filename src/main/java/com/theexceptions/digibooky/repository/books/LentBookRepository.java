package com.theexceptions.digibooky.repository.books;

import com.theexceptions.digibooky.exceptions.BookNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class LentBookRepository {

    private final List<LentBook> lentBooks;

    public LentBookRepository() {
        this.lentBooks = new ArrayList<>();
    }

    public List<LentBook> getAllLendBooks() {
        return lentBooks;
    }

    public void addLentBook(LentBook lentBookToAdd) {
        lentBooks.add(lentBookToAdd);
    }

    public LentBook getLentBookByLentBookId(String lentBookId) {
        return lentBooks.stream().filter(lentBook -> lentBook.getLentBookId().equals(lentBookId))
                .findFirst().orElseThrow(() -> new BookNotFoundException("Book not found"));
    }

    public void deleteReturnedBook(String lentBookId) {
        lentBooks.removeIf(lentBook -> lentBook.getLentBookId().equals(lentBookId));
    }

}
