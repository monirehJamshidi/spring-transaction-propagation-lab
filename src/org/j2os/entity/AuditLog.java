package org.j2os.entity;

import lombok.*;

import javax.persistence.*;

@Entity(name = "auditLog")
@Table(name = "auditLog")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String message;

    private Long timestamp;
}
