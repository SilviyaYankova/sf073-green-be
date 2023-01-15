package org.example.repository;

import org.example.configuration.CinemaProperties;
import org.example.model.Seat;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

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

    public List<Seat> getAvailableSeats() {
        return seats;
    }
}
