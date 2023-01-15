package org.example.exeption;

public class SeatOutOfBounceException extends BusinessException {

    public SeatOutOfBounceException() {
        super("The number of a row or a column is out of bounds!");
    }
}
