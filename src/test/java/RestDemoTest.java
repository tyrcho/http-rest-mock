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

        // TODO https://gitlab.kazan.priv.atos.fr/kazan/kazan-cutewizard/blob/master/testing/src/main/java/com/worldline/cutewizard/tests/web/Grizzly2Listener.java#L57

        SimpleHttpServer httpServer = new SimpleHttpServer(
                "localhost",
                12345,
                "http");
        httpServer.register(myService);
        httpServer.start();

        RestAssured.baseURI = "http://localhost:12345/mock";
        when().
                get("/data").
                then().
                statusCode(200).
                body("testProp", equalTo("mocked value"));
    }
}