package org.example.model.dto;

import lombok.Value;

@Value
public class SeatResponse {
    int row;
    int column;
    int price;
}
