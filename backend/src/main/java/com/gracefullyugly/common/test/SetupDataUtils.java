package com.gracefullyugly.common.test;

import com.gracefullyugly.domain.item.dto.ItemRequest;
import com.gracefullyugly.domain.item.enumtype.Category;
import com.gracefullyugly.domain.user.entity.User;
import com.gracefullyugly.domain.user.enumtype.Role;
import com.gracefullyugly.domain.user.enumtype.SignUpType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class SetupDataUtils {

    // 테스트용 유저 데이터 Input
    public static final String TEST_LOGIN_ID = "testId";
    public static final String PASSWORD = "testPassword";
    public static final String TEST_NICKNAME = "testNickname";
    public static final String TEST_EMAIL = "test@test.com";
    public static final String TEST_ADDRESS = "testAddress";
    public static final String TEST_PHONE_NUMBER = "01012345678";
    public static final Role TEST_ROLE = Role.BUYER;
    public static final String NEW_NICKNAME = "newNickname";
    public static final String TEST_PASSWORD = "test";

    // 테스트용 상품 데이터 Input
    public final static String NAME = "테스트용 이름";
    public final static String PRODUCTION_PLACE = "테스트용 생산지";
    public final static Category CATEGORY_ID = Category.VEGETABLE;
    public final static LocalDateTime CLOSED_DATE = LocalDateTime.now().plusDays(1);
    public final static int MIN_UNIT_WEIGHT = 1000;
    public final static int PRICE = 10000;
    public final static int TOTAL_SALES_UNIT = 10;
    public final static int MIN_GROUP_BUY_WEIGHT = 5000;
    public final static String DESCRIPTION = "테스트용 내용";

    // Output 메시지
    public static final String ID_VALID_MESSAGE = "아이디 입력은 필수입니다.";
    public static final String ROLE_VALID_MESSAGE = "역할은 필수입니다.";
    public static final String NICKNAME_VALID_MESSAGE = "닉네임 입력은 필수입니다.";
    public static final String EMAIL_VALID_MESSAGE = "이메일 입력은 필수입니다.";
    public static final String ADDRESS_VALID_MESSAGE = "주소 입력은 필수입니다.";
    public static final String PASSWORD_VALID_MESSAGE = "비밀번호 입력은 필수입니다.";
    public final static String ADD_CART_ITEM_SUCCESS = "해당 상품이 찜 목록에 추가되었습니다.";
    public final static String ADD_CART_ITEM_FAIL = "회원 정보가 존재하지 않습니다.";
    public final static String DELETE_CART_ITEM_SUCCESS = "해당 상품이 찜 목록에서 삭제되었습니다.";
    public final static String DELETE_CART_ITEM_NOT_FOUND_CART = "찜 목록이 아직 생성되지 않았습니다.";
    public final static String DELETE_CART_ITEM_NOT_FOUND_ITEM = "해당 상품이 찜 목록에 존재하지 않습니다.";
    public final static String CREATE_ORDER_SUCCESS = "주문이 정상적으로 저장되었습니다.";
    public final static String CREATE_ORDER_NOT_FOUND_USER = "회원 정보가 존재하지 않습니다.";

    private SetupDataUtils() {}

    public static User makeTestUser(BCryptPasswordEncoder passwordEncoder) {
        return new User(
            null,
            SignUpType.GENERAL,
            Role.BUYER,
            TEST_LOGIN_ID,
            passwordEncoder.encode(PASSWORD),
            TEST_NICKNAME,
            TEST_EMAIL,
            TEST_ADDRESS,
            false,
            false,
            false);
    }

    public static List<ItemRequest> makeTestItemRequest() {
        List<ItemRequest> retVal = new ArrayList<>();

        retVal.add(ItemRequest.builder()
            .name(NAME)
            .productionPlace(PRODUCTION_PLACE)
            .categoryId(CATEGORY_ID)
            .closedDate(CLOSED_DATE)
            .minUnitWeight(MIN_UNIT_WEIGHT)
            .price(PRICE)
            .totalSalesUnit(TOTAL_SALES_UNIT)
            .minGroupBuyWeight(MIN_GROUP_BUY_WEIGHT)
            .description(DESCRIPTION).build());

        retVal.add(ItemRequest.builder()
            .name(NAME + 2)
            .productionPlace(PRODUCTION_PLACE + 2)
            .categoryId(CATEGORY_ID)
            .closedDate(CLOSED_DATE)
            .minUnitWeight(MIN_UNIT_WEIGHT + 1000)
            .price(PRICE + 10000)
            .totalSalesUnit(TOTAL_SALES_UNIT + 5)
            .minGroupBuyWeight(MIN_GROUP_BUY_WEIGHT + 5000)
            .description(DESCRIPTION + 2).build());

        return retVal;
    }
}
