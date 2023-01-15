package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.model.dto.SeatDTO;

import java.util.List;


@Getter
@AllArgsConstructor
public class CinemaRoom {
    private int totalRows;
    private int totalColumns;
    private List<SeatDTO> availableSeats;
}
