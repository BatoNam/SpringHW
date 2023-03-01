package ru.pflb.person.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.pflb.person.entity.Person;
import ru.pflb.person.service.PersonService;

import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

@RestController
public class PersonController {
    private final PersonService personService;
    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }
    @GetMapping(path = "/greeting/{name}/{age}")
    public String greeting(@PathVariable String name, @PathVariable int age) {
        return String.format("Hello, my name is %s, and I'am %d years old", name, age);
    }

    @PostMapping(path = "/person")
    public Person getPerson(@RequestBody String jsonString) throws IOException {

        StringReader reader = new StringReader(jsonString);

        ObjectMapper mapper = new ObjectMapper();

        Person person = mapper.readValue(reader, Person.class);

        System.out.println(person.getAge());
        System.out.println(person.getName());
        if (person.getAge() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Неправильно введен возраст: он должен быть неотрицательным числом");
        } else if (person.getName() == "") {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Имя пользователя не может быть пустой строкой");
        }
        return person;
    }

    @PostMapping(path = "/create")
    public String createPerson() {
        return personService.createPerson();
    }

    @PutMapping(path = "/update")
    public void updatePerson(@RequestBody Person person) {
        personService.updatePerson(person);
    }

    @PostMapping(path = "/delete/{id}")
    public void removePerson(@PathVariable int id) {
        personService.removePerson(id);
    }
}
