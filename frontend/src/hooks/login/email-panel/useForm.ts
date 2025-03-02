import { reactive } from 'vue';
import type { FormRules } from 'element-plus';

export default function useForm() {
  // 表单数据
  const form = reactive({
    email: '',
    verificationCode: ''
  });

  // 表单验证规则
  const rules: FormRules = {
    email: [
      { required: true, message: '请输入邮箱', trigger: 'blur' },
      { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
    ],
    verificationCode: [
      { required: true, message: '请输入验证码', trigger: 'blur' },
      { min: 6, max: 6, message: '验证码长度为6位', trigger: 'change' },
      { pattern: /^[0-9]+$/, message: '验证码只能是数字', trigger: 'change' }
    ]
  };

  return {
    form,
    rules
  };
}
