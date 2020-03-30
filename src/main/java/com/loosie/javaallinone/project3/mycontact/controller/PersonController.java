package com.loosie.javaallinone.project3.mycontact.controller;


import com.loosie.javaallinone.project3.mycontact.controller.dto.PersonDto;
import com.loosie.javaallinone.project3.mycontact.domain.Person;
import com.loosie.javaallinone.project3.mycontact.exception.PersonNotFoundException;
import com.loosie.javaallinone.project3.mycontact.exception.RenameIsNotPermittedException;
import com.loosie.javaallinone.project3.mycontact.exception.dto.ErrorResponse;
import com.loosie.javaallinone.project3.mycontact.repository.PersonRepository;
import com.loosie.javaallinone.project3.mycontact.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping(value = "/api/person")
@RestController
@Slf4j
public class PersonController {

    @Autowired
    private PersonService personService;

    @GetMapping("/{id}")
    public Person getPerson(@PathVariable Long id) {
        return personService.getPerson(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void postPerson(@RequestBody @Valid PersonDto personDto) {
        personService.put(personDto);

    }

    @PutMapping("/{id}")
    public void modifyPerson(@PathVariable Long id,
                             @RequestBody PersonDto personDto) {
        personService.modify(id, personDto);
    }

    @PatchMapping("/{id}")
    public void modifyPerson(@PathVariable Long id, String name) {
        personService.modify(id, name);

    }

    @DeleteMapping("/{id}")
    public void deletePerson(@PathVariable Long id) {
        personService.delete(id);

    }


}
