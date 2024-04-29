package com.gracefullyugly.domain.item.service;

import com.gracefullyugly.domain.category.entity.Category;
import com.gracefullyugly.domain.category.repository.CategoryRepository;
import com.gracefullyugly.domain.item.dto.ItemRequestDto;
import com.gracefullyugly.domain.item.entity.Item;
import com.gracefullyugly.domain.item.repository.ItemRepository;
import com.gracefullyugly.domain.user.entity.User;
import com.gracefullyugly.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    public ItemService(ItemRepository itemRepository, UserRepository userRepository, CategoryRepository categoryRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    public Item save(ItemRequestDto request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("not found categoryId : " + request.getCategoryId()));
        Item item = itemRepository.save(request.toEntity(category));
        return item;
    }

    public List<Item> findAllItems() {
        return itemRepository.findAll();
    }

    public Item findOneItem(Long itemId) {
        return itemRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException("not found itemId : " + itemId));
    }

    @Transactional
    public Item update(Long itemId, ItemRequestDto request) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("not found itemId : " + itemId));

        item.update(request.getDescription());
        return item;
    }

    public void deletedById(Long itemId) {
        itemRepository.deleteById(itemId);
    }


}
