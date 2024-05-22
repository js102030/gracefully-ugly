package com.gracefullyugly.domain.groupbuy.repository;

import com.gracefullyugly.domain.groupbuy.dto.GroupBuyInfoResponse;
import com.gracefullyugly.domain.groupbuy.dto.GroupBuySelectDto;
import com.gracefullyugly.domain.groupbuy.entity.GroupBuy;
import com.gracefullyugly.domain.groupbuy.enumtype.GroupBuyStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupBuyRepository extends JpaRepository<GroupBuy, Long> {

    @Query(value =
            "SELECT GB.id AS groupBuyId, GB.itemId AS itemId, I.name AS itemName, GB.groupBuyStatus AS groupBuyStatus, COUNT(GBU.id) AS participantCount "
                    + "FROM GroupBuy AS GB "
                    + "LEFT OUTER JOIN Item AS I ON GB.itemId = I.id "
                    + "LEFT OUTER JOIN GroupBuyUser AS GBU ON GB.id = GBU.groupBuyId "
                    + "WHERE GB.id = :groupBuyId "
                    + "GROUP BY GB.id")
    Optional<GroupBuyInfoResponse> findGroupBuyById(Long groupBuyId);

    @Query(value =
            "SELECT GB.id AS groupBuyId, GB.itemId AS itemId, GB.groupBuyStatus AS groupBuyStatus, COUNT(GBU.id) AS participantCount "
                    + "FROM GroupBuy AS GB "
                    + "LEFT OUTER JOIN GroupBuyUser AS GBU ON GB.id = GBU.groupBuyId "
                    + "WHERE GB.itemId = :itemId "
                    + "GROUP BY GB.id "
                    + "ORDER BY GB.id DESC "
                    + "LIMIT 5")
    List<GroupBuySelectDto> findTop5ByItemIdOrderByIdDesc(Long itemId);

    @Query(value = "SELECT GB "
            + "FROM GroupBuy AS GB "
            + "WHERE GB.itemId = :itemId AND GB.groupBuyStatus = com.gracefullyugly.domain.groupbuy.enumtype.GroupBuyStatus.IN_PROGRESS")
    Optional<GroupBuy> findProgressGroupBuyByItemId(Long itemId);

    @Query("SELECT GB "
            + "FROM GroupBuy AS GB "
            + "LEFT OUTER JOIN GroupBuyUser AS GBU ON GB.id = GBU.groupBuyId "
            + "WHERE GBU.orderId = :orderId AND GB.groupBuyStatus = com.gracefullyugly.domain.groupbuy.enumtype.GroupBuyStatus.COMPLETED")
    List<GroupBuy> findCompletedGroupBuyByOrderId(Long orderId);

    @Modifying
    @Query(value = "UPDATE group_buy "
            + "SET group_buy_status = 'COMPLETED' "
            + "WHERE group_buy_id IN ("
            + "SELECT temp.group_buy_id "
            + "FROM ( "
            + "SELECT GB.group_buy_id "
            + "FROM group_buy AS GB "
            + "LEFT OUTER JOIN item AS I ON GB.item_id = I.item_id "
            + "LEFT OUTER JOIN group_buy_user AS GBU ON GB.group_buy_id = GBU.group_buy_id "
            + "WHERE GB.group_buy_id = :groupId AND "
            + "(SELECT SUM(GBU.quantity) FROM group_buy_user AS GBU WHERE GBU.group_buy_id = :groupId) >= (I.min_group_buy_weight / I.min_unit_weight)) AS temp)",
            nativeQuery = true)
    Integer updateGroupBuyStatusByGroupId(Long groupId);

    @Modifying
    @Query(value = "UPDATE group_buy gb " +
            "JOIN item i ON gb.item_id = i.item_id " +
            "SET gb.group_buy_status = 'CANCELLED' " +
            "WHERE gb.group_buy_status = 'IN_PROGRESS' " +
            "AND i.closed_date <= CURRENT_TIMESTAMP "
            , nativeQuery = true)
    void updateExpiredGroupBuyToCanceled();


    @Modifying
    @Query("UPDATE GroupBuy AS GB " +
            "SET GB.groupBuyStatus = com.gracefullyugly.domain.groupbuy.enumtype.GroupBuyStatus.CANCELLED " +
            "WHERE GB.itemId = :itemId AND GB.groupBuyStatus = com.gracefullyugly.domain.groupbuy.enumtype.GroupBuyStatus.IN_PROGRESS")
    void updateGroupBuyStatusToCanceledByItemId(Long itemId);

    Optional<GroupBuy> findByItemIdAndGroupBuyStatus(Long itemId, GroupBuyStatus status);
}
