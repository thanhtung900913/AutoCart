package com.n2t.autocart.modules.location.controller;

import com.n2t.autocart.modules.location.dto.AddressCreateRequest;
import com.n2t.autocart.modules.location.dto.AddressUpdateRequest;
import com.n2t.autocart.modules.location.entity.Address;
import com.n2t.autocart.modules.location.service.AddressService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/locations")
public class AddressController {
    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping("/addresses")
    public ResponseEntity<Address> createAddress(@RequestBody AddressCreateRequest address) {
        return ResponseEntity.ok().body(addressService.handleCreateAddress(address));
    }

    @PutMapping("/addresses/{id}")
    public ResponseEntity<Address> updateAddress(@PathVariable Integer id, @RequestBody AddressUpdateRequest request) {
        return ResponseEntity.ok(addressService.handleUpdateAddress(id, request));
    }

    @DeleteMapping("/addresses/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Integer id) {
        addressService.handleDeleteAddress(id);
        return ResponseEntity.noContent().build();
    }
}
