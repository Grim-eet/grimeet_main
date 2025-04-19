package com.grimeet.grimeet.common.image;

public class ProfileImageUtils {

    public static final String DEFAULT_PROFILE_IMAGE_URL = "https://api.dicebear.com/9.x/notionists-neutral/svg?seed=";

    private ProfileImageUtils() {}

    public static String generateProfileImageUrl(String nickName) {
        return DEFAULT_PROFILE_IMAGE_URL + nickName;
    }

}
