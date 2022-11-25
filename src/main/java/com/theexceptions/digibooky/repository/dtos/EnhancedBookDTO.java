package com.theexceptions.digibooky.repository.dtos;

public class EnhancedBookDTO extends BookDTO {
    private final String userName;

    public EnhancedBookDTO(String isbn, String title, String smallSummary, String authorFirstName, String authorLastName, boolean isLentOut, String userName) {
        super(isbn, title, smallSummary, authorFirstName, authorLastName, isLentOut);
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }
}
