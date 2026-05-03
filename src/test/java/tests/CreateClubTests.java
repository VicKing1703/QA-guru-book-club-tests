package tests;

import io.qameta.allure.Feature;
import models.auth.login.LoginRequestModel;
import models.clubs.ClubModel;
import models.clubs.create_club.CreateClubRequestModel;
import net.datafaker.Faker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static tests.TestData.LOGIN_PASSWORD;
import static tests.TestData.LOGIN_USERNAME;

@Feature("Тесты clubs")
@DisplayName("Создание клуба")
public class CreateClubTests extends TestBase {

    private final Faker faker = new Faker();

    @Test
    @DisplayName("Успешное создание клуба")
    public void successfulCreateClubTest() {
        String accessToken = api.auth.loginAndGetAccessToken(
                new LoginRequestModel(LOGIN_USERNAME, LOGIN_PASSWORD));

        CreateClubRequestModel request = new CreateClubRequestModel(
                faker.book().title() + " " + faker.number().digits(5),
                faker.book().author() + ", " + faker.book().author(),
                faker.number().numberBetween(1950, 2026),
                faker.lorem().paragraph(),
                "https://t.me/" + faker.internet().slug()
        );

        ClubModel created = api.clubs.createClub(accessToken, request);

        try {
            assertThat(created.id()).isNotNull().isPositive();
            assertThat(created.bookTitle()).isEqualTo(request.bookTitle());
            assertThat(created.bookAuthors()).isEqualTo(request.bookAuthors());
            assertThat(created.publicationYear()).isEqualTo(request.publicationYear());
            assertThat(created.description()).isEqualTo(request.description());
            assertThat(created.telegramChatLink()).isEqualTo(request.telegramChatLink());
            assertThat(created.owner()).isNotNull().isPositive();
            assertThat(created.members()).isNotNull();
            assertThat(created.reviews()).isNotNull();
            assertThat(created.created()).isNotNull();
        } finally {
            api.clubs.deleteClub(accessToken, created.id());
        }
    }
}
