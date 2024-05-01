package com.gracefullyugly.domain.groupbuyuser.repository;

import com.gracefullyugly.domain.groupbuyuser.entity.GroupBuyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupBuyUserRepository extends JpaRepository<GroupBuyUser, Long> {

}
