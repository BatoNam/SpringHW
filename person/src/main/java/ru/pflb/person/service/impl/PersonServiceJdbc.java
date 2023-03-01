package ru.pflb.person.service.impl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.pflb.person.entity.Person;
import ru.pflb.person.service.PersonService;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Random;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PersonServiceJdbc implements PersonService {
    private final JdbcTemplate jdbcTemplate;



    @Override
    public String createPerson() {
        Random random = new Random(System.currentTimeMillis());
        Person person = new Person();
        int age = random.nextInt(100);
        person.setAge(age);

        int leftASCII = 97;
        int rightASCII = 122;
        int nameLength = random.nextInt(3,10);
        String name = random.ints(leftASCII, rightASCII+1)
                .limit(nameLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint,StringBuilder::append)
                .toString();
        person.setName(name);

        UUID uuid = UUID.randomUUID();
        Object[] params = {name,age,uuid};

        jdbcTemplate.update(
                "INSERT INTO hw_person.person (name, age, uuid) VALUES (?, ?, ?);", params
        );

        String getIdQuery = "SELECT id FROM hw_person.person WHERE uuid = ?";
        int id = jdbcTemplate.queryForObject(getIdQuery, new Object[] { uuid }, int.class);
        person.setId(id);

        ObjectMapper mapper = new ObjectMapper();
        StringWriter writer = new StringWriter();
        try {
            mapper.writeValue(writer, person);
        }
        catch (IOException e) {
            e.printStackTrace(System.out);
        }
        return writer.toString();
    }



    @Override
    public void removePerson(int id) {
        String deletePersonQuery = "DELETE FROM hw_person.person WHERE id = ?";
        jdbcTemplate.update(deletePersonQuery, new Object[] {id});
    }

    @Override
    public void updatePerson(Person person) {
        String updateNameAgeQuery = "UPDATE hw_person.person SET name = ?, age = ? WHERE id = ?";
        String updateNameQuery = "UPDATE hw_person.person SET name = ? WHERE id = ?";
        String updateAgeQuery = "UPDATE hw_person.person SET age = ? WHERE id = ?";
        Object[] paramsNameAge = {person.getName(), person.getAge(), person.getId()};
        Object[] paramsName = {person.getName(), person.getId()};
        Object[] paramsAge = {person.getAge(), person.getId()};
        if (person.getName() != null || person.getAge() != -1) {
            try {
                if (person.getAge() == -1) {
                    jdbcTemplate.update(updateNameQuery, paramsName);
                } else if (person.getName() == null) {
                    jdbcTemplate.update(updateAgeQuery, paramsAge);
                } else {
                    jdbcTemplate.update(updateNameAgeQuery, paramsNameAge);
                }
                // debug
                int age = jdbcTemplate.queryForObject("select age from hw_person.person where id = ?", new Object[]{person.getId()}, int.class);
                System.out.println("new age = " + age);
                String name = jdbcTemplate.queryForObject("select name from hw_person.person where id = ?", new Object[]{person.getId()}, String.class);
                System.out.println("new name = " + name);
            } catch (EmptyResultDataAccessException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Нет такого id");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Нечего менять");
        }
    }
}
