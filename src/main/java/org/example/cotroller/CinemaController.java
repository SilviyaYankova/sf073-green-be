package org.example.cotroller;

import org.example.model.CinemaRoom;
import org.example.model.ErrorInfo;
import org.example.model.Seat;
import org.example.service.CinemaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CinemaController {
    CinemaService cinemaService;

    public CinemaController(CinemaService cinemaService) {
        this.cinemaService = cinemaService;
    }

    @GetMapping("/seats")
    CinemaRoom foo() {
        return cinemaService.getCinemaRoomInfo();
    }

    @PostMapping("/purchase")
    ResponseEntity<?> purchase(@RequestBody Seat seat) {
        if (cinemaService.seatDoesNotExist(seat)) {
            return new ResponseEntity<>(new ErrorInfo(cinemaService.getErrorInfo().getError()),
                                        HttpStatus.BAD_REQUEST);
        }
        if (cinemaService.seatIsPurchased(seat)) {
            return new ResponseEntity<>(new ErrorInfo(cinemaService.getErrorInfo().getError()),
                                        HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(cinemaService.purchase(seat), HttpStatus.OK);
    }
}
