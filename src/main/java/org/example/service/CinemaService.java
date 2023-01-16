package org.example.service;

import org.example.model.CinemaRoom;
import org.example.model.SeatCoordinates;
import org.example.model.dto.ReturnedTicketDto;
import org.example.model.dto.SoldTicketDto;

public interface CinemaService {
    CinemaRoom getCinemaRoomInfo();

    SoldTicketDto purchase(SeatCoordinates seat);

    ReturnedTicketDto returnTicket(String token);
}
