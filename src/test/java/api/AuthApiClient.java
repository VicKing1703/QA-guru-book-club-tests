package api;

import io.qameta.allure.Step;
import models.auth.login.LoginValidationErrorResponseModel;
import models.auth.login.LoginRequestModel;
import models.auth.login.LoginSuccessResponseModel;
import models.auth.login.LoginAuthErrorResponseModel;
import models.auth.logout.LogoutValidationErrorResponseModel;
import models.auth.logout.LogoutAuthErrorResponseModel;
import models.auth.logout.LogoutRequestModel;

import static io.restassured.RestAssured.given;
import static specs.auth.LoginSpec.*;
import static specs.auth.LogoutSpec.*;

public class AuthApiClient {

    @Step("Успешный логин, тело с access и refresh")
    public LoginSuccessResponseModel login(LoginRequestModel loginBody) {
        return given(loginRequestSpec)
                .body(loginBody)
                .when()
                .post("/auth/token/")
                .then()
                .spec(successfulLoginResponseSpec)
                .extract()
                .as(LoginSuccessResponseModel.class);
    }

    @Step("Авторизация и получение refresh-токена")
    public String loginAndGetRefreshToken(LoginRequestModel loginBody) {
        return given(loginRequestSpec)
                .body(loginBody)
                .when()
                .post("/auth/token/")
                .then()
                .spec(successfulLoginResponseSpec)
                .extract()
                .path("refresh");
    }

    @Step("Авторизация и получение access-токена")
    public String loginAndGetAccessToken(LoginRequestModel loginBody) {
        return given(loginRequestSpec)
                .body(loginBody)
                .when()
                .post("/auth/token/")
                .then()
                .spec(successfulLoginResponseSpec)
                .extract()
                .path("access");
    }

    @Step("Логин с неверным логином или паролем")
    public LoginAuthErrorResponseModel loginWrongCredentials(LoginRequestModel loginBody) {
        return given(loginRequestSpec)
                .body(loginBody)
                .when()
                .post("/auth/token/")
                .then()
                .spec(wrongCredentialsLoginResponseSpec)
                .extract()
                .as(LoginAuthErrorResponseModel.class);
    }

    @Step("Логин с пустым логином и/или паролем")
    public LoginValidationErrorResponseModel loginEmptyField(LoginRequestModel loginBody) {
        return  given(loginRequestSpec)
                .body(loginBody)
                .when()
                .post("/auth/token/")
                .then()
                .spec(emptyFieldLoginResponseSpec)
                .extract()
                .as(LoginValidationErrorResponseModel.class);
    }

    @Step("Успешный выход по refresh-токену")
    public void logout(LogoutRequestModel logoutBody) {
        given(logoutRequestSpec)
                .body(logoutBody)
                .when()
                .post("/auth/logout/")
                .then()
                .spec(successfulLogoutResponseSpec);
    }

    @Step("Выход без refresh-токена")
    public LogoutValidationErrorResponseModel validationErrorLogout(LogoutRequestModel logoutBody) {
        return given(logoutRequestSpec)
                .body(logoutBody)
                .when()
                .post("/auth/logout/")
                .then()
                .spec(validationErrorLogoutResponseSpec)
                .extract()
                .as(LogoutValidationErrorResponseModel.class);
    }

    @Step("Выход с неверным типом токена")
    public LogoutAuthErrorResponseModel authErrorLogout(LogoutRequestModel logoutBody) {
        return given(logoutRequestSpec)
                .body(logoutBody)
                .when()
                .post("/auth/logout/")
                .then()
                .spec(authErrorLogoutResponseSpec)
                .extract()
                .as(LogoutAuthErrorResponseModel.class);
    }
}

