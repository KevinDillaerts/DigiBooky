package com.theexceptions.digibooky.repository.books;

import java.time.LocalDate;
import java.util.UUID;

public class LentBook {
    private final String lentBookId;
    private final String isbn;
    private final String userId;
    private final LocalDate returnDate;

    public LentBook( String isbn, String userId) {
        this.lentBookId = UUID.randomUUID().toString();
        this.isbn = isbn;
        this.userId = userId;
        this.returnDate = LocalDate.now().plusWeeks(3);
    }

    public String getLentBookId() {
        return lentBookId;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getUserId() {
        return userId;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public boolean isOverdue() {
        return LocalDate.now().isAfter(returnDate);
    }
}
