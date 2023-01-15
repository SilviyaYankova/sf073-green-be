package org.example.cotroller;

import org.example.model.CinemaRoom;
import org.example.service.CinemaService;
import org.springframework.web.bind.annotation.GetMapping;
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
}
