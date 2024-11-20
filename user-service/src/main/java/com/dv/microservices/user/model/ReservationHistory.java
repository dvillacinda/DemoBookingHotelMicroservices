package com.dv.microservices.user.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "t_reservation_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=true,unique = true)
    private String reservationId;  

    @ManyToOne
    @JoinColumn(name="reservation_history_id")
    private User user; 

    public ReservationHistory(String reservationId, User user){
        this.reservationId = reservationId;
        this.user = user;
    }

}
