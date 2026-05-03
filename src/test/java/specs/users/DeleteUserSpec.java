package specs.users;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.filter.log.LogDetail.ALL;
import static specs.BaseSpec.baseRequestSpec;

public class DeleteUserSpec {

    public static RequestSpecification deleteUserRequestSpec = baseRequestSpec;

    public static ResponseSpecification successfulDeleteUserResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(204)
            .build();
}
