package site.chatmanager.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;
import site.chatmanager.mapper.QueryMapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PresenceCheckTest {

    @Mock
    private QueryMapper queryMapper;

    @InjectMocks
    private PresenceCheck presenceCheck;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        ApplicationContext applicationContext = mock(ApplicationContext.class);
        when(applicationContext.getBean(QueryMapper.class)).thenReturn(queryMapper);
        presenceCheck.setApplicationContext(applicationContext);
    }

    @Test
    @DisplayName("测试账号存在性校验 - 账号 'testAccount' 存在")
    void testCheckAccount_Exists_TestAccount() {
        String account = "testAccount";
        when(queryMapper.queryUidByAccount(account)).thenReturn(1L);

        assertTrue(PresenceCheck.checkAccount(account), "账号应存在");
    }

    @Test
    @DisplayName("测试账号存在性校验 - 账号 'nonExistentAccount' 不存在")
    void testCheckAccount_NotExists_NonExistentAccount() {
        String account = "nonExistentAccount";
        when(queryMapper.queryUidByAccount(account)).thenReturn(null);

        assertFalse(PresenceCheck.checkAccount(account), "账号不应存在");
    }

    @Test
    @DisplayName("测试邮箱存在性校验 - 邮箱 'test@example.com' 存在")
    void testCheckEmail_Exists_TestExampleCom() throws Exception {
        String email = "test@example.com";
        String encryptedEmail = EncryptionUtils.normalSecurityEncrypt(email);
        when(queryMapper.queryUidByEmail(encryptedEmail)).thenReturn(1L);

        assertTrue(PresenceCheck.checkEmail(email), "邮箱应存在");
    }

    @Test
    @DisplayName("测试邮箱存在性校验 - 邮箱 'nonexistent@example.com' 不存在")
    void testCheckEmail_NotExists_NonexistentExampleCom() throws Exception {
        String email = "nonexistent@example.com";
        String encryptedEmail = EncryptionUtils.normalSecurityEncrypt(email);
        when(queryMapper.queryUidByEmail(encryptedEmail)).thenReturn(null);

        assertFalse(PresenceCheck.checkEmail(email), "邮箱不应存在");
    }

    @Test
    @DisplayName("测试手机存在性校验 - 手机 '1234567890' 存在")
    void testCheckCellphone_Exists_1234567890() throws Exception {
        String cellphone = "1234567890";
        String encryptedCellphone = EncryptionUtils.normalSecurityEncrypt(cellphone);
        when(queryMapper.queryUidByCellphone(encryptedCellphone)).thenReturn(1L);

        assertTrue(PresenceCheck.checkCellphone(cellphone), "手机应存在");
    }

    @Test
    @DisplayName("测试手机存在性校验 - 手机 '0987654321' 不存在")
    void testCheckCellphone_NotExists_0987654321() throws Exception {
        String cellphone = "0987654321";
        String encryptedCellphone = EncryptionUtils.normalSecurityEncrypt(cellphone);
        when(queryMapper.queryUidByCellphone(encryptedCellphone)).thenReturn(null);

        assertFalse(PresenceCheck.checkCellphone(cellphone), "手机不应存在");
    }
}
