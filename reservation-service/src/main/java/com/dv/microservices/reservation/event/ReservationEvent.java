package com.dv.microservices.reservation.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationEvent {
    private String reservationOrder;
    private String email;
}
