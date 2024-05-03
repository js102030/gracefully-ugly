package com.gracefullyugly.domain.groupbuyuser.repository;

import com.gracefullyugly.domain.groupbuyuser.entity.GroupBuyUser;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupBuyUserRepository extends JpaRepository<GroupBuyUser, Long> {

    @Query("SELECT GBU "
         + "FROM GroupBuyUser AS GBU "
         + "LEFT OUTER JOIN GroupBuy AS GB ON GBU.groupBuyId = GB.id "
         + "WHERE GB.itemId = :itemId AND GB.groupBuyStatus = 'IN_PROGRESS' AND GBU.userId = :userId")
    Optional<GroupBuyUser> findByUserIdAndItemId(Long userId, Long itemId);
}
