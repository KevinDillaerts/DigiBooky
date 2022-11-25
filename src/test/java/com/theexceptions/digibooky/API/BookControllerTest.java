package com.theexceptions.digibooky.API;

import com.theexceptions.digibooky.repository.books.Book;
import com.theexceptions.digibooky.repository.books.BookRepository;
import com.theexceptions.digibooky.repository.books.LendBookIdDTO;
import com.theexceptions.digibooky.repository.books.LentBookRepository;
import com.theexceptions.digibooky.repository.dtos.BookDTO;
import com.theexceptions.digibooky.repository.dtos.CreateBookDTO;
import com.theexceptions.digibooky.repository.users.*;
import com.theexceptions.digibooky.service.books.BookMapper;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class BookControllerTest {
    @Value("8080")
    private int port;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private LentBookRepository lentBookRepository;

    //OK
    @Test
    void createLibrarian_givenBookData_thenTheNewlyCreatedBookIsSavedAndReturned() {
        userRepository.addUser(new User("librarian@digibooky.com", "librarian", "Kevin", "Dillaerts", Role.LIBRARIAN));
        CreateBookDTO bookToCreate = new CreateBookDTO("123", "title", "summary", "Jonas", "Nata");

        BookDTO bookDTO =
                RestAssured
                        .given()
                        .auth().preemptive().basic("librarian@digibooky.com", "librarian")
                        .contentType(JSON)
                        .body(bookToCreate)
                        .accept(JSON)
                        .when()
                        .port(port)
                        .post("/books")
                        .then()
                        .assertThat()
                        .statusCode(HttpStatus.SC_CREATED)
                        .extract()
                        .as(BookDTO.class);

        assertThat(bookDTO.getIsbn()).isEqualTo(bookToCreate.getIsbn());
        assertThat(bookDTO.getTitle()).isEqualTo(bookToCreate.getTitle());
        assertThat(bookDTO.getAuthorLastName()).isEqualTo(bookToCreate.getAuthorLastName());

    }

    @Test
    void createMember_whenCreatingBook_throwsUnauthorizedException() {
        User testUser = new Member("test@testweer.be", "test", "Kevin", "Bacon", "1235",
                new Address("Koekoeksstraat", "70", "9090", "Melle"));
        userRepository.addUser(testUser);
        CreateBookDTO bookToCreate = new CreateBookDTO("123", "title", "summary", "Jonas", "Nata");

        RestAssured
                .given()
                .auth().preemptive().basic("test@testweer.be", "test")
                .contentType(JSON)
                .body(bookToCreate)
                .accept(JSON)
                .when()
                .port(port)
                .post("/books")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    @Test
    void givenListOfBooks_whenSearchingWithParams_thenTheCorrectBookIsReturned() {
        Book book1 = new Book("123456", "The DiscWorld",
                "All about wizzzzzards!", "Terry", "Pratchett");
        Book book2 = new Book("289456", "Good Omens",
                "All about gods.", "Neill", "Gaimon");
        bookRepository.addBook(book1);
        bookRepository.addBook(book2);
        BookMapper mapper = new BookMapper();


        List<BookDTO> bookDTOList =
                RestAssured
                        .given()
                        .accept(JSON)
                        .when()
                        .port(port)
                        .get("/books?title=disc")
                        .then()
                        .assertThat()
                        .statusCode(HttpStatus.SC_OK)
                        .extract().body().jsonPath().getList(".", BookDTO.class);

        assertThat(bookDTOList).containsExactlyInAnyOrder(mapper.toDTO(book1));
    }

    @Test
    void givenListOfBooks_whenSearchingWithParams_thenTheCorrectBooksAreReturned() {
        Book book1 = new Book("123456", "The DiscWorld",
                "All about wizzzzzards!", "Terry", "Pratchett");
        Book book2 = new Book("289456", "Good Omens",
                "All about gods.", "Neill", "Gaimon");
        bookRepository.addBook(book1);
        bookRepository.addBook(book2);
        BookMapper mapper = new BookMapper();


        List<BookDTO> bookDTOList =
                RestAssured
                        .given()
                        .accept(JSON)
                        .when()
                        .port(port)
                        .get("/books?isbn=456")
                        .then()
                        .assertThat()
                        .statusCode(HttpStatus.SC_OK)
                        .extract().body().jsonPath().getList(".", BookDTO.class);

        assertThat(bookDTOList).containsExactlyInAnyOrder(mapper.toDTO(book1), mapper.toDTO(book2));
    }

    @Test
    void givenListOfBooks_whenSearchingWithWrongParams_thenThrowInvalidFilterValueException() {
        Book book1 = new Book("123456", "The DiscWorld",
                "All about wizzzzzards!", "Terry", "Pratchett");
        Book book2 = new Book("289456", "Good Omens",
                "All about gods.", "Neill", "Gaimon");
        bookRepository.addBook(book1);
        bookRepository.addBook(book2);
        BookMapper mapper = new BookMapper();

        RestAssured
                .given()
                .accept(JSON)
                .when()
                .port(port)
                .get("/books?wrongParam=456")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    void givenMember_whenMemberLendsBook_BookIsInLentList() {
        User testUser = new Member("test@testweer.be", "test", "Kevin", "Bacon", "1235",
                new Address("Koekoeksstraat", "70", "9090", "Melle"));
        userRepository.addUser(testUser);
        Book book1 = new Book("123456", "The DiscWorld",
                "All about wizzzzzards!", "Terry", "Pratchett");
        Book book2 = new Book("289456", "Good Omens",
                "All about gods.", "Neill", "Gaimon");
        bookRepository.addBook(book1);
        bookRepository.addBook(book2);

        LendBookIdDTO lendBookIdDTO = new LendBookIdDTO("123456");

        RestAssured
                .given()
                .auth().preemptive().basic("test@testweer.be", "test")
                .contentType(JSON)
                .accept(JSON)
                .when()
                .port(port)
                .body(lendBookIdDTO)
                .post("books/lend")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_CREATED);

        assertThat(lentBookRepository.getAllLendBooks()).anyMatch(lentBook -> lentBook.getIsbn().equals("123456"));
    }
}
