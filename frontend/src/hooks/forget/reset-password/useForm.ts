import { reactive } from 'vue';
import { PASSWORD_REGEX } from '@/global/constant/rule';

export default function useForm() {
  // 表单数据
  const formData = reactive({
    password: '',
    confirm: ''
  });

  // 确认密码的校验逻辑
  function validatorFunc(rule: unknown, value: unknown, callback: (error?: Error) => void) {
    if (value === formData.password) callback();
    else callback(new Error('两次输入的密码不一致'));
  }

  // 表单的校验规则
  const formRules = reactive({
    password: [
      { required: true, message: '请输入新的密码', trigger: 'blur' },
      {
        pattern: PASSWORD_REGEX,
        message: '长度为8到32位，不能全是数字，可以包含字母、特殊符号',
        trigger: 'blur'
      }
    ],
    confirm: [
      { required: true, message: '请再次输入密码', trigger: 'blur' },
      { validator: validatorFunc, trigger: 'blur' }
    ]
  });

  return {
    formData,
    formRules
  };
}
