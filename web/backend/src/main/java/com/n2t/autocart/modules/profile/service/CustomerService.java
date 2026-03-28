package com.n2t.autocart.modules.profile.service;

import com.n2t.autocart.modules.account.entity.User;
import com.n2t.autocart.modules.account.repository.UserRepository;
import com.n2t.autocart.modules.cart.entity.Cart;
import com.n2t.autocart.modules.profile.dto.CustomerProfileResponse;
import com.n2t.autocart.modules.profile.dto.CreateCustomerRequest;
import com.n2t.autocart.modules.profile.dto.UpdateMyCustomerProfileRequest;
import com.n2t.autocart.modules.profile.entity.Customer;
import com.n2t.autocart.modules.profile.repository.CustomerRepository;
import com.n2t.autocart.security.SecurityUtil;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;

    public CustomerService(CustomerRepository customerRepository, UserRepository userRepository) {
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
    }
    public Customer handlerCreateCustomer(CreateCustomerRequest request){
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Customer customer = new Customer();
        customer.setUser(user);
        customer.setAge(request.age());
        customer.setAvatarUrl(request.avatarUrl());
        customer.setFullname(request.fullname());
        customer.setCart(new Cart());
        customer.setDateJoin(Instant.now());
        return customerRepository.save(customer);
    }

    public CustomerProfileResponse handleGetMyProfile() {
        Customer customer = getCurrentCustomer();
        return mapToProfileResponse(customer);
    }

    public CustomerProfileResponse handleUpdateMyProfile(UpdateMyCustomerProfileRequest request) {
        if (request.fullname() == null || request.fullname().isBlank()) {
            throw new RuntimeException("Fullname is required");
        }
        if (request.age() == null) {
            throw new RuntimeException("Age is required");
        }

        Customer customer = getCurrentCustomer();
        customer.setFullname(request.fullname().trim());
        customer.setAge(request.age());
        customer.setAvatarUrl(request.avatarUrl());

        Customer updatedCustomer = customerRepository.save(customer);
        return mapToProfileResponse(updatedCustomer);
    }

    private Customer getCurrentCustomer() {
        String email = SecurityUtil.getCurrentUserLogin()
                .orElseThrow(() -> new RuntimeException("Unauthenticated user"));

        return customerRepository.findByUser_Email(email)
                .orElseThrow(() -> new RuntimeException("Customer profile not found"));
    }

    private CustomerProfileResponse mapToProfileResponse(Customer customer) {
        return new CustomerProfileResponse(
                customer.getId(),
                customer.getPhone(),
                customer.getFullname(),
                customer.getAge(),
                customer.getAvatarUrl()
        );
    }
}
