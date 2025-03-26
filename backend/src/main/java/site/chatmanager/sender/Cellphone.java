package site.chatmanager.sender;

public interface Cellphone {

    // 发送手机验证码
    public boolean sendCellphoneVerificationCode(String cellphone, String code);

}
