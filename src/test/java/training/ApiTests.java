package training;

import io.restassured.response.ValidatableResponse;
import models.Product;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ApiTests {
    @Test
    public void getCategories() {
        String endpoint = "http://localhost:8888/api_testing/product/read.php";
        ValidatableResponse response =
                given()
                        .when()
                        .get(endpoint)
                        .then()
                        .log()
                        .headers()
                        .assertThat()
                        .headers("Content-Type", equalTo("application/json; charset=UTF-8"))
                        .statusCode(200)
                        .body("records.size()", greaterThan(0))
                        .body("records.id", everyItem(notNullValue()))
                        .body("records.name", everyItem(notNullValue()))
                        .body("records.description", everyItem(notNullValue()))
                        .body("records.price", everyItem(notNullValue()))
                        .body("records.category_id", everyItem(notNullValue()))
                        .body("records.category_name", everyItem(notNullValue()))
                        //Check if the first id value = 1006
                        .body("records.id[0]", equalTo("1006"));
        response.log().body();
    }

    @Test
    public void getProduct() {
        String endpoint = "http://localhost:8888/api_testing/product/read_one.php";
        ValidatableResponse response =
                given()
                        .queryParam("id", 2)
                        .when()
                        .get(endpoint)
                        .then()
                        .assertThat()
                        .statusCode(200)
                        .body("id", equalTo("2"))
                        .body("name", equalTo("Cross-Back Training Tank"))
                        .body("description", equalTo("The most awesome phone of 2013!"))
                        .body("price", equalTo("299.00"))
                        .body("category_id", equalTo("2"))
                        .body("category_name", equalTo("Active Wear - Women"));
        response.log().body();
    }

    @Test
    public void createProduct() {
        String endpoint = "http://localhost:8888/api_testing/product/create.php";
        String body = """
                {
                    "name": "Logitech 4 Life",
                    "description": "Logitech ",
                    "price": "14.00",
                    "category_id": "2",
                    "category_name": "Active Wear - Women"
                }
                """;
        //Using payload
        ValidatableResponse response =
                given()
                        .body(body)
                        .when()
                        .post(endpoint)
                        .then()
                        .assertThat().statusCode(201);
        response.log().body();
    }

    @Test
    public void updateProduct() {
        String endpoint = "http://localhost:8888/api_testing/product/update.php";
        String body = """
                {
                             "id": "18",
                             "name": "Multi-Vitamin (90 capsules)",
                             "description": "A daily dose of our Multi-Vitamins fulfills a day’s nutritional needs for over 12 vitamins and minerals.",
                             "price": "50.00",
                             "category_id": "4",
                             "category_name": "Supplements"
                }
                """;
        ValidatableResponse response =
                given()
                        .body(body)
                        .when()
                        .put(endpoint)
                        .then()
                        .assertThat().statusCode(200);
        response.log().body();
    }

    @Test
    public void deleteProduct() {
        String endpoint = "http://localhost:8888/api_testing/product/delete.php";
        String body = """
                {
                     "id": "16"
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

    /*-----*/
    @Test
    public void createSerializedProduct() {
        String endpoint = "http://localhost:8888/api_testing/product/create.php";
        Product product = new Product("Iphone", "Iphone 15", 1100.0, 2);
        ValidatableResponse response =
                given()
                        .body(product)
                        .when()
                        .post(endpoint)
                        .then().assertThat().statusCode(201);
        response.log().body();

    }

    @Test
    public void getSerializeProduct() {

        String endpoint = "http://localhost:8888/api_testing/product/read_one.php";
        Product expectedProduct = new Product(2, "Cross-Back Training Tank", "The most awesome phone of 2013!", 299.00, 2, "Active Wear - Women");
        Product actualProduct =
                given()
                        .queryParam("id", 2)
                        .when()
                        //JSON -> Object
                        .get(endpoint)
                        .as(Product.class);
        assertThat(actualProduct, samePropertyValuesAs(expectedProduct));
    }
}
