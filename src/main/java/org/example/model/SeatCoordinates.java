package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter@Setter@NoArgsConstructor
@AllArgsConstructor
public class SeatCoordinates {
    int row;
    int column;

    public SeatCoordinates(Seat seat) {
        this.row = seat.getRow();
        this.column = seat.getColumn();
    }
}
