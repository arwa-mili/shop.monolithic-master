package arwa.web;


import arwa.utils.TestContainerResource;
import com.oracle.svm.core.annotate.Inject;
import io.quarkus.test.common.QuarkusTestResource;

import io.quarkus.test.junit.QuarkusTest;

import org.junit.jupiter.api.Test;


import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;
import java.util.HashMap;
import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.post;
import static io.restassured.RestAssured.delete;

import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.containsString;

import javax.sql.DataSource;

import static javax.ws.rs.core.Response.Status.OK;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
@QuarkusTestResource(TestContainerResource.class)

class CartResourceTest {

    private static final String INSERT_WRONG_CART_IN_DB =
            "insert into carts values (999, current_timestamp, current_timestamp, 'NEW', 3)";
    private static final String DELETE_WRONG_CART_IN_DB =
            "delete from carts where id = 999";
    @Inject
    Datasource datasource;

    @Test
    void testFindAll() {
        get("/carts").then().statusCode(OK.getStatusCode()).body("size()", greaterThan(0));
    }
    @Test
    void testFindAllActiveCarts() {
        get("/carts/active").then()
                .statusCode(OK.getStatusCode());
    }
    @Test
    void testGetActiveCartForCustomer() {
        get("/carts/customer/3").then()
                .statusCode(OK.getStatusCode())
                .body(containsString("Peter"));
    }
    @Test
    void testFindById() {
        get("/carts/3").then()
                .statusCode(OK.getStatusCode())
                .body(containsString("status"))
                .body(containsString("NEW"));
        get("/carts/100").then()
                .statusCode(NO_CONTENT.getStatusCode())
                .body(emptyOrNullString());
    }
    @Test
    void testDelete() {
        get("/carts/active").then()
                .statusCode(OK.getStatusCode())
                .body(containsString("Jason"))
                .body(containsString("NEW"));
        delete("/carts/1").then()
                .statusCode(NO_CONTENT.getStatusCode());
        get("/carts/1").then()
                .statusCode(OK.getStatusCode())
                .body(containsString("Jason"))
                .body(containsString("CANCELED"));
    }
    @Test
    void testGetActiveCartForCustomerWhenThereAreTwoCartsInDB() {
        executeSql(INSERT_WRONG_CART_IN_DB);
        get("/carts/customer/3").then()
                .statusCode(INTERNAL_SERVER_ERROR.getStatusCode())
                .body(containsString(INTERNAL_SERVER_ERROR.getReasonPhrase()))
                .body(containsString("Many active carts detected !!!"));
        executeSql(DELETE_WRONG_CART_IN_DB);
    }
    private void executeSql(String insertWrongCartInDb) {
        try (var connection = dataSource.getConnection()) {
            var statement = connection.createStatement();
            statement.executeUpdate(insertWrongCartInDb);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Test
    void testCreateCart() {
        var requestParams = new HashMap<>();
        requestParams.put("firstName", "Saul");
        requestParams.put("lastName", "Berenson");
        requestParams.put("email", "call.saul@mail.com");
        var newCustomerId = given()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body(requestParams).post("/customers").then()
                .statusCode(OK.getStatusCode())
                .extract()
  .jsonPath()
  .getInt("id");
        var response = post("/carts/customer/" + newCustomerId).then()
                .statusCode(OK.getStatusCode())
                .extract()
  .jsonPath()
  .getMap("$");
        assertNotNull(response.get("id"));
        
        delete("/carts/" + response.get("id")).then()
                .statusCode(NO_CONTENT.getStatusCode());
        delete("/customers/" + newCustomerId).then()
                .statusCode(NO_CONTENT.getStatusCode());
    }
        @Test
    void testFailCreateCartWhileHavingAlreadyActiveCart() {
        var requestParams = new HashMap<>();
        requestParams.put("firstName", "Saul");
        requestParams.put("lastName", "Berenson");
        requestParams.put("email", "call.saul@mail.com");
        var newCustomerId = given()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body(requestParams)
                .post("/customers").then()
                .statusCode(OK.getStatusCode())
                .extract()
                .jsonPath()
                .getLong("id");
        var newCartId = post("/carts/customer/" + newCustomerId).then()
                .statusCode(OK.getStatusCode())
                .extract()
                .jsonPath()
                .getLong("id");
        post("/carts/customer/" + newCustomerId).then()
                .statusCode(INTERNAL_SERVER_ERROR.getStatusCode())
                .body(containsString(INTERNAL_SERVER_ERROR.getReasonPhrase()))
                .body(containsString("There is already an active cart"));

        delete("/carts/" + newCartId).then()
                .statusCode(NO_CONTENT.getStatusCode());
        delete("/customers/" + newCustomerId).then()
                .statusCode(NO_CONTENT.getStatusCode());
    }



}

