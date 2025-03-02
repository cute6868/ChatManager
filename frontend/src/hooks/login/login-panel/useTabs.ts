import type { Ref } from 'vue';
import type { TabPaneName } from 'element-plus';
import { ref } from 'vue';
import { localCache } from '@/utils/cache';

export default function useTabs(checkboxState: Ref<boolean>, CHECKBOX_STATE: string) {
  // 设置默认tab为"账号登录tab"
  const activeTabName = ref<TabPaneName>('byAccount');
  const isDisabled = ref<boolean>(false);
  // 改变tab时，执行以下操作
  function tabChangeHandler() {
    if (activeTabName.value === 'byEmail') {
      // 切换到邮箱登录tab时
      checkboxState.value = false; // 取消复选框的勾选
      isDisabled.value = true; // 禁用"记住密码"复选框和"忘记密码"链接
    } else {
      // 切换到账号登录tab时
      isDisabled.value = false; // 解封"记住密码"复选框和"忘记密码"链接
      checkboxState.value = localCache.getItem(CHECKBOX_STATE); // 恢复"复选框"原来的勾选状态
    }
  }

  return {
    activeTabName,
    isDisabled,
    tabChangeHandler
  };
}
