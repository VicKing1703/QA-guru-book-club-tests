package api;

import io.qameta.allure.Step;
import models.clubs.ClubModel;
import models.clubs.create_club.CreateClubRequestModel;
import models.clubs.get_clubs.ClubsListResponseModel;
import models.clubs.put_club.PutClubRequestModel;

import static io.restassured.RestAssured.given;
import static specs.clubs.CreateClubSpec.createClubRequestSpec;
import static specs.clubs.CreateClubSpec.successfulCreateClubResponseSpec;
import static specs.clubs.DeleteClubSpec.deleteClubRequestSpec;
import static specs.clubs.DeleteClubSpec.successfulDeleteClubResponseSpec;
import static specs.clubs.GetClubsSpec.getClubsRequestSpec;
import static specs.clubs.GetClubsSpec.successfulClubsListResponseSpec;
import static specs.clubs.PutClubSpec.putClubRequestSpec;
import static specs.clubs.PutClubSpec.successfulPutClubResponseSpec;

public class ClubsApiClient {

    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_PAGE_SIZE = 10;

    @Step("Первая страница списка клубов (page=1, page_size=10)")
    public ClubsListResponseModel getClubs() {
        return getClubs(DEFAULT_PAGE, DEFAULT_PAGE_SIZE);
    }

    @Step("Список клубов (page={page}, page_size={pageSize})")
    public ClubsListResponseModel getClubs(Integer page, Integer pageSize) {
        return given(getClubsRequestSpec)
                .queryParam("page", page)
                .queryParam("page_size", pageSize)
                .when()
                .get("/clubs/")
                .then()
                .spec(successfulClubsListResponseSpec)
                .extract()
                .as(ClubsListResponseModel.class);
    }

    @Step("Создание клуба")
    public ClubModel createClub(String accessToken, CreateClubRequestModel body) {
        return given(createClubRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .body(body)
                .when()
                .post("/clubs/")
                .then()
                .spec(successfulCreateClubResponseSpec)
                .extract()
                .as(ClubModel.class);
    }

    @Step("Обновление клуба (id={clubId})")
    public ClubModel putClub(String accessToken, Integer clubId, PutClubRequestModel body) {
        return given(putClubRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .pathParam("clubId", clubId)
                .body(body)
                .when()
                .put("/clubs/{clubId}/")
                .then()
                .spec(successfulPutClubResponseSpec)
                .extract()
                .as(ClubModel.class);
    }

    @Step("Удаление клуба (id={clubId}, без тела ответа)")
    public void deleteClub(String accessToken, Integer clubId) {
        given(deleteClubRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .pathParam("clubId", clubId)
                .when()
                .delete("/clubs/{clubId}/")
                .then()
                .spec(successfulDeleteClubResponseSpec);
    }
}
