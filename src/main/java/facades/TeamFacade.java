package facades;

import DTO.*;
import com.google.gson.Gson;
import entities.Role;
import entities.Team;
import entities.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class TeamFacade {

    private static TeamFacade instance;
    private static EntityManagerFactory emf;
    private Gson gson = new Gson();
    private Object Team;

    //Private Constructor to ensure Singleton
    private TeamFacade() {}
    
    
    /**
     * 
     * @param _emf
     * @return an instance of this facade class.
     */

    public static TeamFacade getTeamFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new TeamFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public TeamDTO insertTeam(TeamDTO teamDTO) throws ExecutionException, InterruptedException {
        EntityManager em = emf.createEntityManager();

        System.out.println(teamDTO);
        Team teamToInsert = null;

        try {
                List<User> listOfUsers = null;

                em.getTransaction().begin();
                System.out.println("Beginning transaction");
                if (!teamDTO.getTeamName().equals("") &&
                    !teamDTO.getTeamPass().equals("") &&
                    !teamDTO.getUsers().equals(""))
                        {
                            /*System.out.println("adding users");
                            for (User u: teamDTO.getUsers()){
                            System.out.println("creating user");

                            new User(u.getUserName(), u.getUserPass());
                            u.addRole(new Role("user"));
                            listOfUsers.add(u);
                            System.out.println("adding user to list");*/

                            for (User u: teamDTO.getUsers()) {
                                u.setUserPass(teamDTO.getTeamPass());
                            }
                            

                            System.out.println("persisting to db");
                    teamToInsert = new Team(teamDTO.getTeamName(), teamDTO.getTeamPass(), teamDTO.getUsers());
                    em.merge(teamToInsert);
                    em.getTransaction().commit();
                } else {
                    System.out.println("missing team");
                }
            } finally {
                System.out.println("Closing DB connection");
                em.close();
            }
        return new TeamDTO(teamToInsert);
    }

    public List<TeamDTO> showTeamMembers(String teamname) {
        EntityManager em = emf.createEntityManager();

        List<TeamDTO> teamMembers;

        try {
            TypedQuery<TeamDTO> tq = em.createQuery("SELECT Team FROM Team t WHERE User = :TeamDTO", TeamDTO.class);
            tq.setParameter("TeamDTO", teamname);
            teamMembers = tq.getResultList();
        } finally {
            em.close();
        }
        return teamMembers;
    }
}

