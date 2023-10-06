package com.sp.fc.paper.repository;

import com.sp.fc.paper.domain.Paper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaperRepository extends JpaRepository<Paper, Long> {

    List<Paper> findAllByPaperTemplateIdAndStudentUserIdIn(Long paperTemplateId, List<Long> studentIdlist);

    List<Paper> findAllByPaperTemplateId(Long paperTemplateId);

    long countByPaperTemplateId(Long paperTemplateId);

    List<Paper> findAllByStudentUserIdOrderByCreatedDesc(Long studentUserId);

    long countByStudentUserId(Long studentUserId);

    Page<Paper> findAllByStudentUserIdAndStateOrderByCreatedDesc(Long studentUserId, Paper.PaperState state, Pageable pageable);

    List<Paper> findAllByStudentUserIdAndStateOrderByCreatedDesc(Long studentUserId, Paper.PaperState state);

    long countByStudentUserIdAndState(Long studentUserId, Paper.PaperState state);

    List<Paper> findAllByStudentUserIdAndStateInOrderByCreatedDesc(Long studentUserId, List<Paper.PaperState> states);

    long countByStudentUserIdAndStateIn(Long studentUserId, List<Paper.PaperState> states);
}