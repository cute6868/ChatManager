<template>
  <div class="forget">
    <div class="white-panel">
      <!-- 重置密码面板 -->
      <div class="reset-password-panel">
        <h3 class="title">重置密码</h3>
        <el-form
          ref="formRef"
          :model="formData"
          :rules="formRules"
          style="width: 260px"
          label-width="auto"
        >
          <el-form-item label="新密码" prop="newPassword">
            <el-input v-model="formData.newPassword" type="password" show-password />
          </el-form-item>

          <el-form-item label="确认密码" prop="confirmPassword">
            <el-input v-model="formData.confirmPassword" type="password" show-password />
          </el-form-item>

          <el-form-item prop="email">
            <template #label>
              <div class="email-title">QQ邮箱</div>
            </template>
            <el-input v-model="formData.email" />
          </el-form-item>

          <el-form-item label="验证码" prop="verifyCode">
            <el-input v-model="formData.verifyCode">
              <template #suffix>
                <div class="get-code" @click="wrapGetCode">
                  <span v-show="!flag">获取验证码</span>
                  <span v-show="flag">重新获取({{ second }}秒)</span>
                </div>
              </template>
            </el-input>
          </el-form-item>
        </el-form>

        <!-- 确认按钮 -->
        <el-button class="confirm-btn" type="primary" @click="wrapReset">确认</el-button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
// 表单相关的数据和方法
import useForm from '@/hooks/forget/useForm';
const { formData, formRules } = useForm();

// 验证码相关的数据和方法
import useVerifyCode from '@/hooks/forget/useVerifyCode';
const { second, flag, wrapGetCode } = useVerifyCode(formData);

// 按钮相关的数据和方法
import useButton from '@/hooks/forget/useButton';
const { formRef, wrapReset } = useButton(formData);
</script>

<style scoped lang="scss">
.forget {
  width: 100vw;
  height: 100vh;

  display: flex;
  justify-content: center;
  align-items: center;

  background-image: url('@/assets/img/bg.png');
  background-repeat: no-repeat;
  background-position: center;
  background-size: cover;

  .white-panel {
    width: max(24%, 320px);
    background-color: #fff;

    display: flex;
    justify-content: center;
    align-items: center;

    margin-bottom: 6%;
    border-radius: 2%;

    padding: max(3%, 22px) 0;
  }

  // 重置密码面板
  .reset-password-panel {
    width: max(78%, 250px);
    border: 2px solid #e6e6e6;
    border-radius: 1%;

    // 让面板容器里面元素居中
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;

    // 设置面板容器与里面元素之间内部间距
    padding-top: max(2%, 20px);
    padding-right: max(2%, 14px);
    padding-bottom: max(1.5%, 16px);
    padding-left: max(2%, 12px);

    // 标题样式
    .title {
      font-size: 22px;
      margin-bottom: 26px;
    }

    // 确认按钮样式
    .confirm-btn {
      // 登录按钮
      width: 100%;
      margin-top: 2%;
    }

    // 每个输入框之间的距离
    .el-form-item {
      margin-bottom: 30px;
    }

    // 验证码样式
    .get-code {
      width: 62px;
      height: 14px;
      letter-spacing: 1px;
      font-size: 10px;
      display: flex;
      justify-content: center;
      align-items: center;
      cursor: pointer;
      &:hover {
        color: rgb(64, 158, 255);
      }
      &:active {
        color: rgb(181, 213, 246);
      }
    }

    // 验证码样式
    // 禁用元素
    .disabled-element {
      pointer-events: none;
    }
  }
}
</style>
