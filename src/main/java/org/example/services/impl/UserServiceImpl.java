package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.models.entities.UserEntity;
import org.example.models.enums.RoleEnum;
import org.example.models.responses.ChangeUserAccessResponse;
import org.example.models.responses.ChangeUserRoleResponse;
import org.example.models.responses.DeleteUserResponse;
import org.example.models.responses.RegisterUserResponse;
import org.example.repositories.UserRepository;
import org.example.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    final UserRepository userRepository;
    final PasswordEncoder passwordEncoder;

    @Override
    public RegisterUserResponse register(String name, String username, String password) {
        if (usernameAlreadyExists(username)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        UserEntity userEntity = new UserEntity(name, username);
        userEntity.setPassword(passwordEncoder.encode(password));

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
        if (findUserByUsername(username).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
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
    public ChangeUserRoleResponse changeRole(String username, String userRole) {
        if (!checkIfRoleExist(userRole)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if (userRole.equals(RoleEnum.ADMINISTRATOR.name())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        Optional<UserEntity> user = findUserByUsername(username);
        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if (userRole.equals(user.get().getRole().name())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        RoleEnum role = RoleEnum.valueOf(userRole);
        user.get().setRole(role);
        userRepository.save(user.get());
        return new ChangeUserRoleResponse(user.get().getId(), user.get().getName(),
                                          user.get().getUsername(), user.get().getRole().toString());
    }

    @Override
    public boolean checkIfRoleExist(String role) {
        return role.equals(RoleEnum.ADMINISTRATOR.name())
                || role.equals(RoleEnum.MERCHANT.name())
                || role.equals(RoleEnum.SUPPORT.name());
    }

    @Override
    public ChangeUserAccessResponse changeAccess(String username, String operation) {
        ChangeUserAccessResponse changeUserAccessResponse = null;
        if (operation.equals("UNLOCK")) {
            changeUserAccessResponse = unlockUser(username);
        } else if (operation.equals("LOCK")) {
            changeUserAccessResponse = lockUser(username);
        }
        return changeUserAccessResponse;
    }

    @Override
    public Optional<UserEntity> getLoggedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return findUserByUsername(username);
    }

    private ChangeUserAccessResponse unlockUser(String username) {
        UserEntity userEntity = findUserByUsername(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException("Username " + username + " doesn't exist!"));
        userEntity.setAccountLocked(true);
        userRepository.save(userEntity);
        return new ChangeUserAccessResponse("User " + username + " unlocked!");
    }


    private ChangeUserAccessResponse lockUser(String username) {
        UserEntity userEntity = findUserByUsername(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException("Username " + username + " doesn't exist!"));
        userEntity.setAccountLocked(false);
        userRepository.save(userEntity);
        return new ChangeUserAccessResponse("User " + username + " locked!");
    }
}
