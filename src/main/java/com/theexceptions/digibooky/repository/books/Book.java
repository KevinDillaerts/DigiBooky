package com.theexceptions.digibooky.repository.books;

public class Book {
    private final String isbn;
    private final String title;
    private final String smallSummary;
    private final String authorFirstName;
    private final String authorLastName;
    private boolean isLentOut;

    public Book(String isbn, String title, String smallSummary, String authorFirstName, String authorLastName) {
        this.isbn = isbn;
        this.title = title;
        this.smallSummary = smallSummary;
        this.authorFirstName = authorFirstName;
        this.authorLastName = authorLastName;
        isLentOut = false;
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

    public boolean isLentOut() {
        return isLentOut;
    }
}
