package org.j2os.repository;

import org.j2os.entity.Person;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class PersonRepositoryImpl implements PersonRepository{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void save(Person person) {
        entityManager.persist(person);
    }

    @Override
    public List<Person> findAll() {
        return entityManager
                .createQuery("select distinct o from person o left join fetch o.carList", Person.class)
                .getResultList();
    }
}