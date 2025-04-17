<template>
  <div class="login_container">
    <div class="login_box">
      <!-- 头像区 -->
      <div class="avatar_box">
        <i class="el-icon-user-solid" style="font-size: 100px; color: #409EFF"></i>
      </div>
      <!-- 表单区 -->
      <el-form ref="registerFormRef" :model="registerForm" :rules="registerFormRules" label-width="0px" class="login_form">
        <el-form-item prop="username">
          <el-input v-model="registerForm.username" prefix-icon="el-icon-user" placeholder="用户名"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="registerForm.password" prefix-icon="el-icon-lock" placeholder="密码" type="password"></el-input>
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <el-input v-model="registerForm.confirmPassword" prefix-icon="el-icon-lock" placeholder="确认密码" type="password"></el-input>
        </el-form-item>
        <el-row>
          <el-form-item class="btns">
            <el-button type="info" @click="goToLogin">返回登录</el-button>
            <el-button type="primary" @click="register">注册</el-button>
          </el-form-item>
        </el-row>
      </el-form>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    const validatePassword = (rule, value, callback) => {
      if (value !== this.registerForm.password) {
        callback(new Error('两次输入密码不一致!'));
      } else {
        callback();
      }
    };

    return {
      registerForm: {
        username: '',
        password: '',
        confirmPassword: ''
      },
      registerFormRules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 3, max: 10, message: '长度在 3 到 10 个字符', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, message: '请再次输入密码', trigger: 'blur' },
          { validator: validatePassword, trigger: 'blur' }
        ]
      }
    };
  },
  methods: {
    goToLogin() {
      this.$router.push('/login');
    },
    async register() {
      this.$refs.registerFormRef.validate(async valid => {
        if (!valid) return;

        try {
          const { data: res } = await this.$http.post('user/register', {
            username: this.registerForm.username,
            password: this.registerForm.password
          });

          if (res.meta.status !== 200) {
            return this.$message.error(res.meta.msg || '注册失败');
          }

          this.$message.success('注册成功');
          this.$router.push('/login');
        } catch (error) {
          console.error('注册失败:', error);
          this.$message.error('注册失败，请重试');
        }
      });
    }
  }
};
</script>

<style lang="less" scoped>
@import '../assets/css/login.less';
</style>