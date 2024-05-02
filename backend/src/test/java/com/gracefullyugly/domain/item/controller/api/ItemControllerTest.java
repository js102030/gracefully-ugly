package com.gracefullyugly.domain.item.controller.api;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gracefullyugly.domain.item.dto.ItemRequest;
import com.gracefullyugly.domain.item.dto.ItemResponse;
import com.gracefullyugly.domain.item.enumtype.Category;
import com.gracefullyugly.domain.item.service.ItemService;
import com.gracefullyugly.domain.user.repository.UserRepository;
import com.gracefullyugly.testutil.SetupDataUtils;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


@SpringBootTest
@AutoConfigureMockMvc
class ItemControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ItemService itemService;

    @Autowired
    ObjectMapper objectMapper;

    // 판매글 여러개 제작할 때 사용
    private ItemRequest createItemRequest(String name, String productionPlace, Category categoryId,
                                          LocalDateTime closedDate,
                                          int minUnitWeight, int price, int totalSalesUnit, int minGroupBuyWeight,
                                          String description) {
        return new ItemRequest(name, productionPlace, categoryId, closedDate, minUnitWeight, price, totalSalesUnit,
                minGroupBuyWeight, description);
    }

    private ResultActions performPostRequest(ItemRequest itemRequest) throws Exception {
        String content = objectMapper.writeValueAsString(itemRequest);
        return mockMvc.perform(MockMvcRequestBuilders.post("/api/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content));
    }

    @Test
    @DisplayName("판매글 생성 테스트")
    void addItemTest() throws Exception {
        // GIVEN
        String name = SetupDataUtils.ITEM_NAME;
        String productionPlace = SetupDataUtils.PRODUCTION_PLACE;
        Category categoryId = SetupDataUtils.CATEGORY_ID;
        LocalDateTime closedDate = SetupDataUtils.CLOSED_DATE;
        int minUnitWeight = SetupDataUtils.MIN_UNIT_WEIGHT;
        int price = SetupDataUtils.PRICE;
        int totalSalesUnit = SetupDataUtils.TOTAL_SALES_UNIT;
        int minGroupBuyWeight = SetupDataUtils.MIN_GROUP_BUY_WEIGHT;
        String description = SetupDataUtils.DESCRIPTION;

        ItemRequest itemRequest = new ItemRequest(name, productionPlace, categoryId, closedDate, minUnitWeight, price,
                totalSalesUnit, minGroupBuyWeight, description);
        String content = objectMapper.writeValueAsString(itemRequest);

        // WHEN
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        );
        // THEN
        resultActions.andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.price").value(price))
                .andExpect(jsonPath("$.description").value(description));
    }

    @Test
    @DisplayName("상품 생성 후 상품 목록 조회 테스트")
    void addItemAndShowItemsTest() throws Exception {
        // GIVEN
        ItemRequest itemRequest1 = createItemRequest("테스트용 이름1", "테스트용 생산지1", Category.FRUIT,
                LocalDateTime.now().plusDays(5), 3, 7900, 20, 15, "테스트용 내용");
        ItemRequest itemRequest2 = createItemRequest("테스트용 이름2", "테스트용 생산지2", Category.VEGETABLE,
                LocalDateTime.now().plusDays(10), 5, 150000, 50, 30, "테스트용 내용2");

        // WHEN
        ResultActions createAction1 = performPostRequest(itemRequest1);
        ResultActions createAction2 = performPostRequest(itemRequest2);

        // THEN
        createAction1.andExpect(status().isCreated());
        createAction2.andExpect(status().isCreated());

        // 상품 목록 조회
        ResultActions showAction = mockMvc.perform(MockMvcRequestBuilders.get("/api/items")
                .contentType(MediaType.APPLICATION_JSON));

        // THEN (상품 목록 조회 검증)
        showAction.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0].name").value(itemRequest1.getName()))
                .andExpect(jsonPath("$[0].price").value(itemRequest1.getPrice()))
                .andExpect(jsonPath("$[0].description").value(itemRequest1.getDescription()))
                .andExpect(jsonPath("$[0].categoryId").value(itemRequest1.getCategoryId().toString()))
                .andExpect(jsonPath("$[1].name").value(itemRequest2.getName()))
                .andExpect(jsonPath("$[1].price").value(itemRequest2.getPrice()))
                .andExpect(jsonPath("$[1].description").value(itemRequest2.getDescription()))
                .andExpect(jsonPath("$[1].categoryId").value(itemRequest2.getCategoryId().toString()));
    }

    @Test
    @DisplayName("판매글 상세 조회 테스트")
    void showOneItemTest() throws Exception {
        // GIVEN
        ItemRequest itemRequest = createItemRequest("테스트용 이름1", "테스트용 생산지1", Category.FRUIT,
                LocalDateTime.now().plusDays(5), 3, 7900, 20, 15, "테스트용 내용");
        ResultActions createAction = performPostRequest(itemRequest);
        createAction.andExpect(status().isCreated());

        String responseContent = createAction.andReturn().getResponse().getContentAsString();
        ItemResponse createdItemResponse = objectMapper.readValue(responseContent, ItemResponse.class);
        Long itemId = createdItemResponse.getId();

        // WHEN
        ResultActions showAction = mockMvc.perform(MockMvcRequestBuilders.get("/api/items/{itemId}", itemId)
                .contentType(MediaType.APPLICATION_JSON));

        // THEN
        showAction.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(itemId))
                .andExpect(jsonPath("$.name").value(itemRequest.getName()))
                .andExpect(jsonPath("$.productionPlace").value(itemRequest.getProductionPlace()))
                .andExpect(jsonPath("$.categoryId").value(itemRequest.getCategoryId().toString()))
                .andExpect(jsonPath("$.minUnitWeight").value(itemRequest.getMinUnitWeight()))
                .andExpect(jsonPath("$.price").value(itemRequest.getPrice()))
                .andExpect(jsonPath("$.totalSalesUnit").value(itemRequest.getTotalSalesUnit()))
                .andExpect(jsonPath("$.minGroupBuyWeight").value(itemRequest.getMinGroupBuyWeight()))
                .andExpect(jsonPath("$.description").value(itemRequest.getDescription()));
    }
}