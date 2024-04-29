package com.gracefullyugly.domain.category.entity;

import com.gracefullyugly.common.base.BaseTimeEntity;
import com.gracefullyugly.domain.item.entity.Item;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class Category extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @OneToMany(mappedBy = "category", cascade = CascadeType.REMOVE)
    private List<Item> ItemList = new ArrayList<Item>();

    private String name;

}
