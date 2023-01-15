package org.example.model;

import lombok.Getter;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Value
public @Getter
class CinemaRoom {
    int totalRows;
    int totalColumns;
    List<Seat> availableSeats;

}
