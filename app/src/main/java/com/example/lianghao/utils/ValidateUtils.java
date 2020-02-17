package com.example.lianghao.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateUtils {

    // 设置邮箱的正则表达式
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    public static interface VolleyCallback{
        void onSuccessResponse(String result);
    }

}
