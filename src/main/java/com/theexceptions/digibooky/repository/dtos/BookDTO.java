package com.theexceptions.digibooky.repository.dtos;

import java.util.Objects;

public class BookDTO {
    private String isbn;
    private String title;
    private String smallSummary;
    private String authorFirstName;
    private String authorLastName;
    private boolean isLentOut;

    public BookDTO(String isbn, String title, String smallSummary, String authorFirstName, String authorLastName, boolean isLentOut) {
        this.isbn = isbn;
        this.title = title;
        this.smallSummary = smallSummary;
        this.authorFirstName = authorFirstName;
        this.authorLastName = authorLastName;
        this.isLentOut = isLentOut;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getSmallSummary() {
        return smallSummary;
    }

    public String getAuthorFirstName() {
        return authorFirstName;
    }

    public String getAuthorLastName() {
        return authorLastName;
    }

    public boolean getIsLentOut() {
        return isLentOut;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookDTO bookDTO = (BookDTO) o;
        return isLentOut == bookDTO.isLentOut && isbn.equals(bookDTO.isbn) && title.equals(bookDTO.title) && smallSummary.equals(bookDTO.smallSummary) && authorFirstName.equals(bookDTO.authorFirstName) && authorLastName.equals(bookDTO.authorLastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn, title, smallSummary, authorFirstName, authorLastName, isLentOut);
    }
}
