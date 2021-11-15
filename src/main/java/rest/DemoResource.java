package rest;

import DTO.ChuckJokeJsonDTO;
import DTO.TeamDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.mysql.cj.xdevapi.JsonString;
import com.nimbusds.jose.shaded.json.JSONObject;
import entities.Team;
import entities.User;

import java.util.ArrayList;
import java.util.List;
import facades.TeamFacade;
import java.util.concurrent.ExecutionException;
import javax.annotation.security.RolesAllowed;
import javax.json.Json;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import facades.FacadeExample;
import jdk.nashorn.internal.parser.JSONParser;
import utils.EMF_Creator;

@Path("rs")
public class DemoResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final TeamFacade FACADE = TeamFacade.getTeamFacade(EMF);
    @Context
    private UriInfo context;
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
//    private Gson gson = new Gson();

    @Context
    SecurityContext securityContext;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getInfoForAll() {
        return "{\"msg\":\"Hello anonymous\"}";
    }

    //Just to verify if the database is setup
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("test")
    public String allUsers() {

        EntityManager em = EMF.createEntityManager();
        try {
            TypedQuery<User> query = em.createQuery ("select u from User u",entities.User.class);
            List<User> users = query.getResultList();
            return "[" + users.size() + "]";
        } finally {
            em.close();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("user")
    @RolesAllowed("user")
    public String getFromUser() {
        String thisuser = securityContext.getUserPrincipal().getName();
        return "{\"msg\": \"Hello to User: " + thisuser + "\"}";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("admin")
    @RolesAllowed("admin")
    public String getFromAdmin() {
        String thisuser = securityContext.getUserPrincipal().getName();
        return "{\"msg\": \"Hello to (admin) User: " + thisuser + "\"}";
    }

    @GET
    @Produces
    @Path("fiveServers")
    public String getDataFromFiveServers() throws ExecutionException, InterruptedException {
        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
        FacadeExample fc = FacadeExample.getFacadeExample(emf);

        List<List<Object>> responses = fc.getDataFromFiveServers();

        return gson.toJson(responses);
    }

    @POST
    @Path("createteam")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String createTeam(TeamDTO teamDTO) throws ExecutionException, InterruptedException {

        TeamDTO teamResponseDTO = null;

        if (teamDTO != null) {
            try {
                 teamResponseDTO = FACADE.insertTeam(teamDTO);
            } catch (Exception e) {
                System.out.println("Error inserting team");
            }
        }
        return gson.toJson(teamResponseDTO);
    }

    @GET
    @Path("team/{teamname}")
    @Produces({MediaType.APPLICATION_JSON})
    public String getTeamByName(@PathParam("teamname") String teamname) throws ExecutionException, InterruptedException {

        List<TeamDTO> teamsDTO = null;

        if (teamname != null) {
            try {
                teamsDTO = FACADE.showTeamMembers(teamname);
            } catch (Exception e) {
                System.out.println("Error finding team or members");
            }
        }
        return gson.toJson(teamsDTO);
    }

}
