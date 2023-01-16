package org.example.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter@Setter@NoArgsConstructor
@AllArgsConstructor
public class SeatEntity {
    int row;
    int column;
    Integer sellPrice;
    String token;

    public SeatEntity(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public boolean isSold() {
        return token != null;
    }
}
