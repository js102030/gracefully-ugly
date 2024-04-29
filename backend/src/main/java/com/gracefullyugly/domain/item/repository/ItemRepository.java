package com.gracefullyugly.domain.item.repository;

import com.gracefullyugly.domain.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    @Modifying
    @Query("update Item i set i.description = :description where i.id = :itemId")
    void updateDescription(Long itemId, String description);

}
