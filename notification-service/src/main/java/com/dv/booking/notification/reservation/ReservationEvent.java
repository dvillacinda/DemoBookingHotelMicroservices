package com.dv.booking.notification.reservation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationEvent {
    private String reservationNumber;
    private String email;
}