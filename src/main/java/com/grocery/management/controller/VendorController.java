package com.grocery.management.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grocery.management.playload.VendorPayload;
import com.grocery.management.service.VendorService;

@RestController
@RequestMapping(value = "/api")
public class VendorController {

    @Autowired
    private VendorService vendorService;
    
    @PostMapping(value = "/vendor")
    public ResponseEntity<Object> create(@RequestBody VendorPayload vendorPayload){
        return vendorService.create(vendorPayload);
    }
    
    @GetMapping(value = "/vendor/{id}")
    public ResponseEntity<Object> get(@PathVariable String id) {
        return vendorService.get(id);
    }
    
    @GetMapping(value = "/vendors")
    public ResponseEntity<List<VendorPayload>> getVendors(){
        List<VendorPayload> vendors = vendorService.getVendors();
        return new ResponseEntity<>(vendors, HttpStatus.OK);
    }
    
    @PutMapping(value = "/vendor/{id}")
    public ResponseEntity<Object> update(@PathVariable String id, @RequestBody VendorPayload vendorPayload){
        return vendorService.update(id, vendorPayload);
    }
    
    @DeleteMapping(value = "/vendor/{id}")
    public ResponseEntity<Object> delete(@PathVariable String id) {
        return vendorService.delete(id);
    }
}
