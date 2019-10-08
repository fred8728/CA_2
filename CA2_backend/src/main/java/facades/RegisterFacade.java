package facades;

import dto.PersonDTO;
import entities.Address;
import entities.CityInfo;
import entities.Hobby;
import entities.Person;
import entities.Phone;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 *
 * Rename Class to a relevant name Add add relevant facade methods
 */

public class RegisterFacade {

    private static RegisterFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private RegisterFacade() {
    }

    /**
     *
     * @param _emf
     * @return an instance of this facade class.
     */
    public static RegisterFacade getRegisterFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new RegisterFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    //Virker - tjekket af simone d. 07/10-19
    public int getPersonCount() {
        EntityManager em = emf.createEntityManager();
        try {
            Query query = em.createNamedQuery("Person.count");
            return  Integer.parseInt(query.getSingleResult().toString());
        } finally {
            em.close();
        }
    }
       
    //Virker - Tjekket af simone d. 07/10-19
    public List<PersonDTO> getAllPersons() {
        EntityManager em = emf.createEntityManager();
        try {

            List <PersonDTO> getAll = em.createQuery("SELECT p Person FROM Person p ").getResultList();
            return getAll;

        } finally {
            em.close();
        }   
    }
    
    public List<PersonDTO> getAllPersonsByCity(String city) {
        EntityManager em = emf.createEntityManager();
        try {

            List <PersonDTO> getAll = em.createNamedQuery( "SELECT Person p FROM Person p JOIN c CTIY Where p.")
                    .setParameter("city", city).getResultList();
            return getAll;

        } finally {
            em.close();
        }   
    }
    
    //Virker - tjekket af simone d. 07/10-19
    public Person getPersonByPhone(int phone) {
        EntityManager em = emf.createEntityManager();
        try { 
            Person p =  em.createQuery("SELECT p FROM Person p JOIN p.phoneCollection ph WHERE ph.number=:number", Person.class)
                    .setParameter("number", phone).getSingleResult();
            return p;
        } finally {
            em.close();
        }
    }
    
    //Virker - tjekket a simone d. 07/10-19
    public List<Person> getAllPersonsWithHobby(String hobby){
        EntityManager em = emf.createEntityManager();
        try{ 
            Query query = em.createQuery("SELECT p FROM Person p JOIN p.hobbyCollection hobby where hobby.name=:name",Person.class)
                    .setParameter("name",hobby);
            return query.getResultList(); 
        }finally{
            em.close();
        }
    }

    //Virker - tjekket a simone d. 07/10-19
    public int getSpecificHobbyCount(String hobby){
        EntityManager em = emf.createEntityManager();
        try {
            Query query = em.createQuery("SELECT count(h) FROM Hobby h WHERE h.name=:name ",Hobby.class)
                    .setParameter("name", hobby);
            return  Integer.parseInt(query.getSingleResult().toString());
        } finally {
            em.close();
        }
    }
    
    //Virker - lavet af Simone d. 07/10-19
    public Person addPerson(Person p, Phone phone, Address add, CityInfo ci, Hobby hobby) {
        p.addHobby(hobby);
        p.addPhone(phone);
        add.addPerson(p);
        ci.addAddress(add);
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(p);
            em.persist(phone);
            em.persist(add);
            em.persist(ci);
            em.getTransaction().commit();
            return p;
        } finally {
            em.close();
        }
    }
    
    //Virker ikke - simone er i gang
    public Person deletePerson(int personId, int addId, int ciID) {
        EntityManager em = emf.createEntityManager();
        Person p = em.find(Person.class, personId);
        Address add = em.find(Address.class, addId);
        CityInfo ci = em.find(CityInfo.class, addId);
        Phone phone = em.find(Phone.class, p.getId());
        Hobby hobby = em.find(Hobby.class, p.getId());
        hobby.getPersons().remove(p);
        
        p.getHobbyCollection().remove(p);
        p.getPhoneCollection().remove(p);
        
        add.getPersons().remove(p);
        ci.getAddress().remove(p);
        try {
            em.getTransaction().begin();
            em.remove(p);
            em.remove(add);
            em.remove(ci);
            em.remove(phone);
            em.remove(hobby);
            em.getTransaction().commit();
        }finally {
            em.close();
        }
        return p;
    }
    
    
}
