package specs.clubs;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.filter.log.LogDetail.ALL;
import static specs.BaseSpec.baseRequestSpec;

public class DeleteClubSpec {

    public static RequestSpecification deleteClubRequestSpec = baseRequestSpec;

    public static ResponseSpecification successfulDeleteClubResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(204)
            .build();
}
