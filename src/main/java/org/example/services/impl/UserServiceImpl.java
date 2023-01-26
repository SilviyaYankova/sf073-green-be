package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.models.entities.UserEntity;
import org.example.models.enums.RoleEnum;
import org.example.models.requests.ChangeUserAccessRequest;
import org.example.models.requests.ChangeUserRoleRequest;
import org.example.models.requests.RegisterUserRequest;
import org.example.models.responses.ChangeUserAccessResponse;
import org.example.models.responses.DeleteUserResponse;
import org.example.models.responses.RegisterUserResponse;
import org.example.repositories.UserRepository;
import org.example.services.UserService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    final UserRepository userRepository;
    final PasswordEncoder passwordEncoder;

    @Override
    public RegisterUserResponse register(RegisterUserRequest request) {
        UserEntity userEntity = new UserEntity(request.getName(), request.getUsername());
        userEntity.setPassword(passwordEncoder.encode(request.getPassword()));

        if (userRepository.count() >= 1) {
            userEntity.setRole(RoleEnum.MERCHANT);
            userEntity.setAccountLocked(false);
        } else {
            userEntity.setRole(RoleEnum.ADMINISTRATOR);
        }

        userRepository.save(userEntity);
        return new RegisterUserResponse(userEntity.getId(), userEntity.getName(), userEntity.getUsername(),
                                        userEntity.getRole().name());
    }

    @Override
    public boolean usernameAlreadyExists(String username) {
        return userRepository
                .findByUsernameIgnoreCase(username)
                .isPresent();
    }

    @Override
    public List<RegisterUserResponse> getAllUsers() {
        return userRepository
                .findAll()
                .stream()
                .map((a) -> new RegisterUserResponse(a.getId(), a.getName(), a.getUsername(), a.getRole().name()))
                .collect(Collectors.toList());
    }

    @Override
    public DeleteUserResponse delete(String username) {
        UserEntity user = findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username " + username + " doesn't exist!"));
        userRepository.delete(user);
        return new DeleteUserResponse(user.getUsername(), "Deleted successfully!");
    }

    public Optional<UserEntity> findUserByUsername(String username) {
        return userRepository
                .findByUsernameIgnoreCase(username);
    }

    @Override
    public void changeRole(ChangeUserRoleRequest request) {
        Optional<UserEntity> userEntity = findUserByUsername(request.getUsername());
        if (userEntity.isPresent()) {
            RoleEnum role = RoleEnum.valueOf(request.getRole());
            userEntity.get().setRole(role);
            userRepository.save(userEntity.get());
        }
    }

    @Override
    public boolean checkIfRoleExist(String role) {
        return role.equals(RoleEnum.ADMINISTRATOR.name())
                || role.equals(RoleEnum.MERCHANT.name())
                || role.equals(RoleEnum.SUPPORT.name());
    }

    @Override
    public ChangeUserAccessResponse changeAccess(ChangeUserAccessRequest request) {
        ChangeUserAccessResponse changeUserAccessResponse;
        if (request.getOperation().equals("UNLOCK")) {
            changeUserAccessResponse = unlockUser(request);
        } else {
            changeUserAccessResponse = lockUser(request);
        }
        return changeUserAccessResponse;
    }

    private ChangeUserAccessResponse unlockUser(ChangeUserAccessRequest request) {
        UserEntity userEntity = findUserByUsername(request.getUsername())
                .orElseThrow(
                        () -> new UsernameNotFoundException("Username " + request.getUsername() + " doesn't exist!"));
        userEntity.setAccountLocked(true);
        userRepository.save(userEntity);
        return new ChangeUserAccessResponse("User " + request.getUsername() + " unlocked!");
    }


    private ChangeUserAccessResponse lockUser(ChangeUserAccessRequest request) {
        UserEntity userEntity = findUserByUsername(request.getUsername())
                .orElseThrow(
                        () -> new UsernameNotFoundException("Username " + request.getUsername() + " doesn't exist!"));
        userEntity.setAccountLocked(false);
        userRepository.save(userEntity);
        return new ChangeUserAccessResponse("User " + request.getUsername() + " locked!");
    }
}