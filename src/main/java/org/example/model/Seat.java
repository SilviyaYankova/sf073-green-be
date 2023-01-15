package org.example.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public
class Seat {
    public int row;
    public int column;

    public Seat(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getPrice() {
        return row <= 4 ? 10 : 8;
    }
}
