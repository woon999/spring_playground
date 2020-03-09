package kr.co.loosie.foody.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User,Long> {


    List<User> findAll();
}
