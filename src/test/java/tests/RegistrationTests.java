package tests;

import io.qameta.allure.Feature;
import models.auth.login.LoginRequestModel;
import models.users.registration.RegistrationValidationErrorResponseModel;
import models.users.registration.RegistrationRequestModel;
import models.users.UserSuccessResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static tests.TestData.*;

@Feature("Тесты users")
@DisplayName("Регистрация пользователя")
public class RegistrationTests extends TestBase {

    String username;
    String password;

    @BeforeEach
    public void prepareTestData() {
        // оставляем генерацию данных в тесте, чтобы каждый запуск был с новыми пользователями
        username = "user_" + System.currentTimeMillis();
        password = "pass_" + System.currentTimeMillis();
    }

    @Test
    @DisplayName("Успешная регистрация нового пользователя")
    public void successfulRegistrationTest() {
        RegistrationRequestModel registrationData = new RegistrationRequestModel(username, password);

        UserSuccessResponseModel registrationResponse = api.users.register(registrationData);

        String accessToken = api.auth.loginAndGetAccessToken(
                new LoginRequestModel(username, password));
        try {
            assertThat(registrationResponse.id()).isGreaterThan(0);
            assertThat(registrationResponse.username()).isEqualTo(username);
            assertThat(registrationResponse.firstName()).isEqualTo("");
            assertThat(registrationResponse.lastName()).isEqualTo("");
            assertThat(registrationResponse.email()).isEqualTo("");
            assertThat(registrationResponse.remoteAddr()).matches(REGISTRATION_IP_REGEXP);
        } finally {
            api.users.deleteCurrentUser(accessToken);
        }
    }

    @Test
    @DisplayName("Создание уже существующего пользователя")
    public void existingUserWrongRegistrationTest() {
        RegistrationRequestModel registrationData = new RegistrationRequestModel(username, password);

        UserSuccessResponseModel firstRegistrationResponse = api.users.register(registrationData);
        assertThat(firstRegistrationResponse.username()).isEqualTo(username);

        String accessToken = api.auth.loginAndGetAccessToken(
                new LoginRequestModel(username, password));
        try {
            RegistrationValidationErrorResponseModel secondRegistrationResponse =
                    api.users.validationErrorRegister(registrationData);

            String actualError = secondRegistrationResponse.username().get(0);
            assertThat(actualError).isEqualTo(REGISTRATION_EXISTING_USER_ERROR);
        } finally {
            api.users.deleteCurrentUser(accessToken);
        }
    }

    @Test
    @DisplayName("Создание пользователя без пароля")
    public void emptyPasswordRegistrationTest() {
        RegistrationRequestModel registrationData = new RegistrationRequestModel(username, "");

        RegistrationValidationErrorResponseModel registrationResponse = api.users.validationErrorRegister(registrationData);

        String actualError = registrationResponse.getError();
        assertThat(actualError).isEqualTo(EMPTY_FIELD_ERROR);
    }

    @Test
    @DisplayName("Создание пользователя без имени")
    public void emptyUsernameRegistrationTest() {
        RegistrationRequestModel registrationData = new RegistrationRequestModel("", password);


        RegistrationValidationErrorResponseModel registrationResponse =
                api.users.validationErrorRegister(registrationData);

        String actualError = registrationResponse.getError();
        assertThat(actualError).isEqualTo(EMPTY_FIELD_ERROR);
    }

    @Test
    @DisplayName("Создание пользователя с длинным именем")
    public void longUsernameRegistrationTest() {
        String longName = LONG_USERNAME;

        RegistrationRequestModel registrationData = new RegistrationRequestModel(longName, password);

        RegistrationValidationErrorResponseModel registrationResponse =
                api.users.validationErrorRegisterLongUsername(longName.length(), registrationData);

        String actualError = registrationResponse.getError();
        assertThat(actualError).isEqualTo(LONG_USERNAME_ERROR);
    }

    @Test
    @DisplayName("Создание пользователя с длинным паролем")
    public void longPasswordRegistrationTest() {
        String longPassword = LONG_PASSWORD;

        RegistrationRequestModel registrationData = new RegistrationRequestModel(username, longPassword);

        RegistrationValidationErrorResponseModel registrationResponse =
                api.users.validationErrorRegisterLongPassword(longPassword.length(), registrationData);

        String actualError = registrationResponse.getError();
        assertThat(actualError).isEqualTo(LONG_PASSWORD_ERROR);
    }

}
