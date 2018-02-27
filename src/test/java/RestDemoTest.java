import io.restassured.RestAssured;
import org.junit.Test;
import org.mockito.Mockito;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;


public class RestDemoTest {
    @Test
    public void checkRest() {
        MyService myService = mock(MyService.class);
        Mockito.when(myService.checkData()).thenReturn(new TestObject("mocked value"));

        SimpleHttpServer httpServer = new SimpleHttpServer(
                "localhost",
                "http");
        httpServer.register(myService);
        httpServer.start();

        RestAssured.baseURI = httpServer.uri + "mock";

        when().
                get("/data").
                then().
                statusCode(200).
                body("testProp", equalTo("mocked value"));
    }
}