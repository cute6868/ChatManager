// 账号规则
const ACCOUNT_REGEX = /^[a-zA-Z0-9]{6,20}$/;

// 密码规则
const PASSWORD_REGEX = /^(?!(?:\d{8,32})$)[A-Za-z\d\W_~!@#$%^&*()\-_=+$${}|;:'",.<>?/\\]{8,32}$/;

// 邮箱规则
const EMAIL_REGEX = /^(?=.{1,200}$)[a-zA-Z0-9_+&*-]+(?:\.[a-zA-Z0-9_+&*-]+)*@qq\.com$/;

// 邮箱验证码规则
const EMAIL_VERIFY_CODE_REGEX = /^[0-9]{6}$/;

// 节流时间（防止按钮点击过快，执行多次逻辑）
const THROTTLE_TIME = 3 * 1000;

export { ACCOUNT_REGEX, PASSWORD_REGEX, EMAIL_REGEX, EMAIL_VERIFY_CODE_REGEX, THROTTLE_TIME };
