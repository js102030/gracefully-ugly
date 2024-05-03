package com.gracefullyugly.domain.item.controller.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.gracefullyugly.common.security.jwt.JWTUtil;
import com.gracefullyugly.domain.item.dto.ItemDtoUtil;
import com.gracefullyugly.domain.item.dto.ItemRequest;
import com.gracefullyugly.domain.item.dto.ItemResponse;
import com.gracefullyugly.domain.item.entity.Item;
import com.gracefullyugly.domain.item.enumtype.Category;
import com.gracefullyugly.domain.item.service.ItemSearchService;
import com.gracefullyugly.domain.item.service.ItemService;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
class ItemControllerTest {

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemSearchService itemSearchService;

    @MockBean
    private ItemService itemService;

    @Test
    @DisplayName("판매글 생성 테스트")
    void addItemTest() throws Exception {
        // GIVEN
        Long testItemId = 1L;
        Long testUserId = 1L;

        ItemRequest itemRequest = ItemRequest.builder()
                .categoryId(Category.VEGETABLE)
                .name("감자")
                .productionPlace("강원도")
                .closedDate(LocalDateTime.now().plusDays(1))
                .minUnitWeight(3)
                .price(7900)
                .totalSalesUnit(20)
                .minGroupBuyWeight(15)
                .description("맛 좋은 감자")
                .build();

        ItemResponse itemResponse = ItemResponse.builder()
                .id(testItemId)
                .userId(testUserId)
                .name("감자")
                .productionPlace("강원도")
                .createdDate(LocalDateTime.now())
                .closedDate(LocalDateTime.now().plusDays(1))
                .lastModifiedDate(LocalDateTime.now())
                .categoryId(Category.VEGETABLE)
                .minUnitWeight(3)
                .price(7900)
                .totalSalesUnit(20)
                .minGroupBuyWeight(15)
                .description("맛 좋은 감자")
                .build();

        given(itemService.save(any(), any())).willReturn(itemResponse);

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
        String json = gson.toJson(itemRequest);

        String access = getToken();

        // THEN
        mockMvc.perform(post("/api/items")
                        .header("access", access)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(testItemId))
                .andExpect(jsonPath("$.userId").value(testUserId))
                .andExpect(jsonPath("$.name").value("감자"))
                .andExpect(jsonPath("$.productionPlace").value("강원도"))
                .andExpect(jsonPath("$.categoryId").value(Category.VEGETABLE.toString()))
                .andExpect(jsonPath("$.minUnitWeight").value(3))
                .andExpect(jsonPath("$.price").value(7900))
                .andExpect(jsonPath("$.totalSalesUnit").value(20))
                .andExpect(jsonPath("$.minGroupBuyWeight").value(15))
                .andExpect(jsonPath("$.description").value("맛 좋은 감자"))
                .andExpect(jsonPath("$.createdDate").exists())
                .andExpect(jsonPath("$.lastModifiedDate").exists())
                .andExpect(jsonPath("$.closedDate").exists())
                .andDo(print());
    }

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

    private String getToken() {
        return jwtUtil.createJwt("access", 100L, "loginId", "ROLE_SELLER", 60 * 10 * 1000L);
    }
}

class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public void write(JsonWriter out, LocalDateTime value) throws IOException {
        out.value(value.format(formatter));
    }

    @Override
    public LocalDateTime read(JsonReader in) throws IOException {
        return LocalDateTime.parse(in.nextString(), formatter);
    }
}