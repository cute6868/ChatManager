import type { FormDataTypeB, FormDataTypeD } from '@/types';
import { onBeforeUnmount, ref } from 'vue';
import throttle from '@/utils/throttle';
import { EMAIL_REGEX, THROTTLE_TIME } from '@/global/constant/rule';
import { getVerifyCodeRequest } from '@/service/api/login';

export default function useVerifyCode(form: FormDataTypeB | FormDataTypeD) {
  const second = ref(60); // 倒计时的秒数
  const flag = ref(false); // 显示倒计时的开关
  let timer: ReturnType<typeof setInterval> | undefined; // 存储定时器ID

  // 编写获取验证码的逻辑
  function getCode() {
    // 判断邮箱格式是否符合规范
    if (!EMAIL_REGEX.test(form.email)) {
      ElMessage({ message: '邮箱格式不正确', type: 'error' });
      return;
    }

    // 如果格式符合规范，则发送"获取验证码"请求
    getVerifyCodeRequest(form.email)
      .then((res) => {
        const code = res.data.code; // 业务状态码

        if (code === 0) {
          ElMessage({ message: res.data.msg, type: 'success' });

          // 显示倒计时60秒，并且期间不得再申请验证码
          second.value = 60; // 初始化
          flag.value = true; // 显示
          document.querySelector('.get-code')?.classList.add('disabled-element'); // 禁止操作

          // 进行倒计时
          timer = setInterval(() => {
            second.value--;
            if (second.value <= 0) {
              flag.value = false; // 隐藏
              document.querySelector('.get-code')?.classList.remove('disabled-element'); // 允许操作
              clearInterval(timer); // 清除定时器
            }
          }, 1000);
        } else {
          ElMessage({ message: res.data.msg, type: 'error' });
        }
      })
      .catch(() => {
        ElMessage({ message: '网络异常', type: 'error' }); // 一般情况下是网络异常
      });
  }

  // 组件卸载前清除定时器
  onBeforeUnmount(() => {
    if (timer) clearInterval(timer); // 如果倒计时过程中，用户跳转其他页面，就清除定时器
  });

  // 包装获取验证码的函数，实现节流
  const wrapGetCode = throttle(getCode, THROTTLE_TIME);

  return {
    flag,
    second,
    wrapGetCode
  };
}
