package org.example.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.models.requests.SuspiciousIPRequest;
import org.example.models.responses.DeleteSuspiciousIPResponse;
import org.example.models.responses.SuspiciousIPResponse;
import org.example.services.IPService;
import org.example.services.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("hasRole('SUPPORT')")
@RequestMapping("/api/antifraud")
@RequiredArgsConstructor
@RestController
public class SuspiciousIPController {
    final IPService IPService;
    final UserService userService;

    @PostMapping("/suspicious-ip")
    SuspiciousIPResponse addSuspiciousIPs(@RequestBody @Valid SuspiciousIPRequest request) {
        return IPService.addSuspiciousIP(request.getIp());
    }

    @DeleteMapping("suspicious-ip/{ip}")
    DeleteSuspiciousIPResponse delete(@PathVariable String ip) {
        return IPService.delete(ip);
    }

    @GetMapping("/suspicious-ip")
    List<SuspiciousIPResponse> get() {
        return IPService.getAll();
    }
}
