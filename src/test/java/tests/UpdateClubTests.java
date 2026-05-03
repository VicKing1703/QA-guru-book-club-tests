package tests;

import io.qameta.allure.Feature;
import models.auth.login.LoginRequestModel;
import models.clubs.ClubModel;
import models.clubs.create_club.CreateClubRequestModel;
import models.clubs.put_club.PutClubRequestModel;
import net.datafaker.Faker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static tests.TestData.LOGIN_PASSWORD;
import static tests.TestData.LOGIN_USERNAME;

@Feature("Тесты clubs")
@DisplayName("Изменение клуба")
public class UpdateClubTests extends TestBase {

    private final Faker faker = new Faker();

    @Test
    @DisplayName("Успешное изменение клуба")
    public void successfulUpdateClubTest() {
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

        try {
            PutClubRequestModel updateBody = new PutClubRequestModel(
                    faker.book().title() + " " + faker.number().digits(5),
                    faker.book().author() + ", " + faker.book().author(),
                    faker.number().numberBetween(1950, 2026),
                    faker.lorem().paragraph(),
                    "https://t.me/" + faker.internet().slug()
            );
            ClubModel updated = api.clubs.putClub(accessToken, created.id(), updateBody);

            assertThat(updated.id()).isEqualTo(created.id());
            assertThat(updated.bookTitle()).isEqualTo(updateBody.bookTitle());
            assertThat(updated.bookAuthors()).isEqualTo(updateBody.bookAuthors());
            assertThat(updated.publicationYear()).isEqualTo(updateBody.publicationYear());
            assertThat(updated.description()).isEqualTo(updateBody.description());
            assertThat(updated.telegramChatLink()).isEqualTo(updateBody.telegramChatLink());
            assertThat(updated.owner()).isEqualTo(created.owner());
            assertThat(updated.members()).isNotNull();
            assertThat(updated.reviews()).isNotNull();
            assertThat(updated.created()).isEqualTo(created.created());
            assertThat(updated.modified()).isNotNull();
        } finally {
            api.clubs.deleteClub(accessToken, created.id());
        }
    }
}
