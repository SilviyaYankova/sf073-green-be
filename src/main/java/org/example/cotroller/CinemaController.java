package org.example.cotroller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exeption.NotAuthorizedException;
import org.example.model.dto.CinemaRoomResponse;
import org.example.model.dto.ReturnTicketRequest;
import org.example.model.dto.SeatCoordinatesRequest;
import org.example.model.dto.ReturnedTicketResponse;
import org.example.model.dto.SoldTicketResponse;
import org.example.model.dto.StatsResponse;
import org.example.service.CinemaService;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
public class CinemaController {
    final CinemaService cinemaService;

    @GetMapping("/seats")
    CinemaRoomResponse getAvailableSeats() {
        return cinemaService.getCinemaRoomInfo();
    }

    @PostMapping("/purchase")
    SoldTicketResponse purchaseTicket(@RequestBody SeatCoordinatesRequest seat) {
        return cinemaService.purchase(seat);
    }

    @PostMapping("/return")
    ReturnedTicketResponse returnTicket(@RequestBody ReturnTicketRequest ticket) {
        return cinemaService.returnTicket(ticket.getToken());
    }

    @PostMapping("/stats")
    StatsResponse statistics(@RequestParam(required = false) String password) {
        log.info("secret = {}", password);
        if (!"super_secret".equals(password)) {
            throw new NotAuthorizedException();
        }
        return cinemaService.calcStats();
    }
}
