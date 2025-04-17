<template>
  <div class="train-container">
    <div class="train-card">
      <h2 class="card-title">火车查询</h2>

      <div class="search-box">
        <el-row>
          <el-col :span="6">
            <el-select v-model="departureProvince" placeholder="出发省份" @change="updateCities('departure')" class="input-select">
              <el-option v-for="province in provinces" :key="province" :label="province" :value="province"></el-option>
            </el-select>
          </el-col>
          <el-col :span="6">
            <el-select v-model="departureCity" placeholder="出发城市" class="input-select">
              <el-option v-for="city in cities[departureProvince]" :key="city" :label="city" :value="city"></el-option>
            </el-select>
          </el-col>
          <el-col :span="6">
            <el-select v-model="arrivalProvince" placeholder="目的省份" @change="updateCities('arrival')" class="input-select">
              <el-option v-for="province in provinces" :key="province" :label="province" :value="province"></el-option>
            </el-select>
          </el-col>
          <el-col :span="6">
            <el-select v-model="arrivalCity" placeholder="目的城市" class="input-select">
              <el-option v-for="city in cities[arrivalProvince]" :key="city" :label="city" :value="city"></el-option>
            </el-select>
          </el-col>
        </el-row>

        <el-row class="mt-20">
          <el-col :span="12">
            <el-date-picker
                v-model="selectedDate"
                type="date"
                placeholder="选择日期"
                class="input-select"
            ></el-date-picker>
          </el-col>
          <el-col :span="12">
            <el-input
                v-model="validCodeInput"
                placeholder="请输入验证码"
                class="input-select"
            ></el-input>
            <valid-code :value.sync="trueValidCode"></valid-code>
          </el-col>
          <el-col :span="12">
            <el-button
                type="primary"
                @click="fetchTrainData"
                class="query-button"
            >
              查询
            </el-button>
          </el-col>
        </el-row>
      </div>

      <div v-if="rawResponse" class="train-info">
        <h3>查询结果</h3>
        <pre class="response">{{ rawResponse }}</pre>
      </div>

      <div v-else class="no-data">
        <p>请选择出发地和目的地查询火车信息</p>
      </div>

      <div class="return-button">
        <v-btn
            type="primary"
            @click="$router.push('/travel')"
            class="ml-2"
        >返回</v-btn>
      </div>
    </div>
  </div>
</template>

<script>
import { provinces, cities } from "@/utils/chinaLocation";  // 引入 provinces 和 cities
import axios from 'axios';
import ValidCode from "@/views/ValidCode.vue";

export default {
  components: {ValidCode},
  data() {
    return {
      provinces: provinces,  // 省份数据
      cities: cities,        // 城市数据
      departureProvince: "",  // 默认出发地省份
      departureCity: "",     // 默认出发城市
      arrivalProvince: "",   // 默认目的地省份
      arrivalCity: "",      // 默认目的城市
      selectedDate: "",         // 用户选择的日期
      rawResponse: "",
      validCodeInput: "",
      trueValidCode: ""
      // 用来存储原始API响应
    };
  },
  methods: {
    // 更新城市选择框
    updateCities(type) {
      if (type === "departure") {
        this.departureCity = this.cities[this.departureProvince][0];  // 选择省份后默认选择第一个城市
      } else if (type === "arrival") {
        this.arrivalCity = this.cities[this.arrivalProvince][0];  // 选择省份后默认选择第一个城市
      }
    },

    // 请求火车信息
    async fetchTrainData() {
      if (this.validCodeInput !== this.trueValidCode) {
        alert('验证码错误');
        return;
      }
      try {
        const response = await axios.get("/train/query", {
          params: {
            departure: this.departureCity,
            arrival: this.arrivalCity,
            date: this.selectedDate  // 传递选择的日期
          }
        });
        this.rawResponse = response.data.data.response;  // 直接拿到原始响应
      } catch (error) {
        console.error("获取火车信息失败:", error);
      }
    }
  },
};
</script>

<style scoped>

.train-container {
  padding: 20px;
  display: flex;
  justify-content: center;
  background-image: url('../assets/img/background.jpg');
  background-size: cover;
  background-position: center;
  margin: 0;
}

.train-card {
  width: 100%;
  max-width: 800px;
  background: #f7f7f7;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.card-title {
  text-align: center;
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 20px;
}

.search-box {
  margin: 20px 0;
}

.input-select {
  width: 100%;
}

.query-button {
  width: 100%;
  font-size: 16px;
  padding: 10px;
}

.train-info {
  margin-top: 20px;
  background: #fff;
  padding: 15px;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.response {
  white-space: pre-wrap;
  word-wrap: break-word;
  font-family: 'Courier New', monospace;
  background-color: #f4f4f4;
  padding: 20px;
  border-radius: 5px;
}

.no-data {
  text-align: center;
  padding: 20px;
  color: #909399;
}

.return-button {
  text-align: center;
  margin-top: 20px;
}
</style>
