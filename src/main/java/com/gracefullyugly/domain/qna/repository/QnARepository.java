package com.gracefullyugly.domain.qna.repository;

import com.gracefullyugly.domain.qna.dto.QnADto;
import com.gracefullyugly.domain.qna.entity.QnA;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface QnARepository extends JpaRepository<QnA, Long> {

    @Query("SELECT new com.gracefullyugly.domain.qna.dto.QnADto(q.id, q.userId, u.nickname, q.itemId, q.question, q.answer) "
            +
            "FROM QnA q JOIN User u ON q.userId = u.id " +
            "WHERE q.itemId = :itemId ORDER BY q.createdDate DESC")
    List<QnADto> findByItemIdOrderByCreatedDateDesc(@Param("itemId") Long itemId);
}
