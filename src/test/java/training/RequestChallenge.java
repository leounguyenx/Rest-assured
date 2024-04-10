package training;

import io.restassured.response.ValidatableResponse;
import models.Product;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;

public class RequestChallenge {
    @Test
    public void createProduct() {
        String endpoint = "http://localhost:8888/api_testing/product/create.php";
        Product product = new Product("Sweatband", "Sweatband Des", 5, 3);
        ValidatableResponse response =
            given().body(product)
            .when().post(endpoint)
            .then().assertThat().statusCode(201);
        response.log().body();
    }

    @Test
    public void updateProduct() {
        String endpoint = "http://localhost:8888/api_testing/product/update.php";
        Product product = new Product(1004, "Sweatband", "Sweatband Description", 6, 3, "Active Wear - Unisex");

        ValidatableResponse response =
                given()
                        .body(product)
                .when()
                        .put(endpoint)
                .then()
                        .assertThat().statusCode(200);
        response.log().body();
    }

    @Test
    public void getProduct() {
        String endpoint = "http://localhost:8888/api_testing/product/read_one.php";
        ValidatableResponse response =
                given().queryParam("id", 1005)
                .when().get(endpoint)
                .then().assertThat().statusCode(200);
        response.log().body();
    }

    @Test
    public void deleteProduct() {
        String endpoint = "http://localhost:8888/api_testing/product/delete.php";
        String body = """
                {
                    "id": "1005"
                }
                """;
        ValidatableResponse response =
                given()
                        .body(body)
                .when()
                        .delete(endpoint)
                .then()
                        .assertThat().statusCode(200);
        response.log().body();
    }
}
