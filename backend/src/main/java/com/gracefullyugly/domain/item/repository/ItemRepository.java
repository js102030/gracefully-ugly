package com.gracefullyugly.domain.item.repository;

import com.gracefullyugly.domain.item.entity.Item;
import com.gracefullyugly.domain.item.enumtype.Category;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query("select i from Item i where i.closedDate >= CURRENT_TIMESTAMP and i.closedDate <= :endTime order by function('rand') ")
    List<Item> findRandomImpendingItems(LocalDateTime endTime);

    @Query(value = """
                SELECT i.*
                FROM item i
                JOIN cart_Item ci ON i.item_id = ci.item_id
                GROUP BY i.item_id
                ORDER BY COUNT(ci.item_id) DESC
                LIMIT 3
            """, nativeQuery = true)
    List<Item> findMostAddedToCartItems();

    @Query("select i from Item i where i.categoryId = :categoryId order by i.id")
    List<Item> findCategoryItems(Category categoryId);

    @Query("SELECT I FROM Item AS I WHERE I.id = :itemId AND I.isDeleted = false AND I.closedDate >= CURRENT_TIMESTAMP")
    Optional<Item> findValidItemById(Long itemId);
}
