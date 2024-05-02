package com.gracefullyugly.domain.item.controller.api;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gracefullyugly.domain.item.dto.ItemDtoUtil;
import com.gracefullyugly.domain.item.dto.ItemRequest;
import com.gracefullyugly.domain.item.dto.ItemResponse;
import com.gracefullyugly.domain.item.entity.Item;
import com.gracefullyugly.domain.item.enumtype.Category;
import com.gracefullyugly.domain.item.service.ItemSearchService;
import com.gracefullyugly.domain.item.service.ItemService;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


@SpringBootTest
@AutoConfigureMockMvc
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemSearchService itemSearchService;

//    @Test
//    @DisplayName("판매글 생성 테스트") //todo customUserDetails" is null
//    void addItemTest() throws Exception {
//        // GIVEN
//        Long userId = 1L;
//        Long itemId = 1L;
//        ItemRequest request = ItemRequest.builder()
//                .name("감자")
//                .productionPlace("강원도")
//                .categoryId(Category.VEGETABLE)
//                .closedDate(LocalDateTime.now().plusDays(3))
//                .minUnitWeight(3)
//                .price(7900)
//                .totalSalesUnit(20)
//                .minGroupBuyWeight(15)
//                .description("맛 좋은 감자")
//                .build();
//
//        ItemResponse response = ItemResponse.builder()
//                .id(itemId)
//                .name("감자")
//                .productionPlace("강원도")
//                .categoryId(Category.VEGETABLE)
//                .closedDate(LocalDateTime.now().plusDays(3))
//                .minUnitWeight(3)
//                .price(7900)
//                .totalSalesUnit(20)
//                .minGroupBuyWeight(15)
//                .description("맛 좋은 감자")
//                .build();
//
//        // WHEN
//        when(itemService.save(userId, request)).thenReturn(response);
//
//        // THEN
//        mockMvc.perform(post("/api/items")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isCreated())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.id").value(response.getId()))
//                .andExpect(jsonPath("$.name").value(response.getName()))
//                .andExpect(jsonPath("$.description").value(response.getDescription()));
//    }

    @Test
    @DisplayName("상품 목록 조회 테스트")
    void ShowItemsTest() throws Exception {
        // GIVEN
        Item item1 = Item.builder()
                .name("감자")
                .productionPlace("강원도")
                .categoryId(Category.VEGETABLE)
                .closedDate(LocalDateTime.now().plusDays(3))
                .minUnitWeight(3)
                .price(7900)
                .totalSalesUnit(20)
                .minGroupBuyWeight(15)
                .description("맛 좋은 감자")
                .build();

        Item item2 = Item.builder()
                .name("고구마")
                .productionPlace("전라남도")
                .categoryId(Category.VEGETABLE)
                .closedDate(LocalDateTime.now().plusDays(7))
                .minUnitWeight(1)
                .price(5000)
                .totalSalesUnit(50)
                .minGroupBuyWeight(20)
                .description("맛있는 고구마 ~ ")
                .build();

        List<Item> itemList = Arrays.asList(item1, item2);

        List<ItemResponse> expectedResponses = itemList.stream()
                .map(ItemDtoUtil::itemToItemResponse)
                .toList();

        // WHEN
        when(itemSearchService.findAllItems()).thenReturn(itemList);


        // THEN (상품 목록 조회 검증)
        mockMvc.perform(get("/api/items")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(expectedResponses.size()))
                .andExpect(jsonPath("$[0].id").value(expectedResponses.get(0).getId()))
                .andExpect(jsonPath("$[0].name").value(expectedResponses.get(0).getName()))
                .andExpect(jsonPath("$[0].productionPlace").value(expectedResponses.get(0).getProductionPlace()))
                .andExpect(jsonPath("$[0].categoryId").value(expectedResponses.get(0).getCategoryId().toString()))
                .andExpect(jsonPath("$[0].minUnitWeight").value(expectedResponses.get(0).getMinUnitWeight()))
                .andExpect(jsonPath("$[0].price").value(expectedResponses.get(0).getPrice()))
                .andExpect(jsonPath("$[0].totalSalesUnit").value(expectedResponses.get(0).getTotalSalesUnit()))
                .andExpect(jsonPath("$[0].minGroupBuyWeight").value(expectedResponses.get(0).getMinGroupBuyWeight()))
                .andExpect(jsonPath("$[0].description").value(expectedResponses.get(0).getDescription()))

                .andExpect(jsonPath("$[1].id").value(expectedResponses.get(1).getId()))
                .andExpect(jsonPath("$[1].name").value(expectedResponses.get(1).getName()))
                .andExpect(jsonPath("$[1].productionPlace").value(expectedResponses.get(1).getProductionPlace()))
                .andExpect(jsonPath("$[1].categoryId").value(expectedResponses.get(1).getCategoryId().toString()))
                .andExpect(jsonPath("$[1].minUnitWeight").value(expectedResponses.get(1).getMinUnitWeight()))
                .andExpect(jsonPath("$[1].price").value(expectedResponses.get(1).getPrice()))
                .andExpect(jsonPath("$[1].totalSalesUnit").value(expectedResponses.get(1).getTotalSalesUnit()))
                .andExpect(jsonPath("$[1].minGroupBuyWeight").value(expectedResponses.get(1).getMinGroupBuyWeight()))
                .andExpect(jsonPath("$[1].description").value(expectedResponses.get(1).getDescription()));
    }

    @Test
    @DisplayName("판매글 상세 조회 테스트")
    void showOneItemTest() throws Exception {
        // GIVEN
        Long itemId = 1L;
        Item item1 = Item.builder()
                .id(itemId)
                .name("감자")
                .productionPlace("강원도")
                .categoryId(Category.VEGETABLE)
                .closedDate(LocalDateTime.now().plusDays(3))
                .minUnitWeight(3)
                .price(7900)
                .totalSalesUnit(20)
                .minGroupBuyWeight(15)
                .description("맛 좋은 감자")
                .build();

        ItemResponse expectedResponse = ItemDtoUtil.itemToItemResponse(item1);

        // WHEN
        when(itemSearchService.findOneItem(itemId)).thenReturn(expectedResponse);

        // THEN
        mockMvc.perform(get("/api/items/{itemId}", itemId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(expectedResponse.getId()))
                .andExpect(jsonPath("$.name").value(expectedResponse.getName()))
                .andExpect(jsonPath("$.productionPlace").value(expectedResponse.getProductionPlace()))
                .andExpect(jsonPath("$.categoryId").value(expectedResponse.getCategoryId().toString()))
                .andExpect(jsonPath("$.minUnitWeight").value(expectedResponse.getMinUnitWeight()))
                .andExpect(jsonPath("$.price").value(expectedResponse.getPrice()))
                .andExpect(jsonPath("$.totalSalesUnit").value(expectedResponse.getTotalSalesUnit()))
                .andExpect(jsonPath("$.minGroupBuyWeight").value(expectedResponse.getMinGroupBuyWeight()))
                .andExpect(jsonPath("$.description").value(expectedResponse.getDescription()));
    }

}