package com.example.ordervalidation.order;

import com.example.ordervalidation.portfolio.Portfolio;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Service
@AllArgsConstructor
@ToString
@Setter
@Table
@Entity(name="orders")
public class Orders {
    @Id
    @SequenceGenerator(
            name= "order_sequence",
            sequenceName = "order_sequence",
            allocationSize = 1
    )

    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "order_sequence"
    )
    @Column(
            nullable=false,
            updatable = false
    )
    private Long id;

    private String product;

    private int quantity;

    private Double price;

    private String status;

    private String side;

    @ManyToOne
    @JoinColumn(name="portfolio_id")
    @JsonBackReference
    private Portfolio portfolio;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}

