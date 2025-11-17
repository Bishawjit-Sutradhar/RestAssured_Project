import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.commons.configuration.ConfigurationException;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static io.restassured.RestAssured.given;

public class MyRestAssured {

    Properties prop;
    public MyRestAssured() throws IOException {
        //Read for Config File
        prop=new Properties();
        FileInputStream fs=new FileInputStream("./src/test/resources/config.properties");
        prop.load(fs);
    }

    @Test
    public void doLogin() throws ConfigurationException {
        RestAssured.baseURI="https://dmoney.roadtocareer.net";
        Response res= given().contentType("application/json").body("{\n" +
                "    \"email\":\"admin@dmoney.com\",\n" +
                "    \"password\":\"1234\"\n" +
                "}").when().post("/user/login");

//        System.out.println(res.asString());

        JsonPath jsonObj=res.jsonPath();
        String token= jsonObj.get("token");
        System.out.println("Token: "+token);

        Utils.setEnv("token",token);

    }

    @Test
    public void searchUser() throws IOException {

        RestAssured.baseURI="https://dmoney.roadtocareer.net";
        Response res= given().contentType("application/json").header("Authorization","bearer "+prop.getProperty("token"))
                .when().get("/user/search/id/28603");
        System.out.println(res.asString());

    }

    @Test
    public void createUser(){

        RestAssured.baseURI="https://dmoney.roadtocareer.net";
        Response res=given().contentType("application/json")
                .header("Authorization","bearer "+prop.getProperty("token"))
                .header("X-AUTH-SECRET-KEY",prop.getProperty("partnerKey"))
                .body("{\n" +
                        "  \"name\": \"kamal Khan\",\n" +
                        "  \"email\": \"kamal123@gmial.com\",\n" +
                        "  \"password\": \"12345\",\n" +
                        "  \"phone_number\": \"01723626335\",\n" +
                        "  \"nid\": \"12671234\",\n" +
                        "  \"role\": \"Customer\"\n" +
                        "}")
                .when().post("/user/create");
        System.out.println(res.asString());

    }

}
