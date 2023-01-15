package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter@Setter@NoArgsConstructor
@AllArgsConstructor
public class SeatCoordinatesRequest {
    int row;
    int column;

    public SeatCoordinatesRequest(Seat seat) {
        this.row = seat.getRow();
        this.column = seat.getColumn();
    }
}
