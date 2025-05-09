import { reactive } from 'vue';
import type { FormRules } from 'element-plus';
import { ACCOUNT_REGEX, PASSWORD_REGEX, EMAIL_VERIFY_CODE_REGEX } from '@/global/constant/rule';
import { checkAccountRequest, checkEmailRequest } from '@/service/api/register';

export default function useForm() {
  // ========================= 表单数据 ==========================
  const formData = reactive({
    account: '',
    password: '',
    confirm: '',
    email: '',
    verifyCode: ''
  });

  // ========================= 校验函数 ==========================

  // 账号校验器
  function accountValidator(rule: unknown, value: string, callback: (error?: Error) => void) {
    if (!value) return callback(new Error('请输入账号'));
    checkAccountRequest(value)
      .then((res) => {
        const code = res.data.code; // 业务状态码

        if (code === 0) {
          return callback(); // 账号可用
        } else {
          return callback(res.data.msg); // 账号不可用,提示原因
        }
      })
      .catch(() => {
        return callback(); // 一般是网络异常,不用提示
      });
  }

  // 确认密码校验器
  function confirmPasswordValidator(
    rule: unknown,
    value: string,
    callback: (error?: Error) => void
  ) {
    if (value === formData.password) callback();
    else callback(new Error('两次输入的密码不一致'));
  }

  // 邮箱校验器
  function emailValidator(rule: unknown, value: string, callback: (error?: Error) => void) {
    if (!value) return callback(new Error('请输入邮箱'));
    checkEmailRequest(value)
      .then((res) => {
        const code = res.data.code;

        if (code === 0) {
          return callback();
        } else {
          return callback(new Error(res.data.msg));
        }
      })
      .catch(() => {
        return callback();
      });
  }

  // ========================= 表单校验 ==========================
  const formRules = reactive<FormRules>({
    account: [
      { required: true, message: '请设置您的账号', trigger: 'blur' },
      { min: 6, max: 20, message: '账号长度为6到20位', trigger: ['blur', 'change'] },
      { pattern: ACCOUNT_REGEX, message: '账号只能含有数字、字母', trigger: ['blur', 'change'] },
      { validator: accountValidator, trigger: 'blur' }
    ],
    password: [
      { required: true, message: '请输入您的密码', trigger: 'blur' },
      {
        pattern: PASSWORD_REGEX,
        message: '长度为8到32位，不能全是数字，可以包含字母、特殊符号',
        trigger: 'blur'
      }
    ],
    confirm: [
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
  });

  return {
    formData,
    formRules
  };
}
