package io.blackdeer.springdatajpa.person;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PersonService {

    private static final Logger logger = LoggerFactory.getLogger(PersonService.class);

    private final PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Person getPersonOrNullById(@NonNull Long id) {
        assert (id != null);
        Optional<Person> personOptional = personRepository.findById(id);
        if (personOptional.isPresent()) {
            return personOptional.get();
        } else {
            logger.error(String.format("id:%d인 Person이 없습니다!", id));
            return null;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public Person savePerson(@NonNull Person person) {
        assert (person != null);
        return personRepository.save(person);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public List<Person> savePersons(@NonNull Iterable<Person> persons) {
        assert (persons != null);
        return personRepository.saveAll(persons);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void deletePerson(@NonNull Person person) {
        assert (person != null);
        personRepository.delete(person);
    }
}
