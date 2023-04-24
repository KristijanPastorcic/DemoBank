package hr.kpastorcic11.demobank.controllers;

import hr.kpastorcic11.demobank.models.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/status")
public class StatusController {

    private final JdbcTemplate jdbcTemplate;
    private static final String SELECT_PERSON = "SELECT * FROM persons WHERE oib = ?";
    private static final String INSERT_FILE = "INSERT INTO files(file_name, fk_oib) VALUES (?, ?)";
    private static final String SELECT_FILE = "SELECT file_name FROM files";

    public StatusController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/customer/{oib}")
    public ResponseEntity<?> getPersonByOIB(@PathVariable String oib) {
        try {
            Person person = getPersonByOibJDBC(oib);
            person.setStatus(Person.Status.ACTIVE);
            String path = writeToFile(oib, person);
            jdbcTemplate.update(INSERT_FILE, path, person.getOib());
            log.info(oib + " status set to ACTIVE");
            return ResponseEntity.ok(person);
        } catch (DataAccessException e) {
            log.error("Person with OIB: " + oib + " not found");
            e.printStackTrace();
            return ResponseEntity.badRequest().body("oib: " + oib + " does not exist");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/file/{oib}")
    public ResponseEntity<?> activateFile(@PathVariable String oib, @RequestParam String status) {
        try {
            log.info("received request for status update: " + oib + ", " + status);
            Person.Status fileStatus = Person.Status.valueOf(status.toUpperCase());
            Person person = getPersonByOibJDBC(oib);
            if (fileStatus.equals(Person.Status.ACTIVE) && person.getStatus() == Person.Status.ACTIVE) {
                return ResponseEntity.badRequest().body("Person with OIB: " + oib + " has an active file");
            }

            person.setStatus(Person.Status.ACTIVE);
            List<String> fileNames = getFileNames();
            return ResponseEntity.ok(fileNames);
        } catch (DataAccessException e) {
            log.error("Person with OIB: " + oib + " not found");
            e.printStackTrace();
            return ResponseEntity.badRequest().body("oib: " + oib + " does not exist");
        } catch (IllegalArgumentException e) {
            log.error("Person with OIB: " + oib + " not found");
            e.printStackTrace();
            return ResponseEntity.badRequest().body("status: " + status + " unsupported");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    private List<String> getFileNames() {
        try {
            List<String> fileNames = new ArrayList<>();
            jdbcTemplate.query(SELECT_FILE, rs -> {
                String fileName = rs.getString("file_name");
                fileNames.add(fileName);
            });
            return fileNames;
        } catch (DataAccessException e) {
            return Collections.emptyList();
        }
    }

    private Person getPersonByOibJDBC(String oib) {
        return jdbcTemplate.queryForObject(SELECT_PERSON,
                (rs, rowNum) -> new Person(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("last_name"),
                        rs.getString("oib"),
                        Person.Status.valueOf(rs.getString("status")))
                ,
                oib);
    }

    private static String writeToFile(String oib, Person person) throws IOException {
        String dirName = "files";
        File dir = new File(Paths.get("").toAbsolutePath() + File.separator + dirName);
        if (!dir.exists()) dir.mkdir();

        String fileName = dirName + File.separator + oib;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyyHHss");
        String timeStamp = LocalDateTime.now().format(formatter);
        File file = new File(fileName.concat(timeStamp).concat(".txt"));
        if (file.createNewFile()) {
            log.info("Created new file at: " + file.getAbsoluteFile());
            try (FileWriter fileWriter = new FileWriter(file)) {
                fileWriter.write(person.toFileLine());
            }
        }
        return file.getPath();
    }

}
