package com.gracefullyugly.domain.user.repository;

import com.gracefullyugly.domain.user.dto.SellerDetailsResponse;
import com.gracefullyugly.domain.user.entity.User;
import com.gracefullyugly.domain.user.enumtype.SignUpType;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT U "
            + "FROM User AS U "
            + "WHERE U.loginId = :userLoginId AND U.isDeleted = false")
    Optional<User> findByLoginId(String userLoginId);

    /**
     * 소셜 타입과 소셜의 식별값으로 회원 찾는 메소드 정보 제공을 동의한 순간 DB에 저장해야하지만, 아직 추가 정보(사는 도시, 나이 등)를 입력받지 않았으므로 유저 객체는 DB에 있지만, 추가 정보가 빠진
     * 상태이다. 따라서 추가 정보를 입력받아 회원 가입을 진행할 때 소셜 타입, 식별자로 해당 회원을 찾기 위한 메소드
     */
    Optional<User> findBySignUpTypeAndSocialId(SignUpType signUpType, String socialId);

    Optional<User> findByNickname(String nickName);

    boolean existsByLoginId(String loginId);

    boolean existsByNickname(String nickname);

    boolean existsByEmail(String email);

    @Query(value = "SELECT u.nickname AS userNickname, " +
            "p.total_price AS totalPrice, " +
            "p.is_paid AS isPaid, " +
            "p.is_refunded AS isRefunded, " +
            "o.address AS address, " +
            "o.created_date AS orderDate " +
            "FROM orders o " +
            "LEFT JOIN orders_item oi ON o.order_id = oi.orders_id " +
            "LEFT JOIN payment p ON o.order_id = p.order_id " +
            "LEFT JOIN users u ON o.user_id = u.user_id " +
            "WHERE oi.item_id = :itemId", nativeQuery = true)
    List<SellerDetailsResponse> findSellerDetails(@Param("itemId") Long itemId);


}
