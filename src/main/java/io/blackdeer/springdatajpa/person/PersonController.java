package io.blackdeer.springdatajpa.person;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@RestController
@RequestMapping("/person")
public class PersonController {

    private static final Logger logger = LoggerFactory.getLogger(PersonController.class);

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @ExceptionHandler({MissingPathVariableException.class})
    public ResponseEntity handleException(MissingPathVariableException e) {
        logger.warn(e.getMessage(), e);
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/insert")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ResponseEntity insertPerson(@RequestParam String name,
                                       @RequestParam Gender gender,
                                       @RequestParam int year,
                                       @RequestParam int month,
                                       @RequestParam int dayOfMonth,
                                       @RequestParam(required = false, name = "memo") String memoOrNull) {
        if (name.length() < 1 || name.length() > 10 || year < 0 || month < 1 || month > 12 || dayOfMonth < 1 || dayOfMonth > 31) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        Person person = new Person(name, gender, year - OffsetDateTime.now().getYear(), Date.from(OffsetDateTime.of(year, month, dayOfMonth, 0, 0, 0, 0, ZoneOffset.ofHours(9)).toInstant()), memoOrNull, null);
        Person savePerson = personService.savePerson(person);
        if (savePerson.equals(person)) {
            return new ResponseEntity(person, HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*
        org.springframework.data.repository.support.DomainClassConverter가
        id를 Entity로 변환하여 줌.
        만약 해당 id에 맞는 데이터가 없다면, org.springframework.web.bind.MissingPathVariableException이 발생함.
     */
    @GetMapping("/get/{id}")
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public ResponseEntity getOne(@PathVariable(value = "id") Person person) {
        return new ResponseEntity(person, HttpStatus.OK);
    }
}
