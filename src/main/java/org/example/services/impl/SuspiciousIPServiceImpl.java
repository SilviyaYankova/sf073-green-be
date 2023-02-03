package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.models.entities.SuspiciousIPEntity;
import org.example.models.entities.UserEntity;
import org.example.models.enums.RoleEnum;
import org.example.models.responses.DeleteSuspiciousIPResponse;
import org.example.models.responses.SuspiciousIPResponse;
import org.example.repositories.SuspiciousIPRepository;
import org.example.services.SuspiciousIPService;
import org.example.services.UserService;
import org.example.services.Validator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SuspiciousIPServiceImpl implements SuspiciousIPService {
    final SuspiciousIPRepository suspiciousIPRepository;
    final UserService userService;
    final Validator validator;

    @Override
    public SuspiciousIPResponse addSuspiciousIP(String ip) {
        if (ipIsBlacklisted(ip)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        validator.validateIp(ip);
        SuspiciousIPEntity entity = new SuspiciousIPEntity();
        entity.setIp(ip);
        suspiciousIPRepository.save(entity);
        return new SuspiciousIPResponse(entity.getId(), entity.getIp());
    }

    @Override
    public boolean ipIsBlacklisted(String ip) {
        return suspiciousIPRepository.findByIp(ip).isPresent();
    }

    @Override
    public DeleteSuspiciousIPResponse delete(String ip) {
        Optional<UserEntity> userEntity = userService.getLoggedUser();
        if (!userEntity.get().getRole().name().equals(RoleEnum.SUPPORT.name())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        validator.validateIp(ip);
        if (!ipIsBlacklisted(ip)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Optional<SuspiciousIPEntity> byIp = suspiciousIPRepository.findByIp(ip);
        suspiciousIPRepository.delete(byIp.get());
        return new DeleteSuspiciousIPResponse("IP " + ip + " successfully removed!");
    }

    @Override
    public List<SuspiciousIPResponse> getAll() {
        return suspiciousIPRepository
                .findAll()
                .stream()
                .map((a) -> new SuspiciousIPResponse(a.getId(), a.getIp()))
                .collect(Collectors.toList());
    }
}
