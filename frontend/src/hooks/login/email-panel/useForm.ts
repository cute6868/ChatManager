import { reactive } from 'vue';
import type { FormRules } from 'element-plus';
import { EMAIL_REGEX, EMAIL_VERIFY_CODE_REGEX } from '@/global/constant/rule';

export default function useForm() {
  // ========================= 表单数据 ==========================
  const formData = reactive({
    email: '',
    verifyCode: ''
  });

  // ========================= 校验函数 ==========================
  // 邮箱校验器
  function emailValidator(rule: unknown, value: string, callback: (error?: Error) => void) {
    // 判断是否输入空
    if (!value) return callback(new Error('请输入邮箱'));

    // 判断邮箱格式是否正确
    if (!EMAIL_REGEX.test(value)) return callback(new Error('请输入正确的邮箱地址'));

    // 校验通过
    return callback();
  }

  // ========================= 表单校验 ==========================
  const formRules: FormRules = {
    email: [
      { required: true, message: '请输入邮箱', trigger: 'blur' },
      { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' },
      { validator: emailValidator, trigger: 'blur' }
    ],
    verifyCode: [
      { required: true, message: '请输入验证码', trigger: 'blur' },
      { min: 6, max: 6, message: '验证码长度为6位', trigger: ['blur', 'change'] },
      {
        pattern: EMAIL_VERIFY_CODE_REGEX,
        message: '验证码只能是数字',
        trigger: ['blur', 'change']
      }
    ]
  };

  return {
    formData,
    formRules
  };
}
