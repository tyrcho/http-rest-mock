import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.model.Resource;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

import static org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory.createHttpServer;

public class SimpleHttpServer {
    final ResourceConfig rc = new ResourceConfig();
    private final URI uri;

    public SimpleHttpServer(String host, int port, String scheme) {
        this.uri = getUri(host, port, scheme);
        registerJackson();
    }

    public void start() {
        createHttpServer(uri, rc, true);
    }

    // This is mandatory to have JSON serialization of POJOs returned by our interface
     void registerJackson() {
        JacksonJaxbJsonProvider provider = new JacksonJaxbJsonProvider();
        provider.setMapper(new ObjectMapper());
        rc.register(provider);
    }

    static URI getUri(String host, int port, String scheme) {
        return UriBuilder.fromPath("/")
                .host(host)
                .port(port)
                .scheme(scheme)
                .build();
    }

    public void register(MyService service) {
        rc.register(service);
    }

    public void register(Resource resource) {
        rc.registerResources(resource);
    }
}
