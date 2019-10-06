package  entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@NamedQueries({
    @NamedQuery(name = "Person.deleteAllRows", query = "DELETE from Person"),
    @NamedQuery(name = "Person.getAll", query = "SELECT p FROM Person p"),
    //@NamedQuery(name = "Person.getByPhone", query = "SELECT p FROM Person p WHERE p.phone = :phone"),
    @NamedQuery(name = "Person.count", query = "SELECT count(p) FROM Person p")})

public class Person implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PERSON_ID")
    private int id;
    @Column(name = "FIRSTNAME")
    private String firstName;
    @Column(name = "LASTNAME")
    private String lastName;
    @Column(name = "EMAIL")
    private String email;
    
    @ManyToMany (cascade = CascadeType.PERSIST)
    private List<Hobby> hobbyCollection;
    
    
    @OneToMany(mappedBy = "person", cascade=CascadeType.PERSIST)
    private List<Phone> phoneCollection;  
    
    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(name="ADDRESS_ID")
    private Address address;
    
    public Person() {
    }

    public Person(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneCollection = new ArrayList();
        this.hobbyCollection = new ArrayList();
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Hobby> getHobbyCollection() {
        return hobbyCollection;
    }

    public void addHobby(Hobby hob) {
        this.hobbyCollection.add(hob);
    }

    public List<Phone> getPhoneCollection() {
        return phoneCollection;
    }

    public void addPhone(Phone phone) {
        this.phoneCollection.add(phone);
    }

    public Address getAddress() {
        return address;
    }

    public void addAddress(Address address) {
        this.address = address;
        address.getPersons().add(this);
    }

    @Override
    public String toString() {
        return "Person{" + "id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + '}';
    }
}
