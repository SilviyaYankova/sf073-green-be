package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.configuration.CinemaProperties;
import org.example.model.CinemaRoom;
import org.example.model.ErrorInfo;
import org.example.model.Seat;
import org.example.model.SeatInfo;
import org.example.repository.SeatRepository;
import org.example.service.CinemaService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CinemaServiceImpl implements CinemaService {
    final private SeatRepository seatRepository;
    final private CinemaProperties properties;
    List<Seat> availableSeats = new ArrayList<>();
    private List<Seat> purchasedSeats = new ArrayList<>();
    private ErrorInfo errorInfo;

    @Override
    public CinemaRoom getCinemaRoomInfo() {
        return new CinemaRoom(properties.getTotalRows(),
                              properties.getTotalColumns(),
                              seatRepository.getAvailableSeats());
    }

    @Override
    public ErrorInfo getErrorInfo() {
        return errorInfo;
    }

    @Override
    public SeatInfo purchase(Seat seat) {
        int price = calculatePrice(seat);
        SeatInfo seatInfo = new SeatInfo(seat.getRow(), seat.getColumn(), price);
        availableSeats = seatRepository.getAvailableSeats();
        Optional<Seat> first = availableSeats.stream().filter(
                o -> o.getRow() == seat.getRow()
                        && o.getColumn() == seat.getColumn()).findFirst();

        if (!purchasedSeats.contains(first.get())) {
            purchasedSeats.add(first.get());
        }

        return seatInfo;
    }

    @Override
    public boolean seatDoesNotExist(Seat seat) {
        Optional<Seat> first = seatRepository.getAvailableSeats().stream().filter(
                o -> o.getRow() == seat.getRow()
                        && o.getColumn() == seat.getColumn()).findFirst();
        if (first.isEmpty()) {
            errorInfo = new ErrorInfo("The number of a row or a column is out of bounds!");
            return true;
        }
        return false;
    }


    private int calculatePrice(Seat seat) {
        return seat.getRow() <= properties.getFrontRows() ? 10 : 8;
    }

    @Override
    public boolean seatIsPurchased(Seat seat) {
        Optional<Seat> first = availableSeats.stream().filter(
                o -> o.getRow() == seat.getRow()
                        && o.getColumn() == seat.getColumn()).findFirst();
        if (purchasedSeats.size() > 0 && purchasedSeats.contains(first.get())) {
            errorInfo = new ErrorInfo("The ticket has been already purchased!");
            return true;
        }
        return false;
    }
}
