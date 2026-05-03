package tests;

import io.qameta.allure.Feature;
import models.auth.login.LoginRequestModel;
import models.auth.logout.LogoutValidationErrorResponseModel;
import models.auth.logout.LogoutAuthErrorResponseModel;
import models.auth.logout.LogoutRequestModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static tests.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;

@Feature("Тесты auth")
@DisplayName("Логаут пользователя")
public class LogoutTests extends TestBase {

    @Test
    @DisplayName("Удачный логаут")
    public void successfulLogoutTest() {
        LoginRequestModel loginData = new LoginRequestModel(LOGIN_USERNAME, LOGIN_PASSWORD);
        String refreshToken = api.auth.loginAndGetRefreshToken(loginData);

        LogoutRequestModel logoutData = new LogoutRequestModel(refreshToken);
        api.auth.logout(logoutData);
    }

    @Test
    @DisplayName("Логаут с использованием access токена")
    public void accessTokenLogoutErrorTest() {
        LoginRequestModel loginData = new LoginRequestModel(LOGIN_USERNAME, LOGIN_PASSWORD);
        String accessToken = api.auth.loginAndGetAccessToken(loginData);

        LogoutRequestModel logoutData = new LogoutRequestModel(accessToken);
        LogoutAuthErrorResponseModel logoutResponse = api.auth.authErrorLogout(logoutData);

        String actualErrorDetail = logoutResponse.detail();
        String actualErrorCode = logoutResponse.code();
        assertThat(actualErrorDetail).isEqualTo(WRONG_TOKEN_TYPE_ERROR);
        assertThat(actualErrorCode).isEqualTo(VALIDATION_TOKEN_ERROR);
    }

    @Test
    @DisplayName("Логаут без токена")
    public void withoutTokenLogoutErrorTest() {
        LogoutRequestModel logoutData = new LogoutRequestModel("");

        LogoutValidationErrorResponseModel logoutResponse = api.auth.validationErrorLogout(logoutData);

        String actualError = logoutResponse.refresh().get(0);
        assertThat(actualError).isEqualTo(EMPTY_FIELD_ERROR);
    }
}

