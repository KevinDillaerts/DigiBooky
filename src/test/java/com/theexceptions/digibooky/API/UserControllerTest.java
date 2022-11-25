package com.theexceptions.digibooky.API;

import com.theexceptions.digibooky.repository.dtos.CreateMemberDTO;
import com.theexceptions.digibooky.repository.dtos.CreateModeratorDTO;
import com.theexceptions.digibooky.repository.dtos.UserDTO;
import com.theexceptions.digibooky.repository.users.*;
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
class UserControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @Test
    void createMember_givenMemberData_thenTheNewlyCreatedMemberIsSavedAndReturned() {
        CreateMemberDTO createMemberDTO = new CreateMemberDTO("kevin@test.be", "paswoord", "Kevin", "Bacon", "2505050",
                new Address("Koekoeksstraat", "70", "9090", "Melle"));

        UserDTO userDTO =
                RestAssured
                        .given()
                        .contentType(JSON)
                        .body(createMemberDTO)
                        .accept(JSON)
                        .when()
                        .port(port)
                        .post("/users")
                        .then()
                        .assertThat()
                        .statusCode(HttpStatus.SC_CREATED)
                        .extract()
                        .as(UserDTO.class);

        assertThat(userDTO.getId()).isNotBlank();
        assertThat(userDTO.getEmail()).isEqualTo(createMemberDTO.getEmail());
        assertThat(userDTO.getRole()).isEqualTo(Role.MEMBER);
    }

    @Test
    void givenAdminUser_whenViewingAllUsers_thenAllUsersAreShown() {
        List<UserDTO> users =
                RestAssured
                        .given()
                        .auth().preemptive().basic("admin@digibooky.com", "admin")
                        .accept(JSON)
                        .when()
                        .port(port)
                        .get("/users")
                        .then()
                        .assertThat()
                        .statusCode(HttpStatus.SC_OK)
                        .extract().body().jsonPath().getList(".", UserDTO.class);

        assertThat(users.size()).isGreaterThan(0);
    }

    @Test
    void givenMember_whenViewingAllUsers_thenErrorThrown() {
        User testUser = new Member("test@testweer.be", "test", "Kevin", "Bacon", "1235",
                new Address("Koekoeksstraat", "70", "9090", "Melle"));
        userRepository.addUser(testUser);

        RestAssured
                .given()
                .auth().preemptive().basic(testUser.getEmail(), "test")
                .accept(JSON)
                .when()
                .port(port)
                .get("/users")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    @Test
    void givenAdminUser_whenCreatingLibrarian_thenLibrarianIsCreated() {
        CreateModeratorDTO createModeratorDTO = new CreateModeratorDTO("test@test.be", "librarian", "Kevin", "Dillaerts", Role.LIBRARIAN);

        UserDTO librarian =
                RestAssured
                        .given()
                        .contentType(JSON)
                        .auth().preemptive().basic("admin@digibooky.com", "admin")
                        .body(createModeratorDTO)
                        .accept(JSON)
                        .when()
                        .port(port)
                        .post("/users/admin")
                        .then()
                        .assertThat()
                        .statusCode(HttpStatus.SC_CREATED)
                        .extract()
                        .as(UserDTO.class);

        assertThat(librarian.getRole()).isEqualTo(Role.LIBRARIAN);
        assertThat(librarian.getEmail()).isEqualTo(createModeratorDTO.email());
        assertThat(librarian.getId()).isNotBlank();
    }

    @Test
    void givenMember_whenCreatingLibrarian_thenErrorThrown() {
        userRepository.addUser(new Member("kevin@digibooky.be", "kevin", "Kevin", "Digibooky", "1235555",
                new Address("Koekoeksstraat", "70", "9090", "Melle")));

        CreateModeratorDTO createModeratorDTO = new CreateModeratorDTO("testing@test.be", "librarian", "John", "Travolta", Role.LIBRARIAN);

        RestAssured
                .given()
                .contentType(JSON)
                .auth().preemptive().basic("kevin@digibooky.be", "kevin")
                .body(createModeratorDTO)
                .accept(JSON)
                .when()
                .port(port)
                .post("/users/admin")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_FORBIDDEN);
    }
}