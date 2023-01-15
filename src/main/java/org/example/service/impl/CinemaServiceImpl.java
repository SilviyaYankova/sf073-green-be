package org.example.service.impl;

import org.example.configuration.CinemaProperties;
import org.example.model.CinemaRoom;
import org.example.repository.SeatRepository;
import org.example.service.CinemaService;
import org.springframework.stereotype.Service;

@Service
public class CinemaServiceImpl implements CinemaService {
    SeatRepository seatRepository;
    CinemaProperties properties;

    public CinemaServiceImpl(SeatRepository seatRepository, CinemaProperties properties) {
        this.seatRepository = seatRepository;
        this.properties = properties;
    }

    @Override
    public CinemaRoom getCinemaRoomInfo() {
        return new CinemaRoom(properties.getTotalRows(),
                              properties.getTotalColumns(),
                              seatRepository.getAvailableSeats());
    }
}
