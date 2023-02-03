package org.example.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.models.requests.SuspiciousIPRequest;
import org.example.models.responses.DeleteSuspiciousIPResponse;
import org.example.models.responses.SuspiciousIPResponse;
import org.example.services.SuspiciousIPService;
import org.example.services.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("hasRole('SUPPORT')")
@RequestMapping("/api/antifraud/suspicious-ip")
@RequiredArgsConstructor
@RestController
public class SuspiciousIPController {
    final SuspiciousIPService SuspiciousIPService;
    final UserService userService;

    @PostMapping()
    SuspiciousIPResponse addSuspiciousIPs(@RequestBody @Valid SuspiciousIPRequest request) {
        return SuspiciousIPService.addSuspiciousIP(request.getIp());
    }

    @DeleteMapping("/{ip}")
    DeleteSuspiciousIPResponse delete(@PathVariable String ip) {
        return SuspiciousIPService.delete(ip);
    }

    @GetMapping()
    List<SuspiciousIPResponse> get() {
        return SuspiciousIPService.getAll();
    }
}
