package tests;

import io.qameta.allure.Feature;
import models.auth.login.LoginRequestModel;
import models.users.UserSuccessResponseModel;
import models.users.put_user.PutUserRequestModel;
import models.users.put_user.PutUserValidationErrorResponseModel;
import models.users.registration.RegistrationRequestModel;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static tests.TestData.REQUIRED_FIELD_ERROR;

@Feature("Тесты users")
@DisplayName("Изменение пользователя (PUT)")
public class UpdateUserTests extends TestBase {

    Faker faker = new Faker();
    String username;
    String password;
    String firstname;
    String lastName;
    String email;

    @BeforeEach
    public void prepareTestData() {

        username = "user_" + System.currentTimeMillis();
        password = "pass_" + System.currentTimeMillis();
        firstname = faker.name().firstName();
        lastName = faker.name().lastName();
        email = faker.internet().emailAddress();
    }

    @Test
    @DisplayName("Успешное изменение данных")
    public void successfulUpdateUserTest() {
        RegistrationRequestModel registrationData = new RegistrationRequestModel(username, password);
        api.users.register(registrationData);

        LoginRequestModel loginData = new LoginRequestModel(registrationData.username(), registrationData.password());
        String accessToken = api.auth.loginAndGetAccessToken(loginData);

        try {
            PutUserRequestModel updateData = new PutUserRequestModel(username, firstname, lastName, email);
            UserSuccessResponseModel updateResponse = api.users.putUser(accessToken, updateData);

            String userNameData = updateData.username();
            String userNameResponse = updateResponse.username();
            String firstNameData = updateData.firstName();
            String firstNameResponse = updateData.firstName();
            String lastNameData = updateData.lastName();
            String lastNameResponse = updateData.lastName();
            String emailData = updateData.email();
            String emailResponse = updateData.email();
            assertThat(userNameResponse).isEqualTo(userNameData);
            assertThat(firstNameResponse).isEqualTo(firstNameData);
            assertThat(lastNameResponse).isEqualTo(lastNameData);
            assertThat(emailResponse).isEqualTo(emailData);
        } finally {
            api.users.deleteCurrentUser(accessToken);
        }
    }

    @Test
    @DisplayName("Изменение без \"Firstname\"")
    public void withoutFirstnameFailedUpdateUserTest() {
        RegistrationRequestModel registrationData = new RegistrationRequestModel(username, password);
        api.users.register(registrationData);

        LoginRequestModel loginData = new LoginRequestModel(registrationData.username(), registrationData.password());
        String accessToken = api.auth.loginAndGetAccessToken(loginData);

        try {
            PutUserRequestModel updateData = PutUserRequestModel.withoutFirstName(username, lastName, email);
            PutUserValidationErrorResponseModel response = api.users.validationErrorPutUser(accessToken, updateData);

            assertThat(response.firstName()).containsExactly(REQUIRED_FIELD_ERROR);
            assertThat(response.username()).isNull();
            assertThat(response.lastName()).isNull();
            assertThat(response.email()).isNull();
        } finally {
            api.users.deleteCurrentUser(accessToken);
        }
    }

    @Test
    @DisplayName("Изменение без передачи всех параметров")
    public void withoutAllFieldsFailedUpdateUserTest() {
        RegistrationRequestModel registrationData = new RegistrationRequestModel(username, password);
        api.users.register(registrationData);

        LoginRequestModel loginData = new LoginRequestModel(registrationData.username(), registrationData.password());
        String accessToken = api.auth.loginAndGetAccessToken(loginData);

        try {
            PutUserRequestModel updateData = PutUserRequestModel.withoutAllFields();

            PutUserValidationErrorResponseModel response = api.users.validationErrorPutUser(accessToken, updateData);

            String expectedErrorMessage = REQUIRED_FIELD_ERROR;
            assertThat(response.firstName()).containsExactly(expectedErrorMessage);
            assertThat(response.username()).containsExactly(expectedErrorMessage);
            assertThat(response.lastName()).containsExactly(expectedErrorMessage);
            assertThat(response.email()).containsExactly(expectedErrorMessage);
        } finally {
            api.users.deleteCurrentUser(accessToken);
        }
    }
}
