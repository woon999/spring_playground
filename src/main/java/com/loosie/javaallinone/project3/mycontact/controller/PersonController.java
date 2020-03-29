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
    public void postPerson(@RequestBody PersonDto personDto) {
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

    @ExceptionHandler(value = RenameIsNotPermittedException.class)
    public ResponseEntity<ErrorResponse> handleRenameNoPermittedException(RenameIsNotPermittedException ex) {
        return new ResponseEntity<>(ErrorResponse.of(HttpStatus.BAD_REQUEST
                , ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = PersonNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePersonNotFoundException(PersonNotFoundException ex) {
        return new ResponseEntity<>(ErrorResponse.of(HttpStatus.BAD_REQUEST
                , ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        log.error("서버오류 : {}", ex.getMessage(), ex);
        return new ResponseEntity<>(ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR
                , "알 수 없는 서버 오류가 발생하였습니다."), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
