package models.clubs.create_club;

/**
 * Тело POST /clubs/ — создание клуба.
 */
public record CreateClubRequestModel(
        String bookTitle,
        String bookAuthors,
        Integer publicationYear,
        String description,
        String telegramChatLink
) {}
