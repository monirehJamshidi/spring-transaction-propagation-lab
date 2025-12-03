package org.j2os.repository;

import org.j2os.entity.Person;

import java.util.List;

public interface PersonRepository {
    void save(Person person);
    List<Person> findAll();
}
