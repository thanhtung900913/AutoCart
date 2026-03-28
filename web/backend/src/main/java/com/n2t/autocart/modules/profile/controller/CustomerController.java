package com.n2t.autocart.modules.profile.controller;

import com.n2t.autocart.common.anotation.ApiMessage;
import com.n2t.autocart.modules.profile.dto.CustomerProfileResponse;
import com.n2t.autocart.modules.profile.dto.CreateCustomerRequest;
import com.n2t.autocart.modules.profile.dto.UpdateMyCustomerProfileRequest;
import com.n2t.autocart.modules.profile.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/v1/profiles/customers"})
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }
    @PostMapping
    public ResponseEntity<String> createUserProfile(@RequestBody CreateCustomerRequest request){
        return ResponseEntity.ok().body(customerService.handlerCreateCustomer(request).toString());
    }

    @GetMapping("/me")
    @ApiMessage("Get customer profile successfully")
    public ResponseEntity<CustomerProfileResponse> getMyProfile() {
        return ResponseEntity.ok(customerService.handleGetMyProfile());
    }

    @PutMapping("/me")
    @ApiMessage("Update customer profile successfully")
    public ResponseEntity<CustomerProfileResponse> updateMyProfile(@RequestBody UpdateMyCustomerProfileRequest request) {
        return ResponseEntity.ok(customerService.handleUpdateMyProfile(request));
    }

}
