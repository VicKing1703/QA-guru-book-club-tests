package tests;

import io.qameta.allure.Feature;
import models.auth.login.LoginRequestModel;
import models.clubs.ClubModel;
import models.clubs.create_club.CreateClubRequestModel;
import net.datafaker.Faker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static tests.TestData.LOGIN_PASSWORD;
import static tests.TestData.LOGIN_USERNAME;

@Feature("Тесты clubs")
@DisplayName("Удаление клуба")
public class DeleteClubTests extends TestBase {

    private final Faker faker = new Faker();

    @Test
    @DisplayName("Успешное удаление клуба")
    public void successfulDeleteClubTest() {
        String accessToken = api.auth.loginAndGetAccessToken(
                new LoginRequestModel(LOGIN_USERNAME, LOGIN_PASSWORD));

        CreateClubRequestModel createBody = new CreateClubRequestModel(
                faker.book().title() + " " + faker.number().digits(5),
                faker.book().author() + ", " + faker.book().author(),
                faker.number().numberBetween(1950, 2026),
                faker.lorem().paragraph(),
                "https://t.me/" + faker.internet().slug()
        );
        ClubModel created = api.clubs.createClub(accessToken, createBody);

        api.clubs.deleteClub(accessToken, created.id());
    }
}
