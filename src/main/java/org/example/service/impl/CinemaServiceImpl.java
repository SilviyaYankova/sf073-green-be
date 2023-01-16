package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.configuration.CinemaProperties;
import org.example.exeption.SeatOutOfBounceException;
import org.example.exeption.TicketAlreadySoldException;
import org.example.exeption.WrongToken;
import org.example.model.dto.CinemaRoomResponse;
import org.example.model.entity.SeatEntity;
import org.example.model.dto.SeatCoordinatesRequest;
import org.example.model.dto.ReturnedTicketResponse;
import org.example.model.dto.SeatResponse;
import org.example.model.dto.SoldTicketResponse;
import org.example.model.dto.StatsResponse;
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
    public CinemaRoomResponse getCinemaRoomInfo() {
        return new CinemaRoomResponse(properties.getTotalRows(),
                                      properties.getTotalColumns(),
                                      seatRepository.getAvailableSeats()
                                            .stream()
                                            .map(this::addPrice)
                                            .collect(Collectors.toList())
        );
    }

    @Override
    public SoldTicketResponse purchase(SeatCoordinatesRequest seat) {
        if (seat.getRow() < 1 || seat.getRow() > properties.getTotalRows() ||
                seat.getColumn() < 1 || seat.getColumn() > properties.getTotalColumns()) {
            throw new SeatOutOfBounceException();
        }
        if (!seatRepository.isAvailable(seat)) {
            throw new TicketAlreadySoldException();
        }
        int price = calculatePrice(seat);
        SeatEntity ticket = seatRepository.sell(seat, price);
        return new SoldTicketResponse(ticket);
    }

    @Override
    public ReturnedTicketResponse returnTicket(String token) {
        SeatEntity seatEntity = seatRepository.getSeatByToken(token).orElseThrow(WrongToken::new);
        ReturnedTicketResponse returnedTicket = new ReturnedTicketResponse(
                new SeatResponse(seatEntity.getRow(), seatEntity.getColumn(), seatEntity.getSellPrice()));
        SeatCoordinatesRequest seatCoordinatesRequest = new SeatCoordinatesRequest(seatEntity);
        seatRepository.setAsAvailable(seatCoordinatesRequest);
        return returnedTicket;
    }

    @Override
    public StatsResponse calcStats() {
        int currentIncome = seatRepository.totalIncome();
        int numberOfAvailableSeats = seatRepository.getAvailableSeats().size();
        int numberOfPurchasedTickets = seatRepository.soldTicketsCount();
        StatsResponse statsResponse = new StatsResponse();
        statsResponse.setCurrentIncome(currentIncome);
        statsResponse.setNumberOfAvailableSeats(numberOfAvailableSeats);
        statsResponse.setNumberOfPurchasedTickets(numberOfPurchasedTickets);
        return statsResponse;
    }

    private int calculatePrice(SeatCoordinatesRequest seat) {
        return seat.getRow() <= properties.getFrontRows()
                ? properties.getFrontRowsPrice()
                : properties.getBackRowsPrice();
    }

    private SeatResponse addPrice(SeatCoordinatesRequest seat) {
        int price = calculatePrice(seat);
        return new SeatResponse(seat.getRow(), seat.getColumn(), price);
    }
}
