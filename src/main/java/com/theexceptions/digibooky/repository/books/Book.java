package com.theexceptions.digibooky.repository.books;

import com.theexceptions.digibooky.exceptions.BookAlreadyLentOutException;
import com.theexceptions.digibooky.exceptions.FieldIsEmptyException;

import javax.validation.constraints.NotNull;

public class Book {
    private final String isbn;

    private String title;
    private String smallSummary;
    private String authorFirstName;
    private String authorLastName;
    private boolean isLentOut;

    public Book(String isbn, String title, String smallSummary, String authorFirstName, String authorLastName) {
        this.isbn = validateField(isbn);
        this.title = validateField(title);
        this.smallSummary = smallSummary;
        this.authorFirstName = authorFirstName;
        this.authorLastName = validateField(authorLastName);
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

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSmallSummary(String smallSummary) {
        this.smallSummary = smallSummary;
    }

    public void setAuthorFirstName(String authorFirstName) {
        this.authorFirstName = authorFirstName;
    }

    public void setAuthorLastName(String authorLastName) {
        this.authorLastName = authorLastName;
    }

    public void setBookToLentOutIsTrue(){
        if(isLentOut){
            throw new BookAlreadyLentOutException("Book already lent out.");
        }
        isLentOut = true;
    }

    public String validateField(String field) {
       if (field == null || field.isEmpty()) {
           throw new FieldIsEmptyException("This field is required");
       }
       return field;
    }


}
