package org.j2os.service;

import org.j2os.entity.Car;
import org.j2os.entity.Person;
import org.j2os.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Scope("prototype")
public class PersonService {

    @Autowired
    private PersonRepository repo;

    @Autowired
    private CarService carService;

    @Autowired
    private AuditService auditService;

    @Autowired
    private HelperService helperService;

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void SavePerson(Person person){

        for (Car car : person.getCarList()){
            car.setPerson(person);
//            carService.saveCar(car);
        }

        // Person ذخیره می‌شود
        repo.save(person);

        // همه‌ی ماشین‌های شخص ذخیره می‌شوند
//        person.getCarList().forEach(carService::saveCar);//با این کد کار نکرد
//با این کد کار نکرد
//        list.forEach(car -> {
//            carService.saveCar(car);
//        });


        auditService.saveAudit("Person saved: " + person.getName()); // REQUIRES_NEW

        helperService.doMandatoryWork();// MANDATORY
        helperService.doSupportsWork();  // SUPPORTS
        //helperService.doNestedWork();    // NESTED (depends on DB)
//        helperService.doNeverWork();     // NEVER → خطا داخل تراکنش
    }

    @Transactional(readOnly = true)
    public List<Person> findAll(){
        return repo.findAll();
    }
}
