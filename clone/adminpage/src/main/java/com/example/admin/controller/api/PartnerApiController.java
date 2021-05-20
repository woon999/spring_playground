package com.example.admin.controller.api;


import com.example.admin.controller.CrudController;
import com.example.admin.model.entity.Partner;
import com.example.admin.model.network.request.PartnerApiRequest;
import com.example.admin.model.network.response.PartnerApiResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/partner")
public class PartnerApiController extends CrudController<PartnerApiRequest, PartnerApiResponse, Partner> {


}
