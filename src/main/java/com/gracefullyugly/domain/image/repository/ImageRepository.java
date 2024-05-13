package com.gracefullyugly.domain.image.repository;

import com.gracefullyugly.domain.image.entity.Image;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {

    String findItemImageByItemId(Long itemId);

    Optional<Image> findByItemId(Long itemId);
}
