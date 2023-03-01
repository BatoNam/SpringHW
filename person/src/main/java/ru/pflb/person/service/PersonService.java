package ru.pflb.person.service;

import ru.pflb.person.entity.Person;

public interface PersonService {
    public String createPerson();

    public void removePerson(int id);

    public void updatePerson(Person person);
}
