package com.example.ordervalidation.portfolio;

import com.example.ordervalidation.Client.Client;
import com.example.ordervalidation.order.Orders;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Service
@AllArgsConstructor
@ToString
@Setter
@NoArgsConstructor
@Table
@Entity(name="portfolios")
public class Portfolio {
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
    @Column(
            nullable=false,
            updatable = false
    )
    private String name;
    @JsonIgnore
    private String clientEmail;
    private LocalDateTime createdAt;
    @JsonIgnore
    @OneToMany(mappedBy = "portfolio")
    private List<Orders> orders;
    @ManyToOne
    @JoinColumn(name="client_id")
    @JsonBackReference
    private Client client;


    public Portfolio(String name, LocalDateTime createdAt, Client client, String clientEmail) {
        this.name = name;
        this.createdAt = createdAt;
        this.client = client;
        this.clientEmail = clientEmail;
    }

}