package org.example.service;

import org.example.model.dto.CinemaRoomResponse;
import org.example.model.dto.SeatCoordinatesRequest;
import org.example.model.dto.ReturnedTicketResponse;
import org.example.model.dto.SoldTicketResponse;
import org.example.model.dto.StatsResponse;

public interface CinemaService {
    CinemaRoomResponse getCinemaRoomInfo();

    SoldTicketResponse purchase(SeatCoordinatesRequest seat);

    ReturnedTicketResponse returnTicket(String token);

    StatsResponse calcStats();
}
