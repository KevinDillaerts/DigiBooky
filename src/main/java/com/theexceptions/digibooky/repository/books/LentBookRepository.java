package com.theexceptions.digibooky.repository.books;

import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class LentBookRepository {

    private List<Book> rentBooks;

    public LentBookRepository(List<Book> rentBooks) {
        this.rentBooks = rentBooks;
    }

    public List<Book> getAllLendBooks(){
        return rentBooks;
    }


}
