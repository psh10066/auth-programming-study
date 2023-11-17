package com.sp.fc.paper.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "sp_paper_answer")
@IdClass(PaperAnswer.PaperAnswerId.class)
public class PaperAnswer {

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "paperId")
    @Id
    Paper paper;

    @Id
    private Integer num; // 1-base

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Embeddable
    public static class PaperAnswerId implements Serializable {
        private Paper paper;
        private Integer num; // 1-base
    }

    private Long problemId;
    private String answer;
    private boolean correct;

    private LocalDateTime answered; // updatable
}
