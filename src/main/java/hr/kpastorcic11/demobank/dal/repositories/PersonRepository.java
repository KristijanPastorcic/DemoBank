package hr.kpastorcic11.demobank.dal.repositories;

import hr.kpastorcic11.demobank.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Integer> {
}