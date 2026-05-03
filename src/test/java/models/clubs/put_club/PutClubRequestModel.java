package models.clubs.put_club;

/**
 * Тело PUT /clubs/{id}/ — изменение клуба.
 */
public record PutClubRequestModel(
        String bookTitle,
        String bookAuthors,
        Integer publicationYear,
        String description,
        String telegramChatLink
) {}
