package com.theexceptions.digibooky.repository.dtos;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class LentBookDTO {
    private final String lentBookId;
    private final String isbn;
    private final String userId;
    private final LocalDate returnDate;

    public LentBookDTO(String isbn, String userId) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LentBookDTO that = (LentBookDTO) o;
        return Objects.equals(lentBookId, that.lentBookId) && Objects.equals(isbn, that.isbn) && Objects.equals(userId, that.userId) && Objects.equals(returnDate, that.returnDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lentBookId, isbn, userId, returnDate);
    }
}
