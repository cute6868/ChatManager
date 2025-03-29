package site.chatmanager.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import site.chatmanager.mapper.QueryMapper;

@Component
public final class PresenceCheck implements ApplicationContextAware {

    private static QueryMapper queryMapper;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        queryMapper = applicationContext.getBean(QueryMapper.class);
    }

    // 账号存在性校验
    public static boolean checkAccount(String account) {
        return queryMapper.queryUidByAccount(account) != null;
    }

    // 邮箱存在性校验
    public static boolean checkEmail(String email) {
        String encryptEmail = EncryptionUtils.normalSecurityEncrypt(email);
        if (encryptEmail == null) return false;
        return queryMapper.queryUidByEmail(encryptEmail) != null;
    }

    // 手机存在性校验
    public static boolean checkCellphone(String cellphone) {
        String encryptCellphone = EncryptionUtils.normalSecurityEncrypt(cellphone);
        if (encryptCellphone == null) return false;
        return queryMapper.queryUidByCellphone(encryptCellphone) != null;
    }
}
