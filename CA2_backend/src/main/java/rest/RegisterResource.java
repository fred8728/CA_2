package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.PersonDTO;
import entities.Person;
import utils.EMF_Creator;
import facades.RegisterFacade;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@OpenAPIDefinition(
        info = @Info(
                title = "Simple Register API",
                version = "0.1",
                description = "Simple API to get info about a registerd person.",
                contact = @Contact(name = "Gruppe 9", email = "cph-ao141@cphbusiness.dk")
        ),
        tags = {
            @Tag(name = "register", description = "API related to Movie Info")

        },
        servers = {
            @Server(
                    description = "For Local host testing",
                    url = "http://localhost:8080/startcode"
            )//,
//                    @Server(
//                            description = "Server API",
//                            url = "http://mydroplet"
//                    )

        }
)


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

    public List<PersonDTO> MakePersonDTOList(List<Person> p) {
        List<PersonDTO> realPeople = new ArrayList<>();
        for (int i = 0; i < p.size(); i++) {
            realPeople.add(FACADE.makeDTO(p.get(i).getId()));
        }
        return realPeople;
    }

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
    /* @Operation(summary = "Get all persons in a list",
            tags = {"persons"},
            responses = {
                     @ApiResponse(
                     content = @Content(mediaType = "application/json",schema = @Schema(implementation = PersonDTO.class))),
                    @ApiResponse(responseCode = "200", description = "All persons"),                       
                    @ApiResponse(responseCode = "400", description = "Persons not found")})*/
    public String getAllPersons() {
        List<Person> p = FACADE.getAllPersons();
        return GSON.toJson(MakePersonDTOList(p));
    }

    @Path("/get/phone{phone}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getPersonByPhone(@PathParam("phone") int phone) {
        Person p = FACADE.getPersonByPhone(phone);
        return GSON.toJson(FACADE.makeDTO(p.getId()));
    }

    @Path("{id}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getPersonDTO(@PathParam("id") int id) {
        PersonDTO p = FACADE.makeDTO(id);
        return GSON.toJson(p);
    }

    @Path("/insertdata")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String insertData() {

        FACADE.insertData();
        return "{\"msg\":\"Done\"}";
    }

    @Path("/get/{city}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getPersonByCity(@PathParam("city") String city) {
        List<Person> p = FACADE.getAllPersonsFromCity(city);
        return GSON.toJson(MakePersonDTOList(p));
    }

    @Path("/get/all/{hobbie}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getPersonByHobby(@PathParam("hobbie") String hobbie) {
        List<Person> p = FACADE.getPersonsWithSameHobby(hobbie);
        return GSON.toJson(MakePersonDTOList(p));
    }

    @Path("/get/count/{hobbie}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getHobbyCount(@PathParam("hobbie") String hobbie) {
        int p = FACADE.getSpecificHobbyCount(hobbie);

        return GSON.toJson("count : " + p);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public String editPerson(String personAsJson, @PathParam("id") int id) {

        Person pOrignal = FACADE.getPersonByID(id);
        Person NewPersonVal = GSON.fromJson(personAsJson, Person.class);
        pOrignal.setFirstName(NewPersonVal.getFirstName());
        //pOrignal.setPhone(NewPersonVal.getPhone());
        pOrignal.setAddress(NewPersonVal.getAddress());

        // makes that the value return is on a good json format
        return GSON.toJson(pOrignal);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String addPerson(String personAsJson) {
        PersonDTO personNew = GSON.fromJson(personAsJson, PersonDTO.class);
        PersonDTO p = FACADE.addPerson(personNew);
        return GSON.toJson(personNew);

    }

    //Delete er ikke testet endnu - 
    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @Path("{id}")
    public String deletePerson(@PathParam("id") int id){
        Person p = FACADE.getPersonByID(id);
        int id1 = p.getId();
        FACADE.deletePerson(id1);
        return "{}";
   }


}

