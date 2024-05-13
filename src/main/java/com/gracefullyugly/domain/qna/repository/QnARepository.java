package com.gracefullyugly.domain.qna.repository;

import com.gracefullyugly.domain.qna.entity.QnA;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QnARepository extends JpaRepository<QnA, Long> {

    List<QnA> findByItemId(Long itemId);

    List<QnA> findByItemIdOrderByCreatedDateDesc(Long itemId);
}
