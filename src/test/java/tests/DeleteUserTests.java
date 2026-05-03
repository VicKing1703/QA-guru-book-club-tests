package tests;

import io.qameta.allure.Feature;
import models.auth.login.LoginRequestModel;
import models.users.registration.RegistrationRequestModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Feature("Тесты users")
@DisplayName("Удаление пользователя")
public class DeleteUserTests extends TestBase {

    String username;
    String password;

    @BeforeEach
    public void prepareTestData() {
        username = "user_" + System.currentTimeMillis();
        password = "pass_" + System.currentTimeMillis();
    }

    @Test
    @DisplayName("Успешное удаление текущего пользователя")
    public void successfulDeleteUserTest() {
        RegistrationRequestModel registrationData = new RegistrationRequestModel(username, password);
        api.users.register(registrationData);

        String accessToken = api.auth.loginAndGetAccessToken(
                new LoginRequestModel(registrationData.username(), registrationData.password()));

        api.users.deleteCurrentUser(accessToken);
    }
}
