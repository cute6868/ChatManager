package site.chatmanager.utils;

import java.util.regex.Pattern;

public final class FormatChecker {
    // 账号长度在 6 到 20 个字符（闭区间），只能包含数字和字母
    private static final Pattern ACCOUNT_REGEX = Pattern.compile("^[a-zA-Z0-9]{6,20}$");

    // 验证各大邮箱厂商格式（长度200以内）
    //private static final Pattern EMAIL_REGEX = Pattern.compile("^(?=.{1,200}$)[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");

    // 验证qq邮箱格式（长度200以内）
    private static final Pattern EMAIL_REGEX = Pattern.compile("^(?=.{1,200}$)[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@qq\\.com$");

    // 密码长度在 8 到 32 个字符（闭区间），且不能全是数字，密码可以包含大小写字母、数字、特殊字符（如 ~!@#$%^&*()\-_=+${}|;:'",.<>?/\\ 等）
    private static final Pattern PASSWORD_REGEX = Pattern.compile("^(?!(?:\\d{8,32})$)[A-Za-z\\d\\W_~!@#$%^&*()\\-_=+${}|;:'\",.<>?/\\\\]{8,32}$");

    // 手机格式正则
    private static final Pattern CELLPHONE_REGEX = Pattern.compile("^1[3-9]\\d{9}$");

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

    // 验证码格式校验（邮箱、手机通用）
    public static boolean checkVerificationCode(String verificationCode) {
        if (verificationCode == null) return false;
        return verificationCode.length() == 6;
    }

    // 手机格式校验
    public static boolean checkCellphone(String cellphone) {
        if (cellphone == null) return false;
        return CELLPHONE_REGEX.matcher(cellphone).matches();
    }
}