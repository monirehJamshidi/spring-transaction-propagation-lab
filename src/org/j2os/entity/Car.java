package org.j2os.entity;

import lombok.*;

import javax.persistence.*;

@Entity(name = "car")
@Table(name = "car")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Car {
    @Id@GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(columnDefinition = "VARCHAR2(20)")
    private String model;

    // ────────────────────────────────
    // رابطه ManyToOne با Person
    // ────────────────────────────────
    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;
}
