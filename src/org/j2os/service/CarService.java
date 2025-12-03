package org.j2os.service;

import org.j2os.entity.Car;
import org.j2os.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CarService {

    @Autowired
    private CarRepository repo;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveCar(Car car){
        repo.save(car);
    }
}
