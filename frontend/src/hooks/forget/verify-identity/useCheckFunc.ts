import { ref } from 'vue';

export default function useCheckFunc(emits: (event: 'update:active', ...args: unknown[]) => void) {
  const emailPanel = ref(); // 获取组件实例

  // 身份验证
  async function check() {
    const isSuccess = await emailPanel.value?.login();
    if (isSuccess) emits('update:active'); // 运行“update:active”事件，从而触发父组件的事件，实现自动切换到下一个面板
  }

  return {
    emailPanel,
    emits,
    check
  };
}
