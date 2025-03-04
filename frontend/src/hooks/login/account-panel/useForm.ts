import { reactive } from 'vue';
import { localCache } from '@/utils/cache';
import { ACCOUNT, PASSWORD } from '@/global/constant/login';
import type { FormRules } from 'element-plus';
import { ACCOUNT_REGEX, PASSWORD_REGEX } from '@/global/constant/rule';

export default function useForm() {
  // 表单数据
  const formData = reactive({
    account: localCache.getItem(ACCOUNT) ?? '',
    password: localCache.getItem(PASSWORD) ?? ''
  });

  // 表单校验规则
  const formRules: FormRules = {
    account: [
      { required: true, message: '请输入账号', trigger: 'blur' },
      { min: 6, max: 20, message: '账号长度为6到20位', trigger: 'change' },
      { pattern: ACCOUNT_REGEX, message: '账号只能包含数字和字母', trigger: 'change' }
    ],
    password: [
      { required: true, message: '请输入密码', trigger: 'blur' },
      {
        pattern: PASSWORD_REGEX,
        message: '密码格式错误',
        trigger: 'blur'
      }
    ]
  };

  return {
    formData,
    formRules
  };
}
