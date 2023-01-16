package org.example.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.model.entity.SeatEntity;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SoldTicketResponse {
    String token;
    SeatResponse ticket;

    public SoldTicketResponse(SeatEntity seatEntity) {
        this.token = seatEntity.getToken();
        this.ticket = new SeatResponse(seatEntity.getRow(), seatEntity.getColumn(), seatEntity.getSellPrice());
    }
}
