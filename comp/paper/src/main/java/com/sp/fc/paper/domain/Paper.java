package com.sp.fc.paper.domain;

import com.sp.fc.config.TimeEntity;
import com.sp.fc.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "sp_paper")
public class Paper extends TimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paperId;

    private Long paperTemplateId;
    private String name;

    private Long studentUserId;

    @Transient
    private User user;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "paper")
    private List<PaperAnswer> paperAnswerList;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private PaperState state;

    public enum PaperState {
        READY,
        START,
        END,
        CANCELED
    }

    private int total;
    private int answered;
    private int correct;

    @Transient
    public double getScore() {
        if (total < 1) return 0;
        return correct * 100.0 / total;
    }

    public void addAnswered() {
        answered++;
    }

    public void addCorrect() {
        correct++;
    }

    public Map<Integer, PaperAnswer> answerMap() {
        if (paperAnswerList == null) return new HashMap<>();
        return paperAnswerList.stream().collect(Collectors.toMap(PaperAnswer::getNum,
            Function.identity()));
    }

}
