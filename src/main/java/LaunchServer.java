import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.process.Inflector;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.model.Resource;
import org.glassfish.jersey.server.model.Resource.Builder;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

public class LaunchServer {
    public static void main(String[] args) {
        URI uri = UriBuilder.fromPath("/")
                .host("localhost")
                .port(12345)
                .scheme("http")
                .build();

        final ResourceConfig rc = new ResourceConfig();
        rc.registerResources(simpleResource());
        GrizzlyHttpServerFactory.createHttpServer(uri, rc, true);


    }

    // test at http://localhost:12345/test/hello?arg=55
    private static Resource simpleResource() {
        Builder builder = Resource.builder("/test");

        // cannot be replaced by a lambda, grizzly server won't handle request
        Inflector<ContainerRequestContext, Object> inflector = new Inflector<ContainerRequestContext, Object>() {
            @Override
            public Object apply(ContainerRequestContext context) {
                return Response.ok("Hello " + context.getUriInfo().getQueryParameters().get("arg")).build();
            }
        };
        builder.addChildResource("/hello")
                .addMethod("GET")
                .handledBy(inflector);

        return builder.build();
    }
}


