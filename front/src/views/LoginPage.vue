<template>
  <div class="login_container">
    <div class="login_box">
      <!-- 头像区 -->
      <div class="avatar_box">
        <i class="el-icon-user-solid" style="font-size: 100px; color: #409EFF"></i>
      </div>
      <!-- 表单区 -->
      <!-- prop代表需要验证 -->
      <el-form ref="loginFormRef" :model="loginForm" :rules="loginFormRules" label-width="0px" class="login_form">
        <el-form-item  prop="username">
          <el-input v-model="loginForm.username" prefix-icon="el-icon-user" placeholder="用户名"></el-input>
        </el-form-item>
        <el-form-item prop="password" >
          <el-input v-model="loginForm.password" prefix-icon="el-icon-lock" placeholder="密码" type="password" ></el-input>
        </el-form-item>
        <el-row>
          <el-col :span="16">
            <el-form-item prop="validcode" class="login_validcode">
              <el-input v-model="loginForm.validcode" prefix-icon="el-icon-edit-outline" placeholder="验证码"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <ValidCode :value.sync="trueValidCode" style="margin: 1px 0px 0px 20px"></ValidCode>
          </el-col>
        </el-row>
        <el-row>
          <el-form-item class="btns" >
            <!-- <span style="margin: 0 10px"></span> -->
            <el-button type="info" @click="$router.push('/register')">注册</el-button>
            <el-button type="primary" @click="login">登录</el-button>
          </el-form-item>
        </el-row>
      </el-form>
    </div>
  </div>
</template>

<script>
import ValidCode from "@/views/ValidCode.vue";

export default {
  components: {ValidCode},
  data () {
    return {
      loginForm: {
        username: '',
        password: '',
        valicode: ''
      },
      trueValidCode: '',
      // 验证规则
      loginFormRules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 3, max: 10, message: '长度在 3 到 10 个字符', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
        ],
        validcode: [
          { required: true, message: '请输入验证码', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    // 点击重置
    resetLoginForm () {
      this.$refs.loginFormRef.resetFields()
    },
    login () {
      this.$refs.loginFormRef.validate(async valid => {
        if (!valid) return
        try {
          const { data: res } = await this.$http.post('user/login', this.loginForm)

          if (this.loginForm.validcode !== this.trueValidCode) {
            return this.$message.error('验证码错误')
          }

          if (res.meta.status !== 200) {
            return this.$message.error(res.meta.msg || '登录失败')
          }

          // 确保存储token和用户ID
          window.sessionStorage.setItem('token', res.data.token)
          window.sessionStorage.setItem('userId', res.data.userid) // 统一使用userId
          window.sessionStorage.setItem('username', res.data.username || '')

          this.$message.success('登录成功')
          this.$router.push('/travel')
        } catch (error) {
          console.error('登录失败:', error)
          this.$message.error('登录失败，请重试')
        }
      })
    }
  }
}
</script>

<style lang="less" scoped>
.login_container {
  background-image: url('../assets/img/background.jpg');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;

  width: 100%;
  height: 100vh;   /* 使用视口高度单位 */
  top: 0;
  left: 0;
  z-index: -1;     /* 确保背景在底层 */
}
.login_box{
  width: 500px;
  height: 360px;
  background-color: rgba(255, 255, 255, 0.8);
  border-radius: 3px;
  -webkit-box-shadow:0px 3px 3px #ada4a4 ;
  -moz-box-shadow:0px 3px 3px #ada4a4 ;
  box-shadow:0px 3px 3px #ada4a4 ;
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%,-50%);
  .avatar_box{
    height: 130px;
    width: 130px;
    border: 1px solid #eee;
    border-radius: 50%;
    padding: 10px;
    box-shadow: 0 0 10px #ddd;
    position: absolute;
    left: 50%;
    transform: translate(-50%,-50%);
    background-color: #fff;
    img{
      width: 100%;
      height: 100%;
      border-radius: 50%;
      background-color: #eee;
    }
  }
}
.login_form{
  position: absolute;
  bottom: 0;
  width: 100%;
  padding: 10px 20px;
  box-sizing: border-box;
}
.btns{
  display: flex;
  justify-content: center;
  .el-button{
    margin: 0px 20px;
    width: 100px;
  }
}
</style>
