package org.example.service;

import org.example.model.CinemaRoom;
import org.example.model.SeatCoordinatesRequest;
import org.example.model.dto.SeatDTO;

public interface CinemaService {
    CinemaRoom getCinemaRoomInfo();

    SeatDTO purchase(SeatCoordinatesRequest seat);
}
