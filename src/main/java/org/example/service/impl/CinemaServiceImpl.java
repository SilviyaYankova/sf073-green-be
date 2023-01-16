package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.configuration.CinemaProperties;
import org.example.exeption.BusinessException;
import org.example.exeption.SeatOutOfBounceException;
import org.example.exeption.TicketAlreadySoldException;
import org.example.exeption.WrongToken;
import org.example.model.CinemaRoom;
import org.example.model.Seat;
import org.example.model.SeatCoordinatesRequest;
import org.example.model.dto.ReturnTicketDto;
import org.example.model.dto.SeatDTO;
import org.example.model.dto.TicketDto;
import org.example.repository.SeatRepository;
import org.example.service.CinemaService;
import org.springframework.stereotype.Service;

import java.util.UUID;
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
    public TicketDto purchase(SeatCoordinatesRequest seatCordinates) {
        if (seatCordinates.getRow() < 1 || seatCordinates.getRow() > properties.getTotalRows() ||
                seatCordinates.getColumn() < 1 || seatCordinates.getColumn() > properties.getTotalColumns()) {
            throw new SeatOutOfBounceException();
        }
        if (!seatRepository.isAvailable(seatCordinates)) {
            throw new TicketAlreadySoldException();
        }

        String token = UUID.randomUUID().toString();
        Seat seat = seatRepository.getSeats().stream()
                                  .filter(s -> s.getRow() == seatCordinates.getRow() &&
                                          s.getColumn() == seatCordinates.getColumn())
                                  .findFirst().stream().findFirst().orElseThrow(BusinessException::new);
        seat.setToken(token);
        seatRepository.markAsSold(seatCordinates);
        SeatDTO seatDTO = addPrice(seatCordinates);
        return new TicketDto(token, seatDTO);
    }

    @Override
    public ReturnTicketDto returnTicket(String token) {
        Seat seat = seatRepository.getSeats().stream()
                                  .filter(s -> token.equals(s.getToken()))
                                  .findFirst()
                                  .orElseThrow(WrongToken::new);
        seat.setToken(null);
        seat.setSold(false);
        SeatCoordinatesRequest seatCoordinatesRequest = new SeatCoordinatesRequest(seat.getRow(), seat.getColumn());
        SeatDTO seatDTO = addPrice(seatCoordinatesRequest);
        return new ReturnTicketDto(seatDTO);
    }

    private int calculatePrice(SeatCoordinatesRequest seat) {
        return seat.getRow() <= properties.getFrontRows()
                ? properties.getFrontRowsPrice()
                : properties.getBackRowsPrice();
    }

    private SeatDTO addPrice(SeatCoordinatesRequest seat) {
        int price = calculatePrice(seat);
        return new SeatDTO(seat.getRow(), seat.getColumn(), price);
    }
}
