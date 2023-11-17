package com.sp.fc.paper.domain;

import com.sp.fc.config.TimeEntity;
import com.sp.fc.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "sp_paper_template")
public class PaperTemplate extends TimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paperTemplateId;

    private String name;

    private Long userId;

    @Transient
    private User creator;

    private int total;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "paperTemplateId")
    private List<Problem> problemList;

    private long publishedCount;

    private long completeCount;
}
