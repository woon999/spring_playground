package com.loosie.javaallinone.project3.mycontact.service;


import com.loosie.javaallinone.project3.mycontact.controller.dto.PersonDto;
import com.loosie.javaallinone.project3.mycontact.domain.Person;
import com.loosie.javaallinone.project3.mycontact.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class PersonService {

    @Autowired
    private PersonRepository personRepository;


    public List<Person> getPeopleByName(String name){
          return personRepository.findByName(name);
    }

    @Transactional(readOnly = true)
    public Person getPerson(Long id){
        return personRepository.findById(id).orElse(null);
    }

    @Transactional
    public void put(PersonDto personDto){
        Person person = new Person();
        person.set(personDto);
        person.setName(personDto.getName());

        personRepository.save(person);
    }

    @Transactional
    public void modify(Long id, PersonDto personDto) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("아이디가 존재하지 않습니다."));

        if(!person.getName().equals(personDto.getName())){
            throw new RuntimeException("이름이 다릅니다.");
        }

        person.set(personDto);

        personRepository.save(person);
    }

    @Transactional
    public void modify(Long id, String name){
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("아이디가 존재하지 않습니다!"));

        person.setName(name);

        personRepository.save(person);
    }

    @Transactional
    public void delete(Long id) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("아이디가 존재하지 않습니다."));

        person.setDeleted(true);

        personRepository.save(person);
    }
}
