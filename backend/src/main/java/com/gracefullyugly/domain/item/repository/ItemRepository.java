package com.gracefullyugly.domain.item.repository;

import com.gracefullyugly.domain.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {

}
