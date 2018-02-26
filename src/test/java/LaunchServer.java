import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.model.Resource;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

import static org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory.createHttpServer;
import static org.mockito.Mockito.mock;

public class LaunchServer {
    public static void main(String[] args) {
        startServer("localhost", 12345, "http", ServiceBuilder.simpleResource("/test", "/hello", context -> "Hello " + context.getUriInfo().getQueryParameters().get("arg")), RestDemoTest.mockedService());
    }

    public static void startServer(String host, int port, String scheme, Resource resource, MyService service) {
        URI uri = UriBuilder.fromPath("/")
                .host(host)
                .port(port)
                .scheme(scheme)
                .build();

        ResourceConfig rc = new ResourceConfig();
        ServiceBuilder.registerJackson(rc);
        rc
                // test at http://localhost:12345/test/hello?arg=55
                .registerResources(resource)
                // test at http://localhost:12345/mock/data or http://localhost:12345/mock/hi
                .register(service);


        createHttpServer(uri, rc, true);
    }

}


