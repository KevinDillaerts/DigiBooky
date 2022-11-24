package com.theexceptions.digibooky.API;

import com.theexceptions.digibooky.repository.books.Book;
import com.theexceptions.digibooky.repository.books.BookRepository;
import com.theexceptions.digibooky.repository.dtos.BookDTO;
import com.theexceptions.digibooky.repository.dtos.CreateBookDTO;
import com.theexceptions.digibooky.repository.dtos.CreateModeratorDTO;
import com.theexceptions.digibooky.repository.dtos.UserDTO;
import com.theexceptions.digibooky.repository.users.*;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

import static io.restassured.http.ContentType.JSON;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class BookControllerTest {
    @Value("8080")
    private int port;

    @Autowired
    private UserRepository userRepository;
    private BookRepository bookRepository;

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
}
