package hr.kpastorcic11.demobank.models;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class PersonDTO {

    @NotNull(message = "Name must not be null")
    private String name;

    @NotNull(message = "Last name must not be null")
    private String lastName;

    @NotNull
    @Pattern(regexp = "\\d{11}", message = "OIB must be exactly 11 digits")
    @Column(unique = true)
    private String oib;
}
