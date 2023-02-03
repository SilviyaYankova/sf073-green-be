package org.example.controllers;
import lombok.RequiredArgsConstructor;
import org.example.models.requests.AddStolenCardRequest;
import org.example.models.responses.CardResponse;
import org.example.models.responses.DeleteCardResponse;
import org.example.services.StolenCardService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("hasRole('SUPPORT')")
@RequestMapping("/api/antifraud")
@RequiredArgsConstructor
@RestController
public class StolenCardController {
    final StolenCardService stolenCardService;

    @PostMapping("/stolencard")
    CardResponse addStolenCard(@RequestBody AddStolenCardRequest request) {
        return stolenCardService.addCard(request.getNumber());
    }

    @DeleteMapping("stolencard/{number}")
    DeleteCardResponse delete(@PathVariable String number) {
        return stolenCardService.delete(number);
    }

    @GetMapping("/stolencard")
    List<CardResponse> getAll() {
        return stolenCardService.getAll();
    }
}
