package site.chatmanager.sender;

public interface EmailSender {

    // 发送邮箱验证码
    public boolean sendEmailVerificationCode(String email, String code);

}
