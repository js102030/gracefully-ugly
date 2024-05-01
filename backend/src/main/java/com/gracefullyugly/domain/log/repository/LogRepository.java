package com.gracefullyugly.domain.log.repository;

import com.gracefullyugly.domain.log.entity.Log;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<Log, Long> {

    List<Log> findByUserId(Long userId);

}
