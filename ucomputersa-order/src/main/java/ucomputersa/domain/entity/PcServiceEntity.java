package ucomputersa.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import ucomputersa.constant.ServiceStatusEnum;
import ucomputersa.constant.ServiceTypeEnum;

import java.time.LocalDateTime;

@Entity
@Table(name = "pc_service")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PcServiceEntity {

    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "service_id", unique = true, nullable = false)
    @Id
    private String serviceId;

    @Column(name = "customer_id", unique = true, nullable = false)
    private String customerId;

    @Column(name = "service_type", nullable = false)
    private ServiceTypeEnum serviceType;

    @Column(name = "service_category", nullable = false)
    private String ServiceCategory;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate;
    @Column(name = "modification_date", nullable = false)
    private LocalDateTime lastModificationDate;

    @Column(name = "last_name", nullable = false)
    private String lastName;
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String email;

    @Column(name = "service_status",nullable = false)
    private ServiceStatusEnum serviceStatus;

    @Column
    private String description;

    @Column
    private String streetAddress;

    @Column
    private String additionalInfo;

    @Column
    private String city;

    @Column
    private String state;

    @Column
    private String zipcode;

}
