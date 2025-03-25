package site.chatmanager.service.checker;

import java.util.regex.Pattern;

public class FormatChecker {
    private static final Pattern ACCOUNT_REGEX = Pattern.compile("^[a-zA-Z0-9]+$");
    private static final Pattern EMAIL_REGEX = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
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