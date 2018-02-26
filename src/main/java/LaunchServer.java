import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.process.Inflector;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.model.Resource;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LaunchServer {
    public static void main(String[] args) {
        SkyService skyService = mock(SkyService.class);
        when(skyService.getMessage()).thenReturn("hello");
        URI uri = UriBuilder.fromPath("/")
                .host("localhost")
                .port(12345)
                .scheme("http")
                .build();

        final ResourceConfig rc = new ResourceConfig();
        Resource.Builder builder = Resource.builder("/test");

        builder.addChildResource("/hello")
                .addMethod("GET")
                .handledBy(new Inflector<ContainerRequestContext, Response>() {
                    public Response apply(ContainerRequestContext context) {
                        Object aaargs = context.getUriInfo().getQueryParameters().get("arg");
                        return Response.ok("Hello " + aaargs).build();
                    }
                });

        rc.registerResources(builder.build());
//rc.register(skyService);
        GrizzlyHttpServerFactory.createHttpServer(uri, rc, true);


    }

    public interface SkyService {
        String getMessage();
    }
}


