import { ref } from 'vue';
import type { Ref } from 'vue';
import type { TabPaneName } from 'element-plus';
import debounce from '@/utils/debounce';

export default function useLoginButton(activeName: Ref<TabPaneName>, checkboxState: Ref<boolean>) {
  const accountPanel = ref(); // 获取AccountPanel组件的实例
  const emailPanel = ref(); // 获取EmailPanel组件的实例

  // 登录按钮，父组件不负责直接登录，而是将登录操作委托给具体的子组件
  function loginHandler() {
    if (activeName.value === 'byAccount') {
      accountPanel.value.login(checkboxState.value); // 调用AccountPanel组件的login方法，进行账号登录
    } else {
      emailPanel.value.login(); // 调用EmailPanel组件的login方法，进行邮箱登录
    }
  }

  // 引入防抖函数，包装 loginHandler，防止用户频繁点击按钮，执行多次登录逻辑
  const wrapLoginHandler = debounce(loginHandler, 500);

  return {
    accountPanel,
    emailPanel,
    wrapLoginHandler
  };
}
