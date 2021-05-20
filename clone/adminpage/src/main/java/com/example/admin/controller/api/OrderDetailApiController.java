package com.example.admin.controller.api;

import com.example.admin.controller.CrudController;
import com.example.admin.model.entity.OrderDetail;
import com.example.admin.model.network.request.OrderDetailApiRequest;
import com.example.admin.model.network.response.OrderDetailApiResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orderDetail")
public class OrderDetailApiController extends CrudController<OrderDetailApiRequest, OrderDetailApiResponse, OrderDetail> {


}
