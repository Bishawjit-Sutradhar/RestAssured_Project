import com.github.javafaker.Faker;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.commons.configuration.ConfigurationException;
import org.testng.Assert;
import org.testng.annotations.Test;

public class UserTestRunner extends SetUp{

    @Test(priority = 1,description = "User Login")
    public void doLogin() throws ConfigurationException {
        UserController userController=new UserController(prop);
        UserModel userModel=new UserModel();
        userModel.setEmail("admin@dmoney.com");
        userModel.setPassword("1234");
        Response res=userController.doLogin(userModel);

        JsonPath jsonObj=res.jsonPath();
        String token= jsonObj.get("token");
        Utils.setEnv("token",token);
        System.out.println(token);
    }

    @Test(priority = 2,description = "User create")
    public void createUser() throws ConfigurationException {
        UserController userController=new UserController(prop);
        UserModel userModel=new UserModel();

        Faker faker=new Faker();

        userModel.setName(faker.name().fullName());
        userModel.setEmail(faker.internet().emailAddress().toString());
        userModel.setPassword("1234");
        userModel.setPhone_number("0120"+Utils.randomNumberGenerate(1000000,9999999));
        userModel.setNid("1231390");
        userModel.setRole("Customer");
        Response res= userController.createUser(userModel);

//        System.out.println(faker.name().fullName());
//        System.out.println(faker.internet().emailAddress());
//        System.out.println(userModel.getPhone_number());

        JsonPath jsonObj=res.jsonPath();
        int userId= jsonObj.get("user.id");
        String userName=jsonObj.get("user.name");
        String userPassword=jsonObj.get("user.password");
        String userEmail=jsonObj.get("user.email");
        String userPhoneNumber=jsonObj.get("user.phone_number");
        String userRole=jsonObj.get("user.role");

        Utils.setEnv("Id",String.valueOf(userId));
        Utils.setEnv("Name",userName);
        Utils.setEnv("Password",userPassword);
        Utils.setEnv("Email",userEmail);
        Utils.setEnv("PhoneNumber",userPhoneNumber);
        Utils.setEnv("Role",userRole);

        System.out.println(res.asString());

        Assert.assertEquals(jsonObj.get("message"),"User created");


    }

    @Test(priority = 3,description = "User Search by Id")
    public void searchUserById() throws InterruptedException {
        UserController userController=new UserController(prop);
        Response res= userController.searchUser(prop.getProperty("Id"));
        System.out.println(res.asString());

        JsonPath jsonObj=res.jsonPath();
        Assert.assertEquals(jsonObj.get("message"),"User found");

    }

}
