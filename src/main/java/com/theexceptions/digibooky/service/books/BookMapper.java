package com.theexceptions.digibooky.service.books;

import com.theexceptions.digibooky.repository.books.Book;
import com.theexceptions.digibooky.repository.books.LentBook;
import com.theexceptions.digibooky.repository.dtos.BookDTO;
import com.theexceptions.digibooky.repository.dtos.LentBookDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class BookMapper {
    public BookDTO toDTO(Book book) {
        return new BookDTO(book.getIsbn(), book.getTitle(), book.getSmallSummary(), book.getAuthorFirstName(),
                book.getAuthorLastName(), book.isLentOut());
    }

    public List<BookDTO> toDTO(List<Book> books) {
        return books.stream().map(this::toDTO).toList();
    }

    public LentBookDTO toLentBookDTO(LentBook lentBook){
        return new LentBookDTO(lentBook.getIsbn(), lentBook.getUserId());
    }

    public List<LentBookDTO> toLentBookDTOList(List<LentBook> lentBooks){
        return lentBooks.stream().map(this :: toLentBookDTO).toList();
    }

}
