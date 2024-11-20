package com.dv.microservices.information.model;

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
@Table(name = "t_information")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Information {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 

    @Column(nullable = false)
    private int roomNumber;
    @Column(nullable = false)
    private int capacity; 
    @Column(nullable = false)
    private String description; 
    @Column(nullable = false)
    private float price; 
    @Column(nullable = false)
    private String servicesInclude; 

    @Column(nullable = true)
    @OneToMany(mappedBy = "information",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Photo> photos; 

    

}
