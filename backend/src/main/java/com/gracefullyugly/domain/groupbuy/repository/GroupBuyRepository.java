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

    @Query(value = "SELECT GB.id AS groupBuyId, GB.itemId AS itemId, I.name AS itemName, GB.groupBuyStatus AS groupBuyStatus, GB.endDate AS endDate, COUNT(GBU.id) AS participantCount "
      + "FROM GroupBuy AS GB "
      + "LEFT OUTER JOIN Item AS I ON GB.itemId = I.id "
      + "LEFT OUTER JOIN GroupBuyUser AS GBU ON GB.id = GBU.groupBuyId "
      + "WHERE GB.id = :groupBuyId "
      + "GROUP BY GB.id")
    Optional<GroupBuyInfoResponse> findGroupBuyById(Long groupBuyId);

    @Query(value = "SELECT GB.id AS groupBuyId, GB.itemId AS itemId, GB.groupBuyStatus AS groupBuyStatus, GB.endDate AS endDate, COUNT(GBU.id) AS participantCount "
      + "FROM GroupBuy AS GB "
      + "LEFT OUTER JOIN GroupBuyUser AS GBU ON GB.id = GBU.groupBuyId "
      + "WHERE GB.itemId = :itemId "
      + "GROUP BY GB.id "
      + "ORDER BY GB.id DESC "
      + "LIMIT 5")
    List<GroupBuySelectDto> findTop5ByItemIdOrderByIdDesc(Long itemId);

    @Query(value = "SELECT GB "
            + "FROM GroupBuy AS GB "
            + "WHERE GB.id = :itemId AND GB.groupBuyStatus = com.gracefullyugly.domain.groupbuy.enumtype.GroupBuyStatus.IN_PROGRESS AND GB.endDate > CURRENT_TIMESTAMP")
    Optional<GroupBuy> findProgressGroupBuyByItemId(Long itemId);

    @Modifying
    @Query(value = "UPDATE group_buy AS GB "
            + "SET GB.group_buy_status = 'COMPLETED' "
            + "WHERE GB.group_buy_id IN ("
            + "SELECT GB.group_buy_id "
            + "FROM GB "
            + "LEFT OUTER JOIN item AS I ON GB.itemId = I.item_id "
            + "LEFT OUTER JOIN group_buy_user AS GBU ON GB.group_buy_id = GBU.group_buy_id "
            + "WHERE GB.group_buy_id = :groupId AND "
            + "I.price * ((SELECT SUM(GBU.quantity) FROM GBU WHERE GBU.group_buy_id = GB.group_buy_id) / I.min_unit_weight) >= I.min_group_buy_weight)",
            nativeQuery = true)
    void updateGroupBuyStatusByGroupId(Long groupId);

    @Modifying
    @Query("UPDATE GroupBuy AS GB "
            + "SET GB.groupBuyStatus = com.gracefullyugly.domain.groupbuy.enumtype.GroupBuyStatus.CANCELLED "
            + "WHERE GB.groupBuyStatus = com.gracefullyugly.domain.groupbuy.enumtype.GroupBuyStatus.IN_PROGRESS AND GB.endDate <= CURRENT_TIMESTAMP")
    void updateExpiredGroupBuyToCanceled();
}
