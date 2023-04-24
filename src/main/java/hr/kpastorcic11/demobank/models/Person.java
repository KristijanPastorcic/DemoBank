package hr.kpastorcic11.demobank.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@Entity
@AllArgsConstructor
@NoArgsConstructor
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

    @Enumerated(EnumType.STRING)
    @Column(length = 8)
    private Status status = Status.INACTIVE;

    public String toFileLine() {
        return name + ", " + lastName + ", " + oib + ", " + status;
    }

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

    public Person copyDetails(ValidatedPersonDTO validatedPersonDTO) {
        setLastName(validatedPersonDTO.getLastName());
        setName(validatedPersonDTO.getName());
        setOib(validatedPersonDTO.getOib());
        setStatus(Status.valueOf(validatedPersonDTO.getStatus()));
        return this;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", oib='" + oib + '\'' +
                ", status=" + status +
                '}';
    }

    public enum Status {
        INACTIVE, ACTIVE
    }


}


