package com.loosie.javaallinone.project3.mycontact.service;

import com.loosie.javaallinone.project3.mycontact.domain.Block;
import com.loosie.javaallinone.project3.mycontact.domain.Person;
import com.loosie.javaallinone.project3.mycontact.repository.BlockRepository;
import com.loosie.javaallinone.project3.mycontact.repository.PersonRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;


@SpringBootTest
class PersonServiceTest {

    @Autowired
    private PersonService personService;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private BlockRepository blockRepository;

    @Test
    void getPeopleExcludeBlocks(){
        givenPeople();
        givenBlocks();

        List<Person> result = personService.getPeopleExcludeBlocks();

        result.forEach(System.out::println);

    }

    @Test
    void getPeopleByName(){
        givenPeople();

        List<Person> result = personService.getPeopleByName("martin");

        result.forEach(System.out::println);
    }

    @Test
    void cascadeTest(){
        givenPeople();

        List<Person> result = personRepository.findAll();
        result.forEach(System.out::println);

        Person person =result.get(3);
        person.getBlock().setStartDate(LocalDate.now());
        person.getBlock().setEndDate(LocalDate.now());

        personRepository.save(person);
        personRepository.findAll().forEach(System.out::println);

        person.setBlock(null);
        personRepository.save(person);
        personRepository.findAll().forEach(System.out::println);
        blockRepository.findAll().forEach(System.out::println);

    }

    @Test
    void getPerson(){
        givenPeople();

        Person person = personService.getPerson(3L);

        System.out.println(person);
    }


    private void givenPeople() {
        givenPerson("martin",10,"A");
        givenPerson("david",8,"B");
        givenBlockPerson("demis",11,"O");
        givenBlockPerson("martin",12 ,"AB");
    }

    private void givenBlocks() {
        givenBlock("martin");
    }

    private void givenPerson(String name, int age, String bloodType) {
        personRepository.save(new Person(name,age,bloodType));
    }

    private void givenBlockPerson(String name, int age, String bloodType){
        Person blockPerson = new Person(name,age,bloodType);
        blockPerson.setBlock(new Block(name));

        personRepository.save(blockPerson);
    }


    private Block givenBlock(String name) {
        return blockRepository.save(new Block(name));
    }
}