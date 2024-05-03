package com.gracefullyugly.domain.groupbuy.repository;

import com.gracefullyugly.domain.groupbuy.dto.GroupBuyInfoResponse;
import com.gracefullyugly.domain.groupbuy.dto.GroupBuySelectDto;
import com.gracefullyugly.domain.groupbuy.entity.GroupBuy;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupBuyRepository extends JpaRepository<GroupBuy, Long> {

    @Query(value =
        "SELECT GB.group_buy_id AS groupBuyId, GB.item_id AS itemId, I.name AS itemName, GB.group_buy_status AS groupBuyStatus, GB.end_date AS endDate, COUNT(GBU.group_buy_user_id) AS participantCount "
      + "FROM group_buy AS GB "
      + "LEFT OUTER JOIN item AS I ON GB.item_id = I.item_id "
      + "LEFT OUTER JOIN group_buy_user AS GBU ON GB.group_buy_id = GBU.group_buy_id "
      + "WHERE GB.group_buy_id = :groupBuyId",
    nativeQuery = true)
    Optional<GroupBuyInfoResponse> findGroupBuyById(Long groupBuyId);

    @Query(value =
        "SELECT GB.group_buy_id AS groupBuyId, GB.item_id AS itemId, GB.group_buy_status AS groupBuyStatus, GB.end_date AS endDate, COUNT(GBU.group_buy_user_id) AS participantCount "
      + "FROM group_buy AS GB "
      + "LEFT OUTER JOIN group_buy_user AS GBU ON GB.group_buy_id = GBU.group_buy_id "
      + "WHERE GB.item_id = :itemId "
      + "GROUP BY GB.group_buy_id "
      + "ORDER BY GB.group_buy_id DESC "
      + "LIMIT 5",
        nativeQuery = true)
    List<GroupBuySelectDto> findTop5ByItemIdOrderByIdDesc(Long itemId);

    @Query(value = "SELECT GB FROM GroupBuy AS GB WHERE GB.id = :itemId AND GB.groupBuyStatus = 'IN_PROGRESS' AND GB.endDate > CURRENT_TIMESTAMP")
    Optional<GroupBuy> findProgressGroupBuyByItemId(Long itemId);

    @Modifying
    @Query("UPDATE GroupBuy AS GB SET GB.groupBuyStatus = 'CANCELLED' WHERE GB.groupBuyStatus = 'IN_PROGRESS' AND GB.endDate <= CURRENT_TIMESTAMP")
    void updateExpiredGroupBuyToCanceled();
}
