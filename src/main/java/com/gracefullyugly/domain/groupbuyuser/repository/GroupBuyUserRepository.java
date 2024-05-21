package com.gracefullyugly.domain.groupbuyuser.repository;

import com.gracefullyugly.domain.groupbuyuser.entity.GroupBuyUser;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupBuyUserRepository extends JpaRepository<GroupBuyUser, Long> {

    @Query("SELECT GBU "
            + "FROM GroupBuyUser AS GBU "
            + "LEFT OUTER JOIN GroupBuy AS GB ON GBU.groupBuyId = GB.id "
            + "WHERE GB.itemId = :itemId AND GBU.userId = :userId "
            + "ORDER BY GBU.id DESC LIMIT 5")
    List<GroupBuyUser> findByUserIdAndItemId(Long userId, Long itemId);

    @Modifying
    @Query(value = "DELETE FROM group_buy_user AS GBU "
            + "WHERE GBU.group_buy_user_id IN ("
            + "SELECT GBU.group_buy_user_id "
            + "FROM ("
            + "SELECT GBU.group_buy_user_id "
            + "FROM group_buy_user AS GBU "
            + "LEFT OUTER JOIN group_buy AS GB ON GB.group_buy_id = GBU.group_buy_id "
            + "LEFT OUTER JOIN item AS I ON I.item_id = GB.item_id "
            + "LEFT OUTER JOIN orders_item AS OI ON I.item_id = OI.item_id "
            + "WHERE GBU.user_id = :userId AND OI.orders_id = :orderId) "
            + "AS GBU)", nativeQuery = true)
    void deleteAllByUserIdAndOrderId(Long userId, Long orderId);

    @Query("SELECT GBU "
            + "FROM GroupBuyUser AS GBU "
            + "JOIN GroupBuy AS GB ON GBU.groupBuyId = GB.id "
            + "WHERE GB.itemId = :itemId "
            + "AND GB.id = :groupBuyId")
    List<GroupBuyUser> findGroupBuyUsersByItemIdAndGroupBuyId(Long itemId, Long groupBuyId);

}
