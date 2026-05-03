package tests;

import io.qameta.allure.Feature;
import models.auth.login.LoginValidationErrorResponseModel;
import models.auth.login.LoginRequestModel;
import models.auth.login.LoginSuccessResponseModel;
import models.auth.login.LoginAuthErrorResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static tests.TestData.*;

@Feature("Тесты auth")
@DisplayName("Логин пользователя")
public class LoginTests extends TestBase {

    @Test
    @DisplayName("Успешный логин")
    public void successfulLoginTest() {
        LoginRequestModel loginData = new LoginRequestModel(LOGIN_USERNAME, LOGIN_PASSWORD);

        LoginSuccessResponseModel loginResponse = api.auth.login(loginData);

        String actualAccess = loginResponse.access();
        String actualRefresh = loginResponse.refresh();
        assertThat(actualAccess).startsWith(LOGIN_TOKEN_PREFIX);
        assertThat(actualRefresh).startsWith(LOGIN_TOKEN_PREFIX);
        assertThat(actualAccess).isNotEqualTo(actualRefresh);
    }

    @Test
    @DisplayName("Логин с неверным паролем")
    public void wrongPasswordLoginTest() {
        LoginRequestModel loginData = new LoginRequestModel(LOGIN_USERNAME, LOGIN_WRONG_PASSWORD);

        LoginAuthErrorResponseModel loginResponse = api.auth.loginWrongCredentials(loginData);

        String actualError = loginResponse.detail();
        assertThat(actualError).isEqualTo(LOGIN_WRONG_CREDENTIALS_ERROR);
    }

    // 401 WrongCredentials
    @Test
    @DisplayName("Логин с неверным именем")
    public void wrongUsernameLoginTest() {
        LoginRequestModel loginData = new LoginRequestModel(LOGIN_WRONG_USERNAME, LOGIN_PASSWORD);

        LoginAuthErrorResponseModel loginResponse = api.auth.loginWrongCredentials(loginData);

        String actualError = loginResponse.detail();
        assertThat(actualError).isEqualTo(LOGIN_WRONG_CREDENTIALS_ERROR);
    }

    // 400 EmptyField
    @Test
    @DisplayName("Логин без имени")
    public void emptyUsernameLoginTest() {
        LoginRequestModel loginData = new LoginRequestModel("", LOGIN_PASSWORD);

        LoginValidationErrorResponseModel loginResponse = api.auth.loginEmptyField(loginData);

        String actualError = loginResponse.username().get(0);
        assertThat(actualError).isEqualTo(EMPTY_FIELD_ERROR);
    }

    @Test
    @DisplayName("Логин без пароля")
    public void emptyPasswordLoginTest() {
        LoginRequestModel loginData = new LoginRequestModel(LOGIN_USERNAME, "");

        LoginValidationErrorResponseModel loginResponse = api.auth.loginEmptyField(loginData);

        String actualError = loginResponse.password().get(0);
        assertThat(actualError).isEqualTo(EMPTY_FIELD_ERROR);
    }

    @Test
    @DisplayName("Логин с пустыми полями имени и пароля")
    public void emptyFieldsLoginTest() {
        LoginRequestModel loginData = new LoginRequestModel("", "");

        LoginValidationErrorResponseModel loginResponse = api.auth.loginEmptyField(loginData);

        String expectedError = EMPTY_FIELD_ERROR;
        String actualUsernameError = loginResponse.username().get(0);
        String actualPasswordError = loginResponse.password().get(0);
        assertThat(actualUsernameError).isEqualTo(expectedError);
        assertThat(actualPasswordError).isEqualTo(expectedError);
    }

}
