package site.chatmanager.utils;

import java.util.regex.Pattern;

public class FormatChecker {
    // 账号长度在 6 到 20 个字符（闭区间），只能包含数字和字母
    private static final Pattern ACCOUNT_REGEX = Pattern.compile("^[a-zA-Z0-9]{6,20}$");

    // 用于验证邮箱格式的正则表达式
    private static final Pattern EMAIL_REGEX = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");

    // 密码长度在 8 到 32 个字符（闭区间），且不能全是数字，密码可以包含大小写字母、数字、特殊字符（如 ~!@#$%^&*()\-_=+${}|;:'",.<>?/\\ 等）
    private static final Pattern PASSWORD_REGEX = Pattern.compile("^(?!(?:\\d{8,32})$)[A-Za-z\\d\\W_~!@#$%^&*()\\-_=+${}|;:'\",.<>?/\\\\]{8,32}$");


    // 账号格式校验
    public static boolean checkAccount(String account) {
        if (account == null) return false;
        return ACCOUNT_REGEX.matcher(account).matches();
    }

    // 邮箱格式校验
    public static boolean checkEmail(String email) {
        if (email == null) return false;
        return EMAIL_REGEX.matcher(email).matches();
    }

    // 密码格式校验
    public static boolean checkPassword(String password) {
        if (password == null) return false;
        return PASSWORD_REGEX.matcher(password).matches();
    }
}