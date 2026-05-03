package tests;

import io.qameta.allure.Feature;
import models.clubs.ClubModel;
import models.clubs.get_clubs.ClubsListResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Feature("Тесты clubs")
@DisplayName("Получение списка клубов")
public class GetClubsTests extends TestBase {

    private static final int DEFAULT_PAGE_SIZE = 10;

    @Test
    @DisplayName("Успешное получение списка клубов")
    public void successfulGetClubsListTest() {
        ClubsListResponseModel response = api.clubs.getClubs(1, DEFAULT_PAGE_SIZE);
        assertThat(response).isNotNull();
        assertThat(response.count()).isGreaterThanOrEqualTo(0);
        assertThat(response.results()).isNotNull();
        assertThat(response.results().size())
                .as("на странице не больше элементов, чем page_size")
                .isLessThanOrEqualTo(DEFAULT_PAGE_SIZE);
        assertThat(response.results().size())
                .as("элементов на странице не больше, чем всего в выборке")
                .isLessThanOrEqualTo(response.count());
    }

    @Test
    @DisplayName("Размер results на странице не превышает count")
    public void getClubsListRespectsTotalCountTest() {
        ClubsListResponseModel response = api.clubs.getClubs();

        assertThat(response.results().size())
                .as("размер results не превышает count (пагинация)")
                .isLessThanOrEqualTo(response.count());
    }

    @Test
    @DisplayName("У каждого клуба в списке заполнены обязательные поля")
    public void getClubsListRequiredFieldsTest() {
        ClubsListResponseModel response = api.clubs.getClubs();

        for (ClubModel club : response.results()) {
            assertThat(club.id()).isNotNull().isPositive();
            assertThat(club.bookTitle()).isNotNull();
            assertThat(club.bookAuthors()).isNotNull();
            assertThat(club.publicationYear()).isNotNull();
            assertThat(club.description()).isNotNull();
            assertThat(club.telegramChatLink()).isNotNull();
            assertThat(club.owner()).isNotNull().isPositive();
            assertThat(club.members()).isNotNull();
            assertThat(club.reviews()).isNotNull();
            assertThat(club.created()).isNotNull();
        }
    }

    @Test
    @DisplayName("В ответе списка клубов есть count и results")
    public void getClubsListPaginationFieldsTest() {
        ClubsListResponseModel response = api.clubs.getClubs();

        assertThat(response.count()).isNotNull();
        // next и previous могут быть null при одной странице
        assertThat(response.results()).isNotNull();
    }
}
