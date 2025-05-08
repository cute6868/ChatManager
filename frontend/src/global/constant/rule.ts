// 账号规则
const ACCOUNT_REGEX = /^[a-zA-Z0-9]+$/;

// 密码规则
const PASSWORD_REGEX = /^(?!(?:\d{8,32})$)[A-Za-z\d\W_~!@#$%^&*()\-_=+$${}|;:'",.<>?/\\]{8,32}$/;

// 邮箱验证码规则
const EMAIL_VERIFY_CODE_REGEX = /^[0-9]+$/;

export { ACCOUNT_REGEX, PASSWORD_REGEX, EMAIL_VERIFY_CODE_REGEX };
