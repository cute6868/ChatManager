import { queryRecordRequest } from '@/service/api/query';
import { ref } from 'vue';
import { UID } from '@/global/constant/login';
import { localCache } from '@/utils/cache';

export default function useCard() {
  // 声明类型
  interface RecordItem {
    question: string;
    time: string;
    sequence_num: number;
  }

  const uid = localCache.getItem(UID); // 用户id
  let isCardOpen = false; // 卡片显示状态
  const recordList = ref<RecordItem[]>([]); // 卡片内容列表

  // 切换卡片状态
  function toggleCard() {
    // 获取卡片元素
    const card = document.querySelector('#record-card') as HTMLElement | null;
    if (!card) return;

    // 修改显示状态
    isCardOpen = !isCardOpen;

    // 根据显示状态进行相应操作
    if (isCardOpen) {
      // 获取数据
      queryRecordRequest(uid)
        .then((res) => {
          const code = res.data.code;
          if (code === 0) {
            recordList.value = res.data.data;
          }
        })
        .catch(() => {
          ElMessage({ message: '网络异常', type: 'error', grouping: true });
        });

      card.style.left = '-6px'; // 显示卡片

      // 延迟绑定外部点击事件
      // 如果直接在 toggleSidebar() 中同步绑定事件，当前的点击事件会继续冒泡到 document，卡片刚打开就又被关闭
      setTimeout(() => {
        document.addEventListener('click', handleOutsideClick);
      }, 0);
    } else {
      card.style.left = '-320px'; // 隐藏卡片
      document.removeEventListener('click', handleOutsideClick);
    }
  }

  // 处理卡片外部点击事件
  function handleOutsideClick(e: MouseEvent) {
    // 获取卡片和图标元素
    const card = document.querySelector('#record-card') as HTMLElement | null;
    const icon = document.getElementById('record-icon') as HTMLElement | null;
    if (!card || !icon) return;

    // 将 e.target 断言为 Node 类型
    const target = e.target as Node | null;
    if (!target) return;

    if (!card.contains(target) && !icon.contains(target)) {
      // 说明点击发生在卡片和图标之外的区域
      isCardOpen = false;
      card.style.left = '-320px'; // 隐藏卡片
      document.removeEventListener('click', handleOutsideClick);
    } else {
      // 说明点击发生在卡片和图标内的区域
      // 检查点击的元素或其祖先是否是p标签（图标里面没有p标签，只有卡片有）
      let currentElement = target as HTMLElement;
      while (currentElement) {
        // 1.先判断点击元素是否是p标签
        if (currentElement.tagName === 'P') {
          // 1.1 获取p标签内的span元素
          const spanElement = currentElement.querySelector('span');
          if (spanElement) {
            // 1.2 获取span元素的文本内容
            const text = spanElement.textContent || '';

            // 复制文本到剪贴板
            navigator.clipboard
              .writeText(text)
              .then(() => {
                ElMessage({ message: '已复制到剪贴板', type: 'success', grouping: true });
              })
              .catch((error) => {
                console.info('复制失败:', error);
              });
          }

          break; // 找到p标签后停止遍历
        }

        // 2.如果不是，则向上遍历父元素
        if (currentElement.parentElement !== null) {
          currentElement = currentElement.parentElement;
        } else {
          break; // 父元素为null时停止遍历
        }
      }
    }
  }

  return {
    recordList,
    toggleCard
  };
}
