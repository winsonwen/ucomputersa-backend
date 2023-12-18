package ucomputersa.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "menu")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id", unique = true, nullable = false)
    @Id
    private Integer menuId;

    @Column
    private String name;

    @Column
    private int parentMenuId;

    @Column
    private Integer menuLevel;

    @Column
    private Integer showStatus;

    @Column
    private Integer sort;

    @Column(nullable = false)
    private String icon;


}