/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

/**
 *
 * @author fskn
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Address.deleteAllRows", query = "DELETE from Address"),
    @NamedQuery(name = "Address.getAll", query = "SELECT p FROM Address p")})
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Integer id;
    @Column(name = "street")
    private String street;
    @Column(name = "additional_info")
    private String additionalinfo;

    
    @OneToMany(mappedBy = "address")
    private List <Person> persons = new ArrayList();
    @ManyToOne
    private CityInfo cityInfo;
    
    public Address() {
    }

    public Address(String street, String additionalinfo) {
        this.street = street;
        this.additionalinfo = additionalinfo;
        this.persons = new ArrayList();

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getAdditionalinfo() {
        return additionalinfo;
    }

    public void setAdditionalinfo(String additionalinfo) {
        this.additionalinfo = additionalinfo;
    }

    public List<Person> getPersons() {
        return persons;
    }

    public void addPerson(Person person) {
        this.persons.add(person);
        person.setAddress(this);
    }

    public CityInfo getCityInfo() {
        return cityInfo;
    }

    public void setCityInfo(CityInfo cityInfo) {
        this.cityInfo = cityInfo;
    }
    
    

//    @Override
//    public String toString() {
//        return "Address{" + "id=" + id + ", street=" + street + ", additionalinfo=" + additionalinfo + ", persons=" + persons + '}';
//    }

}
