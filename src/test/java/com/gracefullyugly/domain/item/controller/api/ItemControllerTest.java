package com.gracefullyugly.domain.item.controller.api;

import static com.gracefullyugly.testutil.SetupDataUtils.CATEGORY_ID;
import static com.gracefullyugly.testutil.SetupDataUtils.DESCRIPTION;
import static com.gracefullyugly.testutil.SetupDataUtils.ITEM_NAME;
import static com.gracefullyugly.testutil.SetupDataUtils.MIN_GROUP_BUY_WEIGHT;
import static com.gracefullyugly.testutil.SetupDataUtils.MIN_UNIT_WEIGHT;
import static com.gracefullyugly.testutil.SetupDataUtils.PRICE;
import static com.gracefullyugly.testutil.SetupDataUtils.PRODUCTION_PLACE;
import static com.gracefullyugly.testutil.SetupDataUtils.TEST_LOGIN_ID;
import static com.gracefullyugly.testutil.SetupDataUtils.TEST_NICKNAME;
import static com.gracefullyugly.testutil.SetupDataUtils.TOTAL_SALES_UNIT;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
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
import com.gracefullyugly.common.security.CustomUserDetails;
import com.gracefullyugly.common.security.jwt.JWTUtil;
import com.gracefullyugly.domain.groupbuy.service.GroupBuyService;
import com.gracefullyugly.domain.item.dto.ItemRequest;
import com.gracefullyugly.domain.item.dto.ItemResponse;
import com.gracefullyugly.domain.item.entity.Item;
import com.gracefullyugly.domain.item.enumtype.Category;
import com.gracefullyugly.domain.item.repository.ItemRepository;
import com.gracefullyugly.domain.item.service.ItemSearchService;
import com.gracefullyugly.domain.item.service.ItemService;
import com.gracefullyugly.domain.user.repository.UserRepository;
import com.gracefullyugly.testuserdetails.TestUserDetailsService;
import com.gracefullyugly.testutil.SetupDataUtils;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;


@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class ItemControllerTest {

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    private ItemService itemService;

    private TestUserDetailsService testUserDetailsService;
    private CustomUserDetails customUserDetails;

    @BeforeEach
    void setupTestData() {
        // 회원 정보 세팅
        userRepository.save(SetupDataUtils.makeTestSellerUser((passwordEncoder)));

        // 상품 정보 세팅
        List<ItemRequest> testItemData = SetupDataUtils.makeTestItemRequest();

        itemService.save(userRepository.findByNickname(TEST_NICKNAME).get().getId(), testItemData.get(0));
        itemService.save(userRepository.findByNickname(TEST_NICKNAME).get().getId(), testItemData.get(1));

        // UserDetails 세팅
        testUserDetailsService = new TestUserDetailsService(userRepository);
        customUserDetails = (CustomUserDetails) testUserDetailsService.loadUserByUsername(TEST_LOGIN_ID);
    }

    @AfterEach
    void deleteTestData() {
        userRepository.deleteAll();
        itemRepository.deleteAll();
    }

//    @Test
//    @DisplayName("판매글 생성 테스트")
//    void addItemTest() throws Exception {
//        // GIVEN
//        ItemRequest itemRequest = ItemRequest.builder()
//                .categoryId(Category.VEGETABLE)
//                .name("감자")
//                .productionPlace("강원도")
//                .closedDate(LocalDateTime.now().plusDays(1))
//                .minUnitWeight(3)
//                .price(7900)
//                .totalSalesUnit(20)
//                .minGroupBuyWeight(15)
//                .description("맛 좋은 감자")
//                .build();
//
//        Gson gson = new GsonBuilder()
//                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
//                .create();
//        String json = gson.toJson(itemRequest);
//
//        // THEN
//        mockMvc.perform(post("/api/items")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(json)
//                        .with(user(customUserDetails)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.name").value("감자"))
//                .andExpect(jsonPath("$.productionPlace").value("강원도"))
//                .andExpect(jsonPath("$.categoryId").value(Category.VEGETABLE.toString()))
//                .andExpect(jsonPath("$.minUnitWeight").value(3))
//                .andExpect(jsonPath("$.price").value(7900))
//                .andExpect(jsonPath("$.totalSalesUnit").value(20))
//                .andExpect(jsonPath("$.minGroupBuyWeight").value(15))
//                .andExpect(jsonPath("$.description").value("맛 좋은 감자"))
//                .andExpect(jsonPath("$.createdDate").exists())
//                .andExpect(jsonPath("$.lastModifiedDate").exists())
//                .andExpect(jsonPath("$.closedDate").exists())
//                .andDo(print());
//    }

    @Test
    @DisplayName("상품 목록 조회 테스트")
    void ShowItemsTest() throws Exception {
        // WHEN & THEN (상품 목록 조회 검증)
        mockMvc.perform(get("/api/all/items")
                        .with(user(customUserDetails)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value(ITEM_NAME))
                .andExpect(jsonPath("$[0].productionPlace").value(PRODUCTION_PLACE))
                .andExpect(jsonPath("$[0].minUnitWeight").value(MIN_UNIT_WEIGHT))
                .andExpect(jsonPath("$[0].price").value(PRICE))
                .andExpect(jsonPath("$[0].totalSalesUnit").value(TOTAL_SALES_UNIT))
                .andExpect(jsonPath("$[0].minGroupBuyWeight").value(MIN_GROUP_BUY_WEIGHT))
                .andExpect(jsonPath("$[0].description").value(DESCRIPTION))

                .andExpect(jsonPath("$[1].name").value(ITEM_NAME + 2))
                .andExpect(jsonPath("$[1].productionPlace").value(PRODUCTION_PLACE + 2))
                .andExpect(jsonPath("$[1].minUnitWeight").value(MIN_UNIT_WEIGHT + 1000))
                .andExpect(jsonPath("$[1].price").value(PRICE + 10000))
                .andExpect(jsonPath("$[1].totalSalesUnit").value(TOTAL_SALES_UNIT + 5))
                .andExpect(jsonPath("$[1].minGroupBuyWeight").value(MIN_GROUP_BUY_WEIGHT + 5000))
                .andExpect(jsonPath("$[1].description").value(DESCRIPTION + 2));
    }

    @Test
    @DisplayName("판매글 상세 조회 테스트")
    void showOneItemTest() throws Exception {
        // GIVEN
        Long testUserId = userRepository.findByNickname(TEST_NICKNAME).get().getId();

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

        ItemRequest request = new ItemRequest(item1.getName(), item1.getProductionPlace(), item1.getCategoryId(),
                item1.getClosedDate(), item1.getMinUnitWeight(), item1.getPrice(), item1.getTotalSalesUnit(),
                item1.getMinUnitWeight(), item1.getDescription());

        ItemResponse expectedResponse = itemService.save(testUserId, request);

        // WHEN & THEN
        mockMvc.perform(get("/api/all/items/{itemId}", expectedResponse.getId())
                        .with(user(customUserDetails)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(expectedResponse.getId()))
                .andExpect(jsonPath("$.name").value(expectedResponse.getName()))
                .andExpect(jsonPath("$.productionPlace").value(expectedResponse.getProductionPlace()))
                .andExpect(jsonPath("$.minUnitWeight").value(expectedResponse.getMinUnitWeight()))
                .andExpect(jsonPath("$.price").value(expectedResponse.getPrice()))
                .andExpect(jsonPath("$.totalSalesUnit").value(expectedResponse.getTotalSalesUnit()))
                .andExpect(jsonPath("$.minGroupBuyWeight").value(expectedResponse.getMinGroupBuyWeight()))
                .andExpect(jsonPath("$.description").value(expectedResponse.getDescription()));
    }

    private String getToken() {
        return jwtUtil.createJwt(100L, "loginId", "ROLE_SELLER", 60 * 10 * 1000L, null);
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