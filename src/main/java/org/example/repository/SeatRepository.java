package org.example.repository;


import org.example.configuration.CinemaProperties;
import org.example.model.Seat;
import org.example.model.SeatCoordinatesRequest;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
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
                seats.add(new Seat(i, j, false, null));
            }
        }
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public List<SeatCoordinatesRequest> getAvailableSeats() {
        return seats.stream()
                    .filter(s -> !s.isSold())
                    .map(SeatCoordinatesRequest::new)
                    .collect(Collectors.toList());
    }

    public boolean isAvailable(SeatCoordinatesRequest seat) {
        return seats.stream()
                    .filter(s -> s.getRow() == seat.getRow() &&
                            s.getColumn() == seat.getColumn())
                    .anyMatch(s -> !s.isSold());
    }

    public void markAsSold(SeatCoordinatesRequest seat) {
        seats.stream()
             .filter(s -> s.getRow() == seat.getRow() &&
                     s.getColumn() == seat.getColumn())
             .findFirst()
             .ifPresent(s -> s.setSold(true));
    }
}
