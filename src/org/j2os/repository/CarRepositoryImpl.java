package org.j2os.repository;

import org.j2os.entity.Car;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class CarRepositoryImpl implements CarRepository{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void save(Car car) {
        entityManager.persist(car);
    }
}
