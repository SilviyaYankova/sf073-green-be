package org.example.repository;


import org.example.configuration.CinemaProperties;
import org.example.exeption.BusinessException;
import org.example.exeption.SeatOutOfBounceException;
import org.example.model.Seat;
import org.example.model.SeatCoordinates;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class SeatRepository {
    CinemaProperties properties;
    private List<Seat> seats;

    public SeatRepository(CinemaProperties properties) {
        this.properties = properties;
    }

    @PostConstruct
    public void init() {
        seats = new ArrayList<>();
        for (int i = 1; i <= properties.getTotalRows(); i++) {
            for (int j = 1; j <= properties.getTotalColumns(); j++) {
                seats.add(new Seat(i, j));
            }
        }
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public List<SeatCoordinates> getAvailableSeats() {
        return seats.stream()
                    .filter(s -> !s.isSold())
                    .map(SeatCoordinates::new)
                    .collect(Collectors.toList());
    }

    public boolean isAvailable(SeatCoordinates seat) {
        return seats.stream()
                    .filter(s -> s.getRow() == seat.getRow() &&
                            s.getColumn() == seat.getColumn())
                    .anyMatch(s -> !s.isSold());
    }

    public Seat sell(SeatCoordinates seat, int price) {
        Seat entity = seats.stream()
                           .filter(s -> s.getRow() == seat.getRow() &&
                                   s.getColumn() == seat.getColumn())
                           .findFirst().orElseThrow(SeatOutOfBounceException::new);
        entity.setSellPrice(price);
        entity.setToken(UUID.randomUUID().toString());
        return entity;
    }

    public Optional<Seat> getSeatByToken(String token) {
        return seats.stream()
                    .filter(s -> token.equals(s.getToken()))
                    .findFirst();
    }

    public void setAsAvailable(SeatCoordinates seat) {
        Seat entity = seats.stream()
                           .filter(s -> s.getRow() == seat.getRow() &&
                                   s.getColumn() == seat.getColumn())
                           .findFirst()
                           .orElseThrow(BusinessException::new);
        entity.setToken(null);
        entity.setSellPrice(null);
    }
}
