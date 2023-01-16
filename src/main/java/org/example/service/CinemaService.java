package org.example.service;

import org.example.model.CinemaRoom;
import org.example.model.SeatCoordinatesRequest;
import org.example.model.dto.ReturnTicketDto;
import org.example.model.dto.TicketDto;

public interface CinemaService {
    CinemaRoom getCinemaRoomInfo();

    TicketDto purchase(SeatCoordinatesRequest seat);

    ReturnTicketDto returnTicket(String token);
}
