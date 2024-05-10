package com.gracefullyugly.domain.item.repository;

import com.gracefullyugly.domain.item.dto.ItemWithImageUrlResponse;
import com.gracefullyugly.domain.item.entity.Item;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query(value = """
            SELECT i.item_id as id, i.user_id as userId, i.name as name, i.production_place as productionPlace,
                   i.category_id as category, i.closed_date as closedDate, i.created_date as createdDate,
                   i.last_modified_date as last_modified_date, i.min_unit_weight as minUnitWeight, i.price as price,
                   i.total_sales_unit as totalSalesUnit, i.min_group_buy_weight as minGroupBuyWeight,
                   i.description as description, img.url as imageUrl
            FROM item i
            LEFT JOIN image img ON i.item_id = img.item_id AND img.is_deleted = false
            WHERE i.item_id = :itemId AND i.is_deleted = false
            """, nativeQuery = true)
    ItemWithImageUrlResponse findOneItemWithImage(@Param("itemId") Long itemId);

    @Query(value = """
            SELECT i.item_id as id, i.user_id as userId, i.name as name, i.production_place as productionPlace,
                   i.category_id as category, i.closed_date as closedDate, i.created_date as createdDate,
                   i.last_modified_date as last_modified_date, i.min_unit_weight as minUnitWeight, i.price as price,
                   i.total_sales_unit as totalSalesUnit, i.min_group_buy_weight as minGroupBuyWeight,
                   i.description as description, img.url as imageUrl
            FROM item i
            LEFT JOIN image img ON i.item_id = img.item_id AND img.is_deleted = false
            WHERE i.is_deleted = false
            """, nativeQuery = true)
    List<ItemWithImageUrlResponse> findAllItemsWithImages();


    @Query(value = """
            SELECT i.item_id as id, i.user_id as userId, i.name as name, i.production_place as productionPlace,
                   i.category_id as category, i.closed_date as closedDate, i.created_date as createdDate,
                   i.last_modified_date as last_modified_date, i.min_unit_weight as minUnitWeight, i.price as price,
                   i.total_sales_unit as totalSalesUnit, i.min_group_buy_weight as minGroupBuyWeight,
                   i.description as description, img.url as imageUrl
            FROM item i
            LEFT JOIN image img ON i.item_id = img.item_id AND img.is_deleted = false
            WHERE i.closed_date >= CURRENT_TIMESTAMP AND i.closed_date <= :endTime
                  AND i.is_deleted = false
            ORDER BY RAND()
            """, nativeQuery = true)
    List<ItemWithImageUrlResponse> findRandomImpendingItems(LocalDateTime endTime);


    @Query(value = """
            SELECT i.item_id as id, i.user_id as userId, i.name as name, i.production_place as productionPlace, 
                   i.category_id as category, i.closed_date as closedDate, i.created_date as createdDate, 
                   i.last_modified_date as last_modified_date, i.min_unit_weight as minUnitWeight, i.price as price,
                   i.total_sales_unit as totalSalesUnit, i.min_group_buy_weight as minGroupBuyWeight, 
                   i.description as description, img.url as imageUrl
            FROM item i
            JOIN cart_item ci ON i.item_id = ci.item_id
            LEFT JOIN image img ON i.item_id = img.item_id AND img.is_deleted = false
            WHERE i.is_deleted = false
            GROUP BY i.item_id, i.user_id, i.name, i.production_place, i.category_id, i.closed_date, i.created_date, 
                     i.last_modified_date, i.min_unit_weight, i.price, i.total_sales_unit, i.min_group_buy_weight, 
                     i.description, img.url
            ORDER BY COUNT(ci.item_id) DESC
            LIMIT 3
            """, nativeQuery = true)
    List<ItemWithImageUrlResponse> findMostAddedToCartItems();


    @Query(value =
            "SELECT i.item_id as id, i.user_id as userId, i.name as name, i.production_place as productionPlace, " +
                    "i.category_id as category, i.closed_date as closedDate, i.created_date as createdDate, " +
                    "i.last_modified_date as last_modified_date, i.min_unit_weight as minUnitWeight, i.price as price, "
                    +
                    "i.total_sales_unit as totalSalesUnit, i.min_group_buy_weight as minGroupBuyWeight, " +
                    "i.description as description, img.url as imageUrl " +
                    "FROM item i " +
                    "LEFT JOIN image img ON i.item_id = img.item_id AND img.is_deleted = false " +
                    "WHERE i.category_id = :categoryId AND i.is_deleted = false" +
                    "ORDER BY i.item_id", nativeQuery = true)
    List<ItemWithImageUrlResponse> findCategoryItemsWithImageUrl(@Param("categoryId") String categoryId);

    @Query("SELECT I FROM Item AS I WHERE I.id = :itemId AND I.isDeleted = false AND I.closedDate >= CURRENT_TIMESTAMP")
    Optional<Item> findValidItemById(Long itemId);
}
