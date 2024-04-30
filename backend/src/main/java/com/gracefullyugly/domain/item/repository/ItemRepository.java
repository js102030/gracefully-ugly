package com.gracefullyugly.domain.item.repository;

import com.gracefullyugly.domain.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query("select i from Item i where i.closedDate >= CURRENT_TIMESTAMP and i.closedDate <= :endTime order by function('rand') ")
    List<Item> findRandomImpendingItems(LocalDateTime endTime);
}
