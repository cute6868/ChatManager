import { reactive } from 'vue';
import { EMAIL_REGEX, EMAIL_VERIFY_CODE_REGEX, PASSWORD_REGEX } from '@/global/constant/rule';
import type { FormRules } from 'element-plus';

export default function useForm() {
  // ========================= 表单数据 ==========================
  const formData = reactive({
    newPassword: '',
    confirmPassword: '',
    email: '',
    verifyCode: ''
  });

  // ========================= 校验函数 ==========================
  // 确认密码校验器
  function confirmPasswordValidator(
    rule: unknown,
    value: string,
    callback: (error?: Error) => void
  ) {
    if (value === formData.newPassword) callback();
    else callback(new Error('两次输入的密码不一致'));
  }

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
    newPassword: [
      { required: true, message: '请输入新的密码', trigger: 'blur' },
      {
        pattern: PASSWORD_REGEX,
        message: '长度为8到32位，不能全是数字，可以包含字母、特殊符号',
        trigger: 'blur'
      }
    ],
    confirmPassword: [
      { required: true, message: '请再次输入密码', trigger: 'blur' },
      { validator: confirmPasswordValidator, trigger: 'blur' }
    ],
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
