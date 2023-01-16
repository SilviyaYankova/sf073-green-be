package org.example.repository;

import org.example.configuration.CinemaProperties;
import org.example.exeption.BusinessException;
import org.example.exeption.SeatOutOfBounceException;
import org.example.model.entity.SeatEntity;
import org.example.model.dto.SeatCoordinatesRequest;
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
    private List<SeatEntity> seatEntities;

    public SeatRepository(CinemaProperties properties) {
        this.properties = properties;
    }

    @PostConstruct
    public void init() {
        seatEntities = new ArrayList<>();
        for (int i = 1; i <= properties.getTotalRows(); i++) {
            for (int j = 1; j <= properties.getTotalColumns(); j++) {
                seatEntities.add(new SeatEntity(i, j));
            }
        }
    }

    public List<SeatCoordinatesRequest> getAvailableSeats() {
        return seatEntities.stream()
                           .filter(s -> !s.isSold())
                           .map(SeatCoordinatesRequest::new)
                           .collect(Collectors.toList());
    }

    public boolean isAvailable(SeatCoordinatesRequest seat) {
        return seatEntities.stream()
                           .filter(s -> s.getRow() == seat.getRow() &&
                                   s.getColumn() == seat.getColumn())
                           .anyMatch(s -> !s.isSold());
    }

    public SeatEntity sell(SeatCoordinatesRequest seat, int price) {
        SeatEntity entity = seatEntities.stream()
                                        .filter(s -> s.getRow() == seat.getRow() &&
                                                s.getColumn() == seat.getColumn())
                                        .findFirst().orElseThrow(SeatOutOfBounceException::new);
        entity.setSellPrice(price);
        entity.setToken(UUID.randomUUID().toString());
        return entity;
    }

    public Optional<SeatEntity> getSeatByToken(String token) {
        return seatEntities.stream()
                           .filter(s -> token.equals(s.getToken()))
                           .findFirst();
    }

    public void setAsAvailable(SeatCoordinatesRequest seat) {
        SeatEntity entity = seatEntities.stream()
                                        .filter(s -> s.getRow() == seat.getRow() &&
                                                s.getColumn() == seat.getColumn())
                                        .findFirst()
                                        .orElseThrow(BusinessException::new);
        entity.setToken(null);
        entity.setSellPrice(null);
    }

    public int totalIncome() {
        return seatEntities.stream()
                           .filter(SeatEntity::isSold)
                           .mapToInt(SeatEntity::getSellPrice).sum();
    }

    public int soldTicketsCount() {
        return (int) seatEntities.stream()
                                 .filter(SeatEntity::isSold)
                                 .count();
    }
}
