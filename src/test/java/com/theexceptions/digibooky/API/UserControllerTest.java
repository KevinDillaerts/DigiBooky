package com.theexceptions.digibooky.API;

import com.theexceptions.digibooky.repository.dtos.CreateMemberDTO;
import com.theexceptions.digibooky.repository.dtos.UserDTO;
import com.theexceptions.digibooky.repository.users.Address;
import com.theexceptions.digibooky.repository.users.Role;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import io.restassured.RestAssured;

import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class UserControllerTest {

    @Value("8080")
    private int port;

    @Test
    void createMember_givenMemberData_thenTheNewlyCreatedMemberisSavedAndReturned() {
        CreateMemberDTO createMemberDTO = new CreateMemberDTO("raf@raf.be", "abc123","Raf", "Abbeel","1111",
                new Address("Kouterbaan","22A","9280","Lesbeke"));

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

}