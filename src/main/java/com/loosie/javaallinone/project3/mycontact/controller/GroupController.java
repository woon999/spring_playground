package com.loosie.javaallinone.project3.mycontact.controller;

import com.loosie.javaallinone.project3.mycontact.domain.Group;
import com.loosie.javaallinone.project3.mycontact.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping
@RestController
public class GroupController {

    @Autowired
    private GroupService groupService;

    @GetMapping
    public List<Group> getAll(){
        return groupService.getAll();
    }

    @GetMapping("/{id}")
    public Group getGroup(@PathVariable Long id){
        return groupService.getGroup(id);
    }
}
