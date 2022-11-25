package com.theexceptions.digibooky.repository.dtos;

public class EnhancedBookDTO extends BookDTO{
    private final String userID;

    public EnhancedBookDTO(String isbn, String title, String smallSummary, String authorFirstName, String authorLastName, boolean isLentOut, String userID) {
        super(isbn, title, smallSummary, authorFirstName, authorLastName, isLentOut);
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }
}
