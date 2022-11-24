package com.theexceptions.digibooky.repository.books;

import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class LentBookRepository {

    private List<LentBook> lentBooks;

    public LentBookRepository(List<LentBook> rentBooks) {
        this.lentBooks = lentBooks;
    }

    public List<LentBook> getAllLendBooks(){
        return lentBooks;
    }
    public void addLentBook(LentBook lentBookToAdd){
        lentBooks.add(lentBookToAdd);
    }


}
