package com.loosie.javaallinone.project3.mycontact.service;


import com.loosie.javaallinone.project3.mycontact.domain.Group;
import com.loosie.javaallinone.project3.mycontact.domain.Person;
import com.loosie.javaallinone.project3.mycontact.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    public List<Group> getAll() {
        return groupRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Group getGroup(Long id) {
        return groupRepository.findById(id).orElse(null);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void postGroup(@RequestBody Object groupDto){
        //DTO 구현
        //Group 생성
    }


    @PostMapping("/{id}")
    public void modifyGroup(@PathVariable Long id, String description){
        //Group description 수정하기
    }

    @GetMapping("/{id}/people")
    public List<Person> getPeopleInGroup(@PathVariable Long id){
        return null; //특정 그룹의 Person 리스트 가져오기
    }

    @PutMapping("/{id}/person/{personId}")
    public void putPersonInGroup(@PathVariable Long id, @PathVariable Long personId){
        //Person 정보를 Group 정보에 매핑하기
    }
}
