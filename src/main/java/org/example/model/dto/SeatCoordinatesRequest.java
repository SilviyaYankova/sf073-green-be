package org.example.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.model.entity.SeatEntity;

@Getter@Setter@NoArgsConstructor
@AllArgsConstructor
public class SeatCoordinatesRequest {
    int row;
    int column;

    public SeatCoordinatesRequest(SeatEntity seatEntity) {
        this.row = seatEntity.getRow();
        this.column = seatEntity.getColumn();
    }
}
