import type { Reactive } from 'vue';

// 账号登录表单数据类型
type FormDataTypeA = Reactive<{
  account: string;
  password: string;
}>;

// 邮箱登录表单数据类型
type FormDataTypeB = Reactive<{
  email: string;
  verificationCode: string;
}>;

export type { FormDataTypeA, FormDataTypeB };
