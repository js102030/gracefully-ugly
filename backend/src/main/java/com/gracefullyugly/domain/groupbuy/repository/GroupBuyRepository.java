package com.gracefullyugly.domain.groupbuy.repository;

import com.gracefullyugly.domain.groupbuy.entity.GroupBuy;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupBuyRepository extends JpaRepository<GroupBuy, Long> {

    @Query(value =
        "SELECT * "
      + "FROM GroupBuy AS GB "
      + "WHERE GB.item_id = :itemId AND GB.group_buy_status = '진행중' AND GB.end_date > NOW()",
    nativeQuery = true)
    Optional<GroupBuy> findGroupBuyByItemId(Long itemId);
}
