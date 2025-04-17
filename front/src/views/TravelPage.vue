<template>
  <div class="train-container">
    <div class="train-card">
      <!-- 标题部分 -->
      <h2 class="card-title">欢迎使用旅行计划应用</h2>
      <p class="card-subtitle">这是一个帮助您规划旅行，管理费用，查询天气等一站式应用</p>

      <!-- 卡片部分 -->
      <div class="search-box">
        <el-row>
          <!-- 旅行计划卡片 -->
          <el-col :span="8">
            <v-card>
              <v-card-title>旅行计划</v-card-title>
              <v-card-subtitle>创建并管理您的旅行计划，记录目的地，出行日期和行程安排。</v-card-subtitle>
              <v-card-actions>
                <el-button class="query-button" @click="viewTravelPlan">查看我的计划</el-button>
              </v-card-actions>
            </v-card>
          </el-col>

          <!-- 天气查询卡片 -->
          <el-col :span="8">
            <v-card>
              <v-card-title>天气查询</v-card-title>
              <v-card-subtitle>查询目的地的天气情况，便于您选择合适的旅行时间。</v-card-subtitle>
              <v-card-actions>
                <el-button class="query-button" @click="checkWeather">查看天气</el-button>
              </v-card-actions>
            </v-card>
          </el-col>

          <el-col :span="8">
            <v-card>
              <v-card-title>经费记录</v-card-title>
              <v-card-subtitle>记录您旅游时产生的衣食住行费用。</v-card-subtitle>
              <v-card-actions>
                <el-button class="query-button" @click="checkMoney">查看记录</el-button>
              </v-card-actions>
            </v-card>
          </el-col>

          <el-col :span="8">
            <v-card>
              <v-card-title>邮政编码查询</v-card-title>
              <v-card-subtitle>查询全国各地的邮政编码。</v-card-subtitle>
              <v-card-actions>
                <el-button class="query-button" @click="postCode">查询邮政编码</el-button>
              </v-card-actions>
            </v-card>
          </el-col>

          <el-col :span="8">
            <v-card>
              <v-card-title>手机号归属地查询</v-card-title>
              <v-card-subtitle>查询中国大陆手机号的归属地。</v-card-subtitle>
              <v-card-actions>
                <el-button class="query-button" @click="viewPhone">查看归属地</el-button>
              </v-card-actions>
            </v-card>
          </el-col>

          <el-col :span="8">
            <v-card>
              <v-card-title>火车票查询</v-card-title>
              <v-card-subtitle>查询指定日期火车票。</v-card-subtitle>
              <v-card-actions>
                <el-button class="query-button" @click="viewTrain">查看火车票</el-button>
              </v-card-actions>
            </v-card>
          </el-col>

        </el-row>
        <!-- 圆形退出按钮 -->
        <v-btn
            class="logout-btn"
            @click="logout"
            color="red"
            fab
            absolute
            :style="{ top: '85%', right: '5%' }" >
        退出
        </v-btn>
      </div>
    </div>
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
    postCode() {
      this.$router.push('/postcode');
    },
    viewPhone() {
      this.$router.push('/phone');
    },
    viewTrain() {
      this.$router.push('/train');
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

<style scoped>

html, body {
  margin: 0;
  padding: 0;
  height: 100%;
}


.train-container {
  background-image: url('../assets/img/background.jpg');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  position: fixed;
  width: 100%;
  height: 100vh;
  top: 0;
  left: 0;
  z-index: -1;
  padding: 0;
  display: flex;
  justify-content: center;
  align-items: center;
}


.train-card {
  width: 100%;
  max-width: 800px;
  background: rgba(255, 255, 255, 0.8);
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  margin: 20px;
}

.card-title {
  text-align: center;
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 20px;
}

.card-subtitle {
  text-align: center;
  font-size: 16px;
  color: #666;
  margin-bottom: 30px;
}

/* 按钮样式 */
.query-button {
  width: 100%;
  font-size: 16px;
  padding: 10px;
  margin: 10px 0;
}

/* 退出按钮 */
.logout-btn {
  position: absolute;
  bottom: 10%;
  right: 5%;
  border-radius: 50%;
  width: 56px;
  height: 56px;
  font-size: 14px;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 0;
}
</style>
