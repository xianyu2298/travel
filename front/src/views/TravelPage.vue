<template>
  <div id="app">
    <v-app>
      <v-container>
        <!-- 标题部分 -->
        <v-row class="justify-center mb-6">
          <v-col cols="12" sm="8" class="text-center">
            <h1>欢迎使用旅行计划应用</h1>
            <p>这是一个帮助您规划旅行，管理费用，查询天气等一站式应用</p>
          </v-col>
        </v-row>

        <!-- 卡片部分 -->
        <v-row>
          <!-- 旅行计划卡片 -->
          <v-col cols="12" sm="4" class="mb-4">
            <v-card>
              <v-card-title>旅行计划</v-card-title>
              <v-card-subtitle>创建并管理您的旅行计划，记录目的地，出行日期和行程安排。</v-card-subtitle>
              <v-card-actions>
                <v-btn color="primary" @click="viewTravelPlan">查看我的计划</v-btn>
              </v-card-actions>
            </v-card>
          </v-col>

          <!-- 天气查询卡片 -->
          <v-col cols="12" sm="4" class="mb-4">
            <v-card>
              <v-card-title>天气查询</v-card-title>
              <v-card-subtitle>查询目的地的天气情况，便于您选择合适的旅行时间。</v-card-subtitle>
              <v-card-actions>
                <v-btn color="primary" @click="checkWeather">查看天气</v-btn>
              </v-card-actions>
            </v-card>
          </v-col>

          <v-col cols="12" sm="4" class="mb-4">
            <v-card>
              <v-card-title>经费记录</v-card-title>
              <v-card-subtitle>记录您旅游时产生的衣食住行费用。</v-card-subtitle>
              <v-card-actions>
                <v-btn color="primary" @click="checkMoney">查看记录</v-btn>
              </v-card-actions>
            </v-card>
          </v-col>
        </v-row>
        <!-- 圆形退出按钮 -->
        <v-btn
            class="logout-btn"
            @click="logout"
            color="error"
            fab
            absolute
            top
            right
        >
          退出
        </v-btn>
      </v-container>
    </v-app>
  </div>
</template>

<script>
export default {
  name: 'App',
  methods: {
    viewTravelPlan() {
      this.$router.push('/travel-plan');
    },
    checkWeather() {
      this.$router.push('/weather');
    },
    checkMoney() {
      this.$router.push({ name: 'MoneyPage' });
    },
    logout() {
      // 清除本地存储的token和用户信息
      window.sessionStorage.removeItem('token')
      window.sessionStorage.removeItem('userInfo')

      // 调用后端退出接口
      this.$http.post('/user/logout')
          .then(() => {
            this.$router.push('/login')
            this.$message.success('退出登录成功')
          })
          .catch(error => {
            console.error('退出失败:', error)
            this.$message.error('退出登录失败')
          })
    }
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  text-align: center;
  color: #2c3e50;
}

.logout-btn {
  border-radius: 50%;
  width: 56px;
  height: 56px;
  font-size: 14px;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 0;
}

.logout-btn .v-btn__content {
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 14px;
}
</style>
