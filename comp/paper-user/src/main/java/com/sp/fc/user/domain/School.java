package com.sp.fc.user.domain;

import com.sp.fc.config.TimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "sp_school")
public class School extends TimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long schoolId;

    private String name;

    private String city;

    @Transient
    private Long teacherCount;

    @Transient
    private Long studentCount;
}
