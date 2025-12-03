package org.j2os;

import org.j2os.common.Spring;
import org.j2os.entity.Car;
import org.j2os.entity.Person;
import org.j2os.service.PersonService;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        PersonService service = (PersonService) Spring.getBean("personService");

        Person person1 = Person.builder()
                .name("Monireh11")
                .family("Jamshidi11")
                .carList(Arrays.asList(
                        Car.builder().model("BMW11").build(),
                        Car.builder().model("BENZ11").build()
                ))
                .build();
//        List<Car> carList = Arrays.asList(
//                        Car.builder().model("BMW44").build(),
//                        Car.builder().model("BENZ44").build()
//                );

//        service.SavePerson(person1);

        System.out.println("---- LIST ----");

        //با این کد کار نکرد
//        service.findAll().forEach(person -> {
//            System.out.println(person.getName() + " " + person.getFamily());
//            person.getCarList()
//                    .forEach(c -> System.out.println(" - car: " + c.getModel()));
//        });
        for (Person person : service.findAll()){
            System.out.println(person.getName() + " " + person.getFamily());

            for (Car car: person.getCarList()){
                System.out.println(" - car: " + car.getModel());
            }
        }
    }
}
