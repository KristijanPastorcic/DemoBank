package hr.kpastorcic11.demobank.controllers;

import hr.kpastorcic11.demobank.dal.repositories.PersonRepository;
import hr.kpastorcic11.demobank.models.Person;
import hr.kpastorcic11.demobank.models.PersonDTO;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/persons")
public class BankController {
    private final PersonRepository personRepository;

    public BankController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createPerson(@Valid @RequestBody PersonDTO personDTO, BindingResult bindingResult) {
        ResponseEntity<String> responseEntity = getResponseEntity(bindingResult, personDTO);
        if (responseEntity.getStatusCode() == HttpStatus.BAD_REQUEST)
            return responseEntity;

        Person newPerson = personRepository.save(new Person().copyDetails(personDTO));
        log.info("New person created: " + newPerson);
        return ResponseEntity
                .created(URI.create("/persons/" + newPerson.getId()))
                .build();
    }

    @GetMapping("/retrieve")
    public List<Person> retrieveAllPersons() {
        return personRepository.findAll();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updatePerson(@PathVariable int id, @Valid @RequestBody PersonDTO personDTO,
                                               BindingResult bindingResult) {

        ResponseEntity<String> responseEntity = getResponseEntity(bindingResult, personDTO);
        if (responseEntity.getStatusCode() == HttpStatus.BAD_REQUEST)
            return responseEntity;


        Optional<Person> optionalPerson = personRepository.findById(id);

        if (optionalPerson.isPresent()) {
            log.info("Person old data: " + optionalPerson.get());
            personRepository.save(optionalPerson.get().copyDetails(personDTO));
            log.info("Person new data: " + optionalPerson.get());
            return  ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private ResponseEntity<String> getResponseEntity(BindingResult bindingResult, PersonDTO personDTO) {
        if (bindingResult.hasErrors()) {
            ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.badRequest();
            bindingResult.getAllErrors()
                    .forEach(objectError -> {
                        log.error(personDTO + " Validation error: " + objectError.getDefaultMessage());
                        bodyBuilder.header("Validation error", objectError.getDefaultMessage());
                    });
            return bodyBuilder.build();
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePerson(@PathVariable int id) {
        Optional<Person> optionalPerson = personRepository.findById(id);
        if (optionalPerson.isPresent()) {
            personRepository.deleteById(id);
            log.info("Person deleted: " + optionalPerson.get());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @ExceptionHandler(Exception.class)
    public void handleException(HttpServletResponse response, DataIntegrityViolationException e) {
        response.setStatus(500);
        response.setHeader("errorMessage", "Exception occurred!");
        e.printStackTrace();
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public void duplicateEmailException(HttpServletResponse response, DataIntegrityViolationException e) {
        response.setStatus(409);
        response.setHeader("errorMessage", "Data constraint violated");
        e.printStackTrace();
    }
}
