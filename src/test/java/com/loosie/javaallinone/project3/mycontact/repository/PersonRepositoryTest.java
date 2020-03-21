package com.loosie.javaallinone.project3.mycontact.repository;

import com.loosie.javaallinone.project3.mycontact.domain.Person;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.predicate;


@SpringBootTest
class PersonRepositoryTest {

    @Autowired
    private PersonRepository personRepository;

    @Test
    void curd() {
        Person person = new Person();
        person.setName("martin");
        person.setAge(10);
        person.setBloodType("A");

        personRepository.save(person);

        System.out.println(personRepository.findAll());

        List<Person> people = personRepository.findAll();

        assertThat(people.size()).isEqualTo(1);
        assertThat(people.get(0).getName()).isEqualTo("martin");
        assertThat(people.get(0).getAge()).isEqualTo(10);
        assertThat(people.get(0).getBloodType()).isEqualTo("A");
    }


    @Test
    void hashCodeAndEquals(){
        Person person1 = new Person("martin",10,"A");
        Person person2 = new Person("martin",10,"A");

        System.out.println(person1.equals(person2));
        System.out.println(person1.hashCode());
        System.out.println(person2.hashCode());

        Map<Person,Integer> map = new HashMap<>();
        map.put(person1,person1.getAge());

        System.out.println(map);
        System.out.println(map.get(person2));
    }

    @Test
    void findByBloodType(){
        givenPerson("martin", 12, "A");
        givenPerson("david", 10, "B");
        givenPerson("dennis", 8, "O");
        givenPerson("sophia", 6, "AB");
        givenPerson("benny", 16, "A");
        givenPerson("john", 13, "A");


        List<Person> result = personRepository.findByBloodType("A");

        result.forEach(System.out::println);

    }

    @Test
    void findByBirthdayBetween(){
        givenPerson("martin", 12, "A",LocalDate.of(1991,9,10));
        givenPerson("david", 10, "B",LocalDate.of(1992,11,20));
        givenPerson("dennis", 8, "O",LocalDate.of(1993,5,12));
        givenPerson("sophia", 6, "AB",LocalDate.of(1994,3,23));
        givenPerson("benny", 16, "A",LocalDate.of(1995,2,5));

        List<Person> result = personRepository.findByBirthdayBetween(
                LocalDate.of(1991,9,10),LocalDate.of(1995,9,10));

        result.forEach(System.out::println);

    }

    private void givenPerson(String name, int age, String bloodType){
        givenPerson(name,age,bloodType,null);
    }

    private void givenPerson(String name, int age, String bloodType, LocalDate birthday){

        Person person = new Person(name, age, bloodType);
        person.setBirthday(birthday);
        personRepository.save(person);

    }
}