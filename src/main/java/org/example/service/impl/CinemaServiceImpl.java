package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.configuration.CinemaProperties;
import org.example.exeption.SeatOutOfBounceException;
import org.example.exeption.TicketAlreadySoldException;
import org.example.exeption.WrongToken;
import org.example.model.CinemaRoom;
import org.example.model.Seat;
import org.example.model.SeatCoordinates;
import org.example.model.dto.ReturnedTicketDto;
import org.example.model.dto.SeatDTO;
import org.example.model.dto.SoldTicketDto;
import org.example.repository.SeatRepository;
import org.example.service.CinemaService;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CinemaServiceImpl implements CinemaService {
    final private SeatRepository seatRepository;
    final private CinemaProperties properties;

    @Override
    public CinemaRoom getCinemaRoomInfo() {
        return new CinemaRoom(properties.getTotalRows(),
                              properties.getTotalColumns(),
                              seatRepository.getAvailableSeats()
                                            .stream()
                                            .map(this::addPrice)
                                            .collect(Collectors.toList())
        );
    }

    @Override
    public SoldTicketDto purchase(SeatCoordinates seat) {
        if (seat.getRow() < 1 || seat.getRow() > properties.getTotalRows() ||
                seat.getColumn() < 1 || seat.getColumn() > properties.getTotalColumns()) {
            throw new SeatOutOfBounceException();
        }
        if (!seatRepository.isAvailable(seat)) {
            throw new TicketAlreadySoldException();
        }
        int price = calculatePrice(seat);
        Seat ticket = seatRepository.sell(seat, price);
        return new SoldTicketDto(ticket);
    }

    @Override
    public ReturnedTicketDto returnTicket(String token) {
        Seat seat = seatRepository.getSeatByToken(token).orElseThrow(WrongToken::new);
        ReturnedTicketDto returnedTicket = new ReturnedTicketDto(
                new SeatDTO(seat.getRow(), seat.getColumn(), seat.getSellPrice()));
        SeatCoordinates seatCoordinates = new SeatCoordinates(seat);
        seatRepository.setAsAvailable(seatCoordinates);
        return returnedTicket;
    }

    private int calculatePrice(SeatCoordinates seat) {
        return seat.getRow() <= properties.getFrontRows()
                ? properties.getFrontRowsPrice()
                : properties.getBackRowsPrice();
    }

    private SeatDTO addPrice(SeatCoordinates seat) {
        int price = calculatePrice(seat);
        return new SeatDTO(seat.getRow(), seat.getColumn(), price);
    }
}
