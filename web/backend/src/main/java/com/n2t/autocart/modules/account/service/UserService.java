package com.n2t.autocart.modules.account.service;

import com.n2t.autocart.modules.account.dto.UserCreateRequest;
import com.n2t.autocart.modules.account.dto.UpdateUserAddressRequest;
import com.n2t.autocart.modules.account.entity.User;
import com.n2t.autocart.modules.account.entity.UserRole;
import com.n2t.autocart.modules.location.entity.Address;
import com.n2t.autocart.modules.account.repository.RoleRepository;
import com.n2t.autocart.modules.account.repository.UserRepository;
import com.n2t.autocart.modules.location.repository.AddressRepository;
import com.n2t.autocart.modules.location.repository.DistrictRepository;
import com.n2t.autocart.modules.location.repository.ProvinceRepository;
import com.n2t.autocart.modules.location.repository.WardRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AddressRepository addressRepository;
    private final WardRepository wardRepository;
    private final ProvinceRepository provinceRepository;
    private final DistrictRepository districtRepository;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            RoleRepository roleRepository,
            AddressRepository addressRepository,
            WardRepository wardRepository,
            ProvinceRepository provinceRepository,
            DistrictRepository districtRepository
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.addressRepository = addressRepository;
        this.wardRepository = wardRepository;
        this.provinceRepository = provinceRepository;
        this.districtRepository = districtRepository;
    }

    public User handleCreateUser(UserCreateRequest request) {
        boolean exists = userRepository.existsByEmail(request.email());
        if (exists) {
            throw new RuntimeException("User with email " + request.email() + " already exists");
        }

        User user = new User();
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));

        // Create UserRole associations
        Set<UserRole> userRoles = request.roleIds().stream()
                .map(roleId -> {
                    UserRole userRole = new UserRole();
                    userRole.setUser(user);
                    userRole.setRole(roleRepository.getReferenceById(roleId));
                    return userRole;
                })
                .collect(Collectors.toSet());
        user.setUserRoles(userRoles);

        return userRepository.save(user);
    }
    public User handleGetUserById(Integer id){
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            return user.get();
        }
        throw new RuntimeException("User not found");
    }
    public User getUserByEmail(String email){
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isPresent()){
            return user.get();
        }
        throw new RuntimeException("User does not exist");
    }
    public User getUserByEmailAndToken(String email, String token){
        Optional<User> user = this.userRepository.findByEmailAndRefreshTokens_Token(email, token);
        if(user.isPresent()){
            return user.get();
        }
        throw new RuntimeException("Khong tim thay user");
    }

    public Address handleUpdateUserAddress(UpdateUserAddressRequest request) {
        User user = handleGetUserById(request.id());
        boolean isValidAddress = wardRepository.existsByWardIdAndDistrict_DistrictIdAndProvince_ProvinceId(
                request.addressCreateRequest().wardId(),
                request.addressCreateRequest().districtId(),
                request.addressCreateRequest().provinceId());

        if (!isValidAddress) {
            throw new RuntimeException("Address is not valid");
        }

        Optional<Address> existingAddress = user.getAddresses().stream()
                .filter(address -> !Boolean.TRUE.equals(address.getIsDeleted()))
                .findFirst();

        Address address = existingAddress.orElseGet(Address::new);
        address.setAddressDetail(request.addressCreateRequest().addressDetail());
        address.setProvince(provinceRepository.getReferenceById(request.addressCreateRequest().provinceId()));
        address.setDistrict(districtRepository.getReferenceById(request.addressCreateRequest().districtId()));
        address.setWard(wardRepository.getReferenceById(request.addressCreateRequest().wardId()));
        address.setIsDeleted(false);
        address.setUser(user);

        Address savedAddress = addressRepository.save(address);
        user.getAddresses().add(savedAddress);

        return savedAddress;
    }
}
