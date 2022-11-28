package com.theexceptions.digibooky.API;

import com.theexceptions.digibooky.repository.books.*;
import com.theexceptions.digibooky.repository.dtos.*;
import com.theexceptions.digibooky.repository.users.*;
import com.theexceptions.digibooky.service.books.BookMapper;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.List;

import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private LentBookRepository lentBookRepository;
    @Autowired
    private BookArchiveRepository bookArchiveRepository;

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

        LentBookIdDTO lendBookIdDTO = new LentBookIdDTO("123456");

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

    @Test
    void givenMemberWithLentBook_whenMemberReturnsBook_messageIsCorrect() {
        User testUser = new Member("test5@testweer.be", "test", "Kevin", "Bacon", "1235",
                new Address("Koekoeksstraat", "70", "9090", "Melle"));
        userRepository.addUser(testUser);
        Book book1 = new Book("12345678", "The DiscWorld",
                "All about wizzzzzards!", "Terry", "Pratchett");
        Book book2 = new Book("289456", "Good Omens",
                "All about gods.", "Neill", "Gaimon");
        bookRepository.addBook(book1);
        bookRepository.addBook(book2);

        LentBook lentBook = new LentBook("12345678", testUser.getId());
        lentBookRepository.addLentBook(lentBook);
        LentBookIdDTO lentBookIdDTO = new LentBookIdDTO(lentBook.getLentBookId());

        String message = RestAssured
                .given()
                .auth().preemptive().basic("test5@testweer.be", "test")
                .contentType(JSON)
                .accept(JSON)
                .when()
                .port(port)
                .body(lentBookIdDTO)
                .delete("books/return/")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response().asString();

        assertThat(lentBookRepository.getAllLendBooks()).noneMatch(book -> book.getIsbn().equals("12345678"));
        assertThat(message.equals("Thank you for returning your book.")).isTrue();
    }

    @Test
    void givenMemberWithLentBookNotHis_whenMemberReturnsBook_messageIsCorrect() {
        User testUser = new Member("test@testweer.be", "test", "Kevin", "Bacon", "1235",
                new Address("Koekoeksstraat", "70", "9090", "Melle"));
        userRepository.addUser(testUser);
        Book book1 = new Book("123456", "The DiscWorld",
                "All about wizzzzzards!", "Terry", "Pratchett");
        Book book2 = new Book("289456", "Good Omens",
                "All about gods.", "Neill", "Gaimon");
        bookRepository.addBook(book1);
        bookRepository.addBook(book2);

        LentBook lentBook = new LentBook("123456", "54533");
        lentBookRepository.addLentBook(lentBook);
        LentBookIdDTO lentBookIdDTO = new LentBookIdDTO(lentBook.getLentBookId());

        RestAssured
                .given()
                .auth().preemptive().basic("test@testweer.be", "test")
                .contentType(JSON)
                .accept(JSON)
                .when()
                .port(port)
                .body(lentBookIdDTO)
                .delete("books/return/")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    @Test
    void givenALibrarianAndABook_whenArchivingBook_StatusOkIsReturnedWhenSuccesFull() {
        User testUser = new User("test4@testweer.be", "test", "Kevin", "Bacon", Role.LIBRARIAN);
        Book book1 = new Book("123456", "The DiscWorld",
                "All about wizzzzzards!", "Terry", "Pratchett");
        userRepository.addUser(testUser);
        bookRepository.addBook(book1);

        RestAssured
                .given()
                .auth().preemptive().basic("test4@testweer.be", "test")
                .when()
                .port(port)
                .delete("books/" + book1.getIsbn())
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    void givenALibrarianAndAnArchivedBook_whenRestoringBook_StatusCreatedAndBookIsReturned() {
        User testUser = new User("test2@testweer.be", "test", "Kevin", "Bacon", Role.LIBRARIAN);
        Book book1 = new Book("123456", "The DiscWorld",
                "All about wizzzzzards!", "Terry", "Pratchett");
        RestoreBookDTO bookToRestore = new RestoreBookDTO("123456");
        userRepository.addUser(testUser);
        bookArchiveRepository.addBook(book1);

        BookDTO bookDTO =
                RestAssured
                        .given()
                        .auth().preemptive().basic("test2@testweer.be", "test")
                        .contentType(JSON)
                        .body(bookToRestore)
                        .accept(JSON)
                        .when()
                        .port(port)
                        .post("/books/restore")
                        .then()
                        .assertThat()
                        .statusCode(HttpStatus.SC_CREATED)
                        .extract()
                        .as(BookDTO.class);
    }

    @Test
    void givenLibrarianAndMemberWithLentBooks_whenRequestingList_listShowsCorrectBooks() {
        User testUser = new Member("test@testweer.be", "test", "Kevin", "Bacon", "1235",
                new Address("Koekoeksstraat", "70", "9090", "Melle"));
        userRepository.addUser(testUser);

        userRepository.addUser(new User("librarian@digibooky.com", "librarian", "Kevin", "Dillaerts", Role.LIBRARIAN));

        Book book1 = new Book("123456", "The DiscWorld",
                "All about wizzzzzards!", "Terry", "Pratchett");
        Book book2 = new Book("289456", "Good Omens",
                "All about gods.", "Neill", "Gaimon");
        bookRepository.addBook(book1);
        bookRepository.addBook(book2);

        LentBook lentBook1 = new LentBook("123456", testUser.getId());
        LentBook lentBook2 = new LentBook("289456", testUser.getId());
        lentBookRepository.addLentBook(lentBook1);
        lentBookRepository.addLentBook(lentBook2);

        List<LentBookDTO> listOfBookLent = RestAssured
                .given()
                .auth().preemptive().basic("librarian@digibooky.com", "librarian")
                .accept(JSON)
                .when()
                .port(port)
                .get("books/lent/" + testUser.getId())
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract().body().jsonPath().getList(".", LentBookDTO.class);

        assertThat(listOfBookLent).allMatch(lentBookDTO -> lentBookDTO.getUserId().equals(testUser.getId()));
        assertThat(listOfBookLent.size() == 2).isTrue();
    }

    @Test
    void givenLibrarianAndMemberWithNoBooks_whenRequestingList_exceptionIsThrown() {
        User testUser = new Member("test@testweer.be", "test", "Kevin", "Bacon", "1235",
                new Address("Koekoeksstraat", "70", "9090", "Melle"));
        userRepository.addUser(testUser);

        userRepository.addUser(new User("librarian@digibooky.com", "librarian", "Kevin", "Dillaerts", Role.LIBRARIAN));

        RestAssured
                .given()
                .auth().preemptive().basic("librarian@digibooky.com", "librarian")
                .accept(JSON)
                .when()
                .port(port)
                .get("books/lent/" + testUser.getId())
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

/*    @Test
    void givenMember_whenViewingDetailsOfBook_detailsOfLentBookAreEnhanced() {
        User testUser = new Member("test@testweer.be", "test", "Kevin", "Bacon", "1235",
                new Address("Koekoeksstraat", "70", "9090", "Melle"));
        userRepository.addUser(testUser);
        Book book1 = new Book("123456", "The DiscWorld",
                "All about wizzzzzards!", "Terry", "Pratchett");
        bookRepository.addBook(book1);

        LentBook lentBook = new LentBook("123456", testUser.getId());
        lentBookRepository.addLentBook(lentBook);

        String message = RestAssured
                .given()
                .auth().preemptive().basic("test@testweer.be", "test")
                .accept(JSON)
                .when()
                .port(port)
                .get("books/123456")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract().response().asString();

        System.out.println(message);
        //assertThat(bookDetails.getUserName().equals("Kevin Bacon")).isTrue();
    }*/

}
