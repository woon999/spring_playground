package com.loosie.javaallinone.project3.mycontact.service;

import com.loosie.javaallinone.project3.mycontact.domain.Person;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class PersonServiceTest {

    @Autowired
    private PersonService personService;


    @Test
    void getPeopleByName(){
        List<Person> result = personService.getPeopleByName("martin");

        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getName()).isEqualTo("martin");
    }


    @Test
    void getPerson(){
        Person person = personService.getPerson(3L);

        assertThat(person.getName()).isEqualTo("dennis");
    }

}