package sakuuj.learn.library.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sakuuj.learn.library.models.Person;
import sakuuj.learn.library.repositories.PersonRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PersonService {
    PersonRepository personRepo;

    @Autowired
    public PersonService(PersonRepository personRepo) {
        this.personRepo = personRepo;
    }

    public List<Person> findAll() {
        return personRepo.findAll();
    }

    public Optional<Person> findById(int id) {
        var ret = personRepo.findById(id);
        ret.ifPresent(person -> Hibernate.initialize(person.getBooks()));
        return ret;
    }

    public Optional<Person> findByName(String name) {
        return personRepo.findByName(name).stream().findAny();
    }

    @Transactional
    public Person save(Person person) {
        return personRepo.save(person);
    }

    @Transactional
    public void update(int id, Person updatedPerson) {
        updatedPerson.setId(id);
        personRepo.save(updatedPerson);
    }

    @Transactional
    public void deleteById(int id) {
        personRepo.deleteById(id);
    }
}
