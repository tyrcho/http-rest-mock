import org.glassfish.jersey.spi.Contract;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Contract
@Path("mock")
@Singleton
public interface MyService {
    @GET
    @Path("hi")
    @Produces("text/plain")
    String sayHello();

    @GET
    @Path("data")
    @Produces("application/json")
    TestObject checkData();
}