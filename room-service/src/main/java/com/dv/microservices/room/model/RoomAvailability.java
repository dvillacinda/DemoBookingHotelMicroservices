package com.dv.microservices.room.model;

import java.util.List;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "t_room_availability")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoomAvailability {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  

    @Column(nullable = false)
    private int roomId;

    @Column(nullable = true)
    @OneToMany(mappedBy = "roomAvailability",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BlockedPeriod> blockedPeriod; 

    @Column(nullable = true)
    @OneToMany(mappedBy = "roomAvailability",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReservationDates> reservationDates; 
}
