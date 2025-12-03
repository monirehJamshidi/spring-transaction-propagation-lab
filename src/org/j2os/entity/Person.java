package org.j2os.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity(name = "person")
@Table(name = "person")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Person {
    @Id@GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(columnDefinition = "VARCHAR2(20)")
    private String name;

    @Column(columnDefinition = "VARCHAR2(20)")
    private String family;

    // ───────────────────────────────────────────────
    // مهم: cascade = ALL  → باعث می‌شود Car ها همراه Person ذخیره شوند
    // mappedBy = "person" → ستون خارجی در car قرار دارد
    // ───────────────────────────────────────────────
    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Car> carList;
}
