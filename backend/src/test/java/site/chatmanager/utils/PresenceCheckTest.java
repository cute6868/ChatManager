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
    @DisplayName("测试账号存在性校验 - 账号存在")
    public void testCheckAccount_Exists() {
        String account = "testAccount";
        when(queryMapper.queryUidByAccount(account)).thenReturn(1L);

        assertTrue(presenceCheck.checkAccount(account), "账号应存在");
    }

    @Test
    @DisplayName("测试账号存在性校验 - 账号不存在")
    public void testCheckAccount_NotExists() {
        String account = "nonExistentAccount";
        when(queryMapper.queryUidByAccount(account)).thenReturn(null);

        assertFalse(presenceCheck.checkAccount(account), "账号不应存在");
    }

    @Test
    @DisplayName("测试邮箱存在性校验 - 邮箱存在")
    public void testCheckEmail_Exists() {
        String email = "test@example.com";
        when(queryMapper.queryUidByEmail(email)).thenReturn(1L);

        assertTrue(presenceCheck.checkEmail(email), "邮箱应存在");
    }

    @Test
    @DisplayName("测试邮箱存在性校验 - 邮箱不存在")
    public void testCheckEmail_NotExists() {
        String email = "nonexistent@example.com";
        when(queryMapper.queryUidByEmail(email)).thenReturn(null);

        assertFalse(presenceCheck.checkEmail(email), "邮箱不应存在");
    }

    @Test
    @DisplayName("测试手机存在性校验 - 手机存在")
    public void testCheckCellphone_Exists() {
        String cellphone = "1234567890";
        when(queryMapper.queryUidByCellphone(cellphone)).thenReturn(1L);

        assertTrue(presenceCheck.checkCellphone(cellphone), "手机应存在");
    }

    @Test
    @DisplayName("测试手机存在性校验 - 手机不存在")
    public void testCheckCellphone_NotExists() {
        String cellphone = "0987654321";
        when(queryMapper.queryUidByCellphone(cellphone)).thenReturn(null);

        assertFalse(presenceCheck.checkCellphone(cellphone), "手机不应存在");
    }
}
