import { onBeforeUnmount, onMounted, ref } from 'vue';
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

  // =================== 新增功能：通过回车键实现点击登录按钮（start） ===================
  // 通过回车键实现点击登录按钮
  function handleKeydown(event: KeyboardEvent) {
    if (event.key === 'Enter') wrapLoginHandler();
  }

  // 组件挂载后执行
  onMounted(() => {
    document.addEventListener('keydown', handleKeydown); // 监听全局键盘事件
  });

  // 组件销毁时执行
  onBeforeUnmount(() => {
    document.removeEventListener('keydown', handleKeydown); // 移除全局键盘事件监听
  });

  // =================== 新增功能：通过回车键实现点击登录按钮（end） ===================
  return {
    accountPanel,
    emailPanel,
    wrapLoginHandler
  };
}
