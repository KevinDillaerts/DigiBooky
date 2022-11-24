package com.theexceptions.digibooky.service.books;

import com.theexceptions.digibooky.repository.books.Book;
import com.theexceptions.digibooky.repository.dtos.BookDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookMapper {
    public BookDTO toDTO(Book book){
        return new BookDTO(book.getIsbn(), book.getTitle(), book.getSmallSummary(), book.getAuthorFirstName(),
                book.getAuthorLastName(), book.isLentOut());
    }

    public List<BookDTO> toDTO(List<Book> books){
        return books.stream().map(this ::toDTO).toList();
    }


}
