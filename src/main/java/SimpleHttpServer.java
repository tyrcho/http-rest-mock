import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.model.Resource;

import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.URI;

import static org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory.createHttpServer;

public class SimpleHttpServer {
    final ResourceConfig rc = new ResourceConfig();
    final URI uri;

    /**
     * Finds a free port to listen to.
     */
    SimpleHttpServer(String host, String scheme) {
        this(host, findFreePort(), scheme);
    }

    private SimpleHttpServer(String host, int port, String scheme) {
        this.uri = getUri(host, port, scheme);
        registerJackson();
    }

    public void start() {
        System.out.println("Starting server, listening at:" + uri);
        createHttpServer(uri, rc, true);

    }

    // This is mandatory to have JSON serialization of POJOs returned by our interface
    void registerJackson() {
        JacksonJaxbJsonProvider provider = new JacksonJaxbJsonProvider();
        provider.setMapper(new ObjectMapper());
        rc.register(provider);
    }

    public void register(MyService service) {
        rc.register(service);
    }

    public void register(Resource resource) {
        rc.registerResources(resource);
    }

    private static int findFreePort() {

        try (ServerSocket socket = new ServerSocket(0)) {
            // setReuseAddress is a little trick to try to "reserve" the address
            socket.setReuseAddress(true);
            return socket.getLocalPort();

        } catch (final IOException e) {
            throw new RuntimeException("Could not start the server", e);
        }
    }

    private static URI getUri(String host, int port, String scheme) {
        return UriBuilder.fromPath("/")
                .host(host)
                .port(port)
                .scheme(scheme)
                .build();
    }


}
