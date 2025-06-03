import type { Reactive } from 'vue';

// 账号登录表单数据类型
type FormDataTypeA = Reactive<{
  account: string;
  password: string;
}>;

// 邮箱登录表单数据类型
type FormDataTypeB = Reactive<{
  email: string;
  verifyCode: string;
}>;

// 重置密码表单数据类型
type FormDataTypeC = Reactive<{
  newPassword: string;
  confirmPassword: string;
  email: string;
  verifyCode: string;
}>;

// 注册账号表单数据类型
type FormDataTypeD = Reactive<{
  account: string;
  password: string;
  confirm: string;
  email: string;
  verifyCode: string;
}>;

// 模型响应的数据类型
interface ModelResponseData {
  code: number;
  model: string;
  response:
    | string
    | {
        reasoning: string;
        answer: string;
      };
}

export type { FormDataTypeA, FormDataTypeB, FormDataTypeC, FormDataTypeD, ModelResponseData };
