package com.gracefullyugly.domain.qna.service;

import com.gracefullyugly.domain.qna.dto.QnADto;
import com.gracefullyugly.domain.qna.dto.QnADtoUtil;
import com.gracefullyugly.domain.qna.entity.QnA;
import com.gracefullyugly.domain.qna.repository.QnARepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class QnASearchService {

    private final QnARepository qnARepository;

    public QnA findById(Long id) {
        return qnARepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 QnA id" + id));
    }

    public QnADto getQnA(Long id) {
        QnA findQnA = findById(id);

        return QnADtoUtil.qnAToQnADto(findQnA);
    }

    public List<QnADto> getQnAList(Long itemId) {
        return qnARepository.findByItemIdOrderByCreatedDateDesc(itemId);
    }
}
