package api;

import io.qameta.allure.Step;
import models.users.put_user.PutUserRequestModel;
import models.users.put_user.PutUserValidationErrorResponseModel;
import models.users.registration.RegistrationValidationErrorResponseModel;
import models.users.registration.RegistrationRequestModel;
import models.users.UserSuccessResponseModel;

import static io.restassured.RestAssured.given;
import static specs.users.DeleteUserSpec.deleteUserRequestSpec;
import static specs.users.DeleteUserSpec.successfulDeleteUserResponseSpec;
import static specs.users.PutUserSpec.*;
import static specs.users.RegistrationSpec.*;

public class UsersApiClient {

    @Step("Успешная регистрация")
    public UserSuccessResponseModel register(RegistrationRequestModel body) {
        return given(registrationRequestSpec)
                .body(body)
                .when()
                .post("/users/register/")
                .then()
                .spec(successfulRegistrationResponseSpec)
                .extract()
                .as(UserSuccessResponseModel.class);
    }

    @Step("Регистрация с ошибкой валидации")
    public RegistrationValidationErrorResponseModel validationErrorRegister(RegistrationRequestModel body) {
        return executeValidationErrorRegister(body);
    }

    @Step("Регистрация с именем длиной {usernameLength} символов")
    public RegistrationValidationErrorResponseModel validationErrorRegisterLongUsername(
            int usernameLength, RegistrationRequestModel body) {
        return executeValidationErrorRegister(body);
    }

    @Step("Регистрация с паролем длиной {passwordLength} символов")
    public RegistrationValidationErrorResponseModel validationErrorRegisterLongPassword(
            int passwordLength, RegistrationRequestModel body) {
        return executeValidationErrorRegister(body);
    }

    private RegistrationValidationErrorResponseModel executeValidationErrorRegister(RegistrationRequestModel body) {
        return given(registrationRequestSpec)
                .body(body)
                .when()
                .post("/users/register/")
                .then()
                .spec(validationErrorRegistrationResponseSpec)
                .extract()
                .as(RegistrationValidationErrorResponseModel.class);
    }

    @Step("Успешное обновление текущего пользователя")
    public UserSuccessResponseModel putUser(String accessToken, PutUserRequestModel body) {
        return given(putUserRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .body(body)
                .when()
                .put("/users/me/")
                .then()
                .spec(successfulPutUserResponseSpec)
                .extract()
                .as(UserSuccessResponseModel.class);
    }

    @Step("Обновление профиля с ошибкой валидации")
    public PutUserValidationErrorResponseModel validationErrorPutUser(String accessToken, PutUserRequestModel body) {
        return given(putUserRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .body(body)
                .when()
                .put("/users/me/")
                .then()
                .spec(validationErrorPutUserResponseSpec)
                .extract()
                .as(PutUserValidationErrorResponseModel.class);
    }

    @Step("Удаление текущего пользователя, без тела ответа")
    public void deleteCurrentUser(String accessToken) {
        given(deleteUserRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .when()
                .delete("/users/me/")
                .then()
                .spec(successfulDeleteUserResponseSpec);
    }
}
