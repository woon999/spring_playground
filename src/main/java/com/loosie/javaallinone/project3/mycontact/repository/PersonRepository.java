package com.loosie.javaallinone.project3.mycontact.repository;

import com.loosie.javaallinone.project3.mycontact.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PersonRepository extends JpaRepository<Person,Long> {

    List<Person> findByName(String name);

    List<Person> findByBlockIsNull();

    List<Person> findByBloodType(String bloodType);

    List<Person> findByBirthdayBetween(LocalDate startDate, LocalDate endDate);
}
