package site.chatmanager.utils;

import java.util.regex.Pattern;

public final class FormatChecker {

    // 账号长度在 6 到 20 个字符（闭区间），只能包含数字和字母
    private static final Pattern ACCOUNT_REGEX = Pattern.compile("^[a-zA-Z0-9]{6,20}$");

    // 密码长度在 8 到 32 个字符（闭区间），且不能全是数字，密码可以包含大小写字母、数字、特殊字符（如 ~!@#$%^&*()\-_=+${}|;:'\",.<>?/\\ 等）
    private static final Pattern PASSWORD_REGEX = Pattern.compile("^(?!(?:\\d{8,32})$)[A-Za-z\\d\\W_~!@#$%^&*()\\-_=+${}|;:'\",.<>?/\\\\]{8,32}$");

    // 验证各大邮箱厂商格式（长度200以内）
    //private static final Pattern EMAIL_REGEX = Pattern.compile("^(?=.{1,200}$)[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");

    // 验证qq邮箱格式（长度200以内）
    private static final Pattern EMAIL_REGEX = Pattern.compile("^(?=.{1,200}$)[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@qq\\.com$");

    // 手机格式正则
    private static final Pattern CELLPHONE_REGEX = Pattern.compile("^1[3-9]\\d{9}$");

    // 验证码格式，由 6 位数字组成
    private static final Pattern VERIFICATION_CODE_REGEX = Pattern.compile("^[0-9]{6}$");

    // 昵称长度在 2 到 10 个字符（闭区间），可以包含中文、字母、数字和常用符号
    private static final Pattern NICKNAME_REGEX = Pattern.compile("^[\\u4e00-\\u9fa5a-zA-Z0-9_~!@#$%^&*()\\-+=<>?]{2,10}$");

    // 头像链接格式校验，简单校验以 http 或 https 开头
    private static final Pattern AVATAR_URL_REGEX = Pattern.compile("^https?://.*$");


    // 账号格式校验
    public static boolean checkAccount(String account) {
        if (account == null) return false;
        return ACCOUNT_REGEX.matcher(account).matches();
    }

    // 密码格式校验
    public static boolean checkPassword(String password) {
        if (password == null) return false;
        return PASSWORD_REGEX.matcher(password).matches();
    }

    // 邮箱格式校验
    public static boolean checkEmail(String email) {
        if (email == null) return false;
        return EMAIL_REGEX.matcher(email).matches();
    }

    // 手机格式校验
    public static boolean checkCellphone(String cellphone) {
        if (cellphone == null) return false;
        return CELLPHONE_REGEX.matcher(cellphone).matches();
    }

    // 验证码格式校验（包括：邮箱验证码、手机验证码）
    public static boolean checkVerifyCode(String verifyCode) {
        if (verifyCode == null) return false;
        return VERIFICATION_CODE_REGEX.matcher(verifyCode).matches();
    }

    // 昵称格式校验
    public static boolean checkNickname(String nickname) {
        if (nickname == null) return false;
        return NICKNAME_REGEX.matcher(nickname).matches();
    }

    // 头像链接格式校验
    public static boolean checkAvatarUrl(String avatarUrl) {
        if (avatarUrl == null) return false;
        return AVATAR_URL_REGEX.matcher(avatarUrl).matches();
    }
}