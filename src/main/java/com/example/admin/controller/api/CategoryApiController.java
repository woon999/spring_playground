package com.example.admin.controller.api;


import com.example.admin.controller.CrudController;
import com.example.admin.model.entity.Category;
import com.example.admin.model.network.request.CategoryApiRequest;
import com.example.admin.model.network.response.CategoryApiResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/category")
public class CategoryApiController extends CrudController<CategoryApiRequest, CategoryApiResponse, Category> {


}
