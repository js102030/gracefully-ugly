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

    @Query("select i, ci.itemCount as salesCount from Item i inner join CartItem ci on i.id = ci.id group by i order by salesCount DESC limit 3")
    List<Item> findPopularityItems();

    @Query("select i from Item i where i.categoryId = :categoryId order by i.id")
    List<Item> findCategoryItems(Category categoryId);

    @Query("SELECT I FROM Item AS I WHERE I.id = :itemId AND I.isDeleted = false AND I.closedDate >= CURRENT_TIMESTAMP")
    Optional<Item> findValidItemById(Long itemId);
}
