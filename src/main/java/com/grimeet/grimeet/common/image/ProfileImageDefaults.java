package com.grimeet.grimeet.common.image;

public class ProfileImageDefaults {

    public static final String DEFAULT_PROFILE_IMAGE_URL = "https://api.dicebear.com/9.x/notionists-neutral/svg?seed=";

    private ProfileImageDefaults() {}

    public static String generateProfileImageUrl(String nickName) {
        return DEFAULT_PROFILE_IMAGE_URL + nickName;
    }

}
