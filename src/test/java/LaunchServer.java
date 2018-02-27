import org.glassfish.jersey.server.model.Resource;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;

public class LaunchServer {
    public static void main(String[] args) {
        MyService myService = mock(MyService.class);
        // http://localhost:12345/mock/hi
        Mockito.when(myService.sayHello()).thenReturn("OK");
        // http://localhost:12345/mock/data
        Mockito.when(myService.checkData()).thenReturn(new TestObject("mocked value"));
        // http://localhost:12345/test/hello?arg=55
        Resource resource = ServiceBuilder.simpleResource("/test", "/hello", context -> "Hello " + context.getUriInfo().getQueryParameters().get("arg"));
        startServer("localhost", 12345, "http", resource, myService);
    }

    public static void startServer(String host, int port, String scheme, Resource resource, MyService service) {
        SimpleHttpServer httpServer = new SimpleHttpServer(host, port, scheme);
        httpServer.register(resource);
        httpServer.register(service);
        httpServer.start();
    }

}


