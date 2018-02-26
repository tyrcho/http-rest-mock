import io.restassured.RestAssured;
import org.junit.Test;
import org.mockito.Mockito;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;


public class RestDemoTest {
    public static MyService mockedService() {
        MyService myService = mock(MyService.class);
        Mockito.when(myService.sayHello()).thenReturn("OK");
        Mockito.when(myService.checkData()).thenReturn(new TestObject("mocked value"));
        return myService;
    }

    @Test
    public void checkRest() {
        LaunchServer.startServer(
                "localhost",
                12345,
                "http",
                ServiceBuilder.simpleResource("/test", "/hello", context -> "Hello " + context.getUriInfo().getQueryParameters().get("arg")),
                mockedService());

        RestAssured.baseURI = "http://localhost:12345/mock";
        when().
                get("/data").
                then().
                statusCode(200).
                body("testProp", equalTo("mocked value"));
    }
}