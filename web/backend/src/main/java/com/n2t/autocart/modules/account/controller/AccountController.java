package com.n2t.autocart.modules.account.controller;

import com.n2t.autocart.modules.account.dto.LoginResponseDTO;
import com.n2t.autocart.modules.account.dto.UpdateUserAddressRequest;
import com.n2t.autocart.modules.account.entity.User;
import com.n2t.autocart.modules.account.service.UserService;
import com.n2t.autocart.modules.location.entity.Address;
import com.n2t.autocart.security.SecurityUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {
    private final UserService userService;

    public AccountController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<LoginResponseDTO.UserResponse> getCurrentAccount(){
        LoginResponseDTO.UserResponse user = new LoginResponseDTO.UserResponse();
        Optional<String> currentUser = SecurityUtil.getCurrentUserLogin();

        if (currentUser.isPresent()){
            User currentUserDB = userService.getUserByEmail(currentUser.get());
            user.setUserEmail(currentUser.get());
            user.setId(currentUserDB.getId());
            user.setUserEmail(currentUserDB.getEmail());
        }
        return ResponseEntity.ok().body(user);
    }

    @PutMapping("/addresses")
    public ResponseEntity<Address> updateUserAddress(@RequestBody UpdateUserAddressRequest request){
        return ResponseEntity.ok(userService.handleUpdateUserAddress(request));
    }
}
