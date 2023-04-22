package hr.kpastorcic11.demobank.models;


import jakarta.persistence.*;


@Entity
@Table(name = "persons")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String oib;

    @Column(nullable = false)
    private Status status = Status.INACTIVE;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getOib() {
        return oib;
    }

    public Status getStatus() {
        return status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setOib(String oib) {
        this.oib = oib;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Person copyDetails(PersonDTO personDetails) {
        setLastName(personDetails.getLastName());
        setName(personDetails.getName());
        setOib(personDetails.getOib());
        return this;
    }

    public enum Status {
        ACTIVE, INACTIVE
    }
}


