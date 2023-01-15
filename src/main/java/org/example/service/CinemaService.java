package org.example.service;


import org.example.model.CinemaRoom;
import org.example.model.ErrorInfo;
import org.example.model.Seat;
import org.example.model.SeatInfo;

public interface CinemaService {
    CinemaRoom getCinemaRoomInfo();

    ErrorInfo getErrorInfo();

    SeatInfo purchase(Seat seat);

    boolean seatDoesNotExist(Seat seat);

    boolean seatIsPurchased(Seat seat);
}
