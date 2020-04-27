package com.example.admin.controller;

import com.example.admin.ifs.CrudInterface;
import com.example.admin.model.network.Header;
import com.example.admin.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@Component
public class CrudController<Req,Res,Entity> implements CrudInterface<Req,Res> {

    @Autowired(required = false)
    protected BaseService<Req,Res,Entity> entityBaseService;

    @Override
    @PostMapping("")
    public Header<Res> create(@RequestBody Header<Req> request) {
        return entityBaseService.create(request);
    }

    @Override
    @GetMapping("{id}")
    public Header<Res> read(@PathVariable Long id) {
        return entityBaseService.read(id);
    }

    @Override
    @PutMapping("")
    public Header<Res> update(@RequestBody Header<Req> request) {
        return entityBaseService.update(request);
    }

    @Override
    @DeleteMapping("{id}")
    public Header delete(@PathVariable Long id) {
        return entityBaseService.delete(id);
    }
}
