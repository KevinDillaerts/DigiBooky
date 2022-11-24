package com.theexceptions.digibooky.repository.dtos;

import java.util.Objects;

public class UpdateBookDTO {
    private String title;
    private String smallSummary;
    private String authorFirstName;
    private String authorLastName;


    public UpdateBookDTO(String title, String smallSummary, String authorFirstName, String authorLastName) {
        this.title = title;
        this.smallSummary = smallSummary;
        this.authorFirstName = authorFirstName;
        this.authorLastName = authorLastName;
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



}
