package com.example.admin.controller;

import com.example.admin.model.SearchParam;
import com.example.admin.model.network.Header;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api") // localhost:8080/api
public class GetController {

    // localhost:8080/api/getMethod
    @RequestMapping(method = RequestMethod.GET, path = "/getMethod")
    public String getRequest() {
        return "Hi getMethod";
    }

    @GetMapping("/getParameter") // localhost:8080/api/getParameter?id=1234&password=yyyymmdd
    public String getParameter(@RequestParam String id, @RequestParam(name = "password") String pwd) {

        System.out.println("id : " + id);
        System.out.println("pwd : " + pwd);

        return id + pwd;
    }

//   localhost:8080/api/getMultiParameter?account=abcd&email=study@gmail.com&page=10
    @GetMapping("/getMultiParameter")
    public SearchParam getMultiParameter(SearchParam searchParam){
        System.out.println(searchParam.getAccount());
        System.out.println(searchParam.getEmail());
        System.out.println(searchParam.getPage());

        return searchParam;
    }

    @GetMapping("/header")
    public Header getHeader(){

        // {"resultCode : "OK", "description": "OK"}
        return Header.builder().resultCode("OK").description("OK").build();
    }
}
