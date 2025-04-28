//package com.grimeet.grimeet.domain.user.service;
//
//import com.grimeet.grimeet.domain.user.dto.UserStatus;
//import com.grimeet.grimeet.domain.user.entity.User;
//import com.grimeet.grimeet.domain.user.repository.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@ActiveProfiles("test")
//@SpringBootTest
//@Transactional
//class UserServiceTest {
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    private User user1;
//    private User user2;
//
//    @BeforeEach
//    void setUp() {
//        user1 = userRepository.save(User.builder()
//                .name("홍길동")
//                .email("test1@example.com")
//                .password("encodedPassword1")
//                .nickname("길동이")
//                .phoneNumber("010-1111-1111")
//                .userStatus(UserStatus.NORMAL)
//                .build());
//
//        user2 = userRepository.save(User.builder()
//                .name("김철수")
//                .email("test2@example.com")
//                .password("encodedPassword2")
//                .nickname("철수")
//                .phoneNumber("010-2222-2222")
//                .userStatus(UserStatus.SOCIAL)
//                .build());
//    }
//
//    @DisplayName("정상_상태의_유저를_휴면으로_전환한다")
//    @Test
//    void 정상_상태의_유저를_휴면으로_전환한다() {
//        List<Long> ids = List.of(user1.getId(), user2.getId());
//
//        userService.updateUserStatusDormantBatch(ids);
//
//        List<User> updated = userRepository.findByIdInAndUserStatusIn(ids, List.of(UserStatus.NORMAL, UserStatus.SOCIAL));
//        for (User user : updated) {
//            assertEquals(UserStatus.DORMANT, user.getUserStatus());
//        }
//    }
//
//    @DisplayName("유저가_없는_경우에도_예외없이_동작한다")
//    @Test
//    void 유저가_없는_경우에도_예외없이_동작한다() {
//        // given
//        List<Long> emptyIds = new ArrayList<>();
//
//        // when
//        assertDoesNotThrow(() -> userService.updateUserStatusDormantBatch(emptyIds));
//
//        // then
//        // 별도 검증 없음, 예외 없이 끝났으면 성공
//    }
//
//}