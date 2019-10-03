package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import entities.Person;
import utils.EMF_Creator;
import facades.RegisterFacade;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

//Todo Remove or change relevant parts before ACTUAL use
@Path("person")
public class RegisterResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory(
            "pu",
            "jdbc:mysql://localhost:3307/startcode",
            "dev",
            "ax2",
            EMF_Creator.Strategy.CREATE);
    private static final RegisterFacade FACADE = RegisterFacade.getRegisterFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String demo() {
        return "{\"msg\":\"Hello World\"}";
    }

    @Path("count")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getPersonCount() {
        long count = FACADE.getPersonCount();
        //System.out.println("--------------->"+count);
        return "{\"count\":" + count + "}";  //Done manually so no need for a DTO
    }

    @Path("all")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getAllBooks() {
        List<Person> books = FACADE.getAllPersons();
        return GSON.toJson(books);
    }

    @Path("/{phone}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})

    public String getPersonByPhone(@PathParam("phone") String phone) {
        Person p = FACADE.findPersonswithPhoneNumber(phone);
        return GSON.toJson(p);
    }

    @Path("/populate")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String populate() {

        FACADE.populate();
        return "{\"msg\":\"Done\"}";
    }

}
