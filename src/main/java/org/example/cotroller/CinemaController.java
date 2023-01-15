package org.example.cotroller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exeption.BusinessException;
import org.example.model.CinemaRoom;
import org.example.model.SeatCoordinatesRequest;
import org.example.model.dto.ErrorDTO;
import org.example.model.dto.SeatDTO;
import org.example.service.CinemaService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
public class CinemaController {
    final CinemaService cinemaService;

    @GetMapping("/seats")
    CinemaRoom getAvailableSeats() {
        return cinemaService.getCinemaRoomInfo();
    }

    @PostMapping("/purchase")
    SeatDTO purchase(@RequestBody SeatCoordinatesRequest seat) {
        return cinemaService.purchase(seat);
    }

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorDTO errorHandler(BusinessException exception) {
        log.info("exception {}", exception.getMessage());
        return new ErrorDTO(exception.getMessage());
    }
}
