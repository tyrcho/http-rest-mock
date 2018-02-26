import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.glassfish.jersey.process.Inflector;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.model.Resource;
import org.glassfish.jersey.server.model.Resource.Builder;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

import static org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory.createHttpServer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LaunchServer {
    public static void main(String[] args) {
        URI uri = UriBuilder.fromPath("/")
                .host("localhost")
                .port(12345)
                .scheme("http")
                .build();

        ResourceConfig rc = new ResourceConfig();
        registerJackson(rc);
        // test at http://localhost:12345/test/hello?arg=55
        rc.register(simpleResource("/test", "/hello"));
        // test http://localhost:12345/test/data or http://localhost:12345/test/hello
        rc.register(mockedService());


        createHttpServer(uri, rc, true);
    }

    private static MyService mockedService() {
        MyService myService = mock(MyService.class);
        when(myService.sayHello()).thenReturn("OK");
        when(myService.checkData()).thenReturn(new TestObject("mocked value"));
        return myService;
    }

    // This is mandatory to have JSON serialization of POJOs returned by our interface
    private static void registerJackson(ResourceConfig rc) {
        JacksonJaxbJsonProvider provider = new JacksonJaxbJsonProvider();
        provider.setMapper(new ObjectMapper());
        rc.register(provider);
    }

    private static Resource simpleResource(String path, String relativePath) {
        Builder builder = Resource.builder(path);

        // cannot be replaced by a lambda, grizzly server won't handle request
        Inflector<ContainerRequestContext, Object> inflector = new Inflector<ContainerRequestContext, Object>() {
            @Override
            public Object apply(ContainerRequestContext context) {
                return Response.ok("Hello " + context.getUriInfo().getQueryParameters().get("arg")).build();
            }
        };
        builder.addChildResource(relativePath)
                .addMethod("GET")
                .handledBy(inflector);

        return builder.build();
    }
}


