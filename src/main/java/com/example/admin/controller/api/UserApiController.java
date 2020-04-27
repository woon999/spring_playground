package com.example.admin.controller.api;

import com.example.admin.controller.CrudController;
import com.example.admin.model.entity.User;
import com.example.admin.model.network.request.UserApiRequest;
import com.example.admin.model.network.response.UserApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserApiController extends CrudController<UserApiRequest, UserApiResponse, User> {


}
