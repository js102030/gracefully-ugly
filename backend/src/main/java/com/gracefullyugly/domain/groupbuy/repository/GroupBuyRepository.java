package com.gracefullyugly.domain.groupbuy.repository;

import com.gracefullyugly.domain.groupbuy.dto.GroupBuyInfoResponse;
import com.gracefullyugly.domain.groupbuy.dto.GroupBuySelectDto;
import com.gracefullyugly.domain.groupbuy.entity.GroupBuy;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
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

    @Query(value =
        "SELECT * "
      + "FROM group_buy AS GB "
      + "WHERE GB.item_id = :itemId AND GB.group_buy_status = '진행중' AND GB.end_date > NOW()",
    nativeQuery = true)
    Optional<GroupBuy> findProgressGroupBuyByItemId(Long itemId);
}
