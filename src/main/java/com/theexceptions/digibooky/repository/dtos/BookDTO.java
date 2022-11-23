package com.theexceptions.digibooky.repository.dtos;

public class BookDTO {
    private String isbn;
    private String title;
    private String smallSummary;
    private String authorFirstName;
    private String authorLastName;
    private boolean isLentOut;

    public BookDTO(String isbn, String title, String smallSummary, String authorFirstName, String authorLastName) {
        this.isbn = isbn;
        this.title = title;
        this.smallSummary = smallSummary;
        this.authorFirstName = authorFirstName;
        this.authorLastName = authorLastName;
        isLentOut = false;
    }
}
