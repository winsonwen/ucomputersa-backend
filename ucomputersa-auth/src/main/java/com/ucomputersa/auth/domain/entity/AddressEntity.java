package com.ucomputersa.auth.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "address")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id", unique = true, nullable = false)
    @Id
    private Integer addressId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")

    private CustomerEntity customerEntity;

    @Column(  nullable = false)
    private String streetAddress;

    @Column(  nullable = false)
    private String additionalInfo;

    @Column(  nullable = false)
    private String city;

    @Column(  nullable = false)
    private String state;

    @Column(  nullable = false)
    private String zipcode;



}