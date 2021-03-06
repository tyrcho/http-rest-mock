import org.glassfish.jersey.process.Inflector;
import org.glassfish.jersey.server.model.Resource;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Response;
import java.util.function.Function;

public class ServiceBuilder {
    public static Resource simpleResource(String path, String relativePath, final Function<ContainerRequestContext, String> arg) {
        Resource.Builder builder = Resource.builder(path);

        // cannot be replaced by a lambda, grizzly server won't handle request
        Inflector<ContainerRequestContext, Object> inflector = new Inflector<ContainerRequestContext, Object>() {
            @Override
            public Object apply(ContainerRequestContext context) {
                return Response.ok(arg.apply(context)).build();
            }
        };
        builder.addChildResource(relativePath)
                .addMethod("GET")
                .handledBy(inflector);

        return builder.build();
    }

}
