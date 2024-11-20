    package com.dv.microservices.room.model;

    import java.time.LocalDateTime;

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
    @Table(name = "t_blocked_period")
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public class BlockedPeriod {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false)
        private LocalDateTime startBlocked;
        @Column(nullable = false) 
        private LocalDateTime endBlocked; 

        @ManyToOne
        @JoinColumn(name = "room_availability_id")
        private RoomAvailability roomAvailability;

        public BlockedPeriod(LocalDateTime start, LocalDateTime end, RoomAvailability roomAvailability){
            this.startBlocked = start;
            this.endBlocked = end;
            this.roomAvailability  = roomAvailability;
        }
    }
