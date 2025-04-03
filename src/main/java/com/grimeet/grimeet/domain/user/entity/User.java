package com.grimeet.grimeet.domain.user.entity;

import com.grimeet.grimeet.common.entity.BaseTime;
import com.grimeet.grimeet.domain.user.config.UserStatusConverter;
import com.grimeet.grimeet.domain.user.dto.UserStatus;
import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "USERS")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = true) // EqualsAndHashCode 어노테이션 추가, of = "id" 옵션 지정
public class User extends BaseTime {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_name", length = 20, nullable = false, unique = false, updatable = false)
    private String name;

    @Column(name = "user_email", length = 255, nullable = false, unique = true, updatable = false)
    private String email;

    @Column(name = "user_password", length = 255, nullable = false, unique = false, updatable = true)
    private String password;

    @Column(name = "user_nickname", length = 20, nullable = false, unique = true, updatable = true)
    private String nickname;

    @Column(name = "user_phone_number", length = 20, nullable = true, unique = true, updatable = true)
    private String phoneNumber;

    @Column(name = "user_status", nullable = false, updatable = true, columnDefinition = "TINYINT")
    @Convert(converter = UserStatusConverter.class)
    private UserStatus userStatus;

    @Column(name = "profile_image_url", length = 1000, nullable = true, unique = false, updatable = true)
    private String profileImageUrl;

    @Column(name = "profile_image_key", length = 500, nullable = true, unique = false, updatable = true)
    private String profileImageKey;

    public User(String name, String email, String password, String nickname, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.userStatus = UserStatus.NORMAL;
    }

    public User(String name, String email, String password, String nickname, String phoneNumber, String profileImageUrl) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.userStatus = UserStatus.NORMAL;
        this.profileImageUrl = profileImageUrl;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public void setProfileImageKey(String profileImageKey) {
        this.profileImageKey = profileImageKey;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
