package org.example.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.model.Seat;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SoldTicketDto {
    String token;
    SeatDTO ticket;

    public SoldTicketDto(Seat seat) {
        this.token = seat.getToken();
        this.ticket = new SeatDTO(seat.getRow(), seat.getColumn(), seat.getSellPrice());
    }
}
