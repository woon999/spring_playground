package com.loosie.javaallinone.project3.mycontact.service;

import com.loosie.javaallinone.project3.mycontact.repository.GroupRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class GroupServiceTest {

    @InjectMocks
    private GroupService groupService;
    @Mock
    private GroupRepository groupRepository;

    @Test
    void getAll(){
        //getAll test
    }


}