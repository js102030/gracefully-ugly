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
    @Query("UPDATE GroupBuy AS GB "
            + "SET GB.groupBuyStatus = com.gracefullyugly.domain.groupbuy.enumtype.GroupBuyStatus.CANCELLED "
            + "WHERE GB.groupBuyStatus = com.gracefullyugly.domain.groupbuy.enumtype.GroupBuyStatus.IN_PROGRESS AND GB.endDate <= CURRENT_TIMESTAMP")
    void updateExpiredGroupBuyToCanceled();
}
