<template>
  <div class="weather-container">
    <div class="weather-card">
      <h2>天气查询</h2>
      <div class="search-box">
        <el-row>
          <el-col :span="8">
            <el-select v-model="selectedProvince" placeholder="选择省份" @change="updateCities">
              <el-option v-for="province in provinces" :key="province" :label="province" :value="province"></el-option>
            </el-select>
          </el-col>
          <el-col :span="8">
            <el-select v-model="selectedCity" placeholder="选择城市" :disabled="!selectedProvince">
              <el-option v-for="city in cityList" :key="city" :label="city" :value="city"></el-option>
            </el-select>
          </el-col>
          <el-col :span="8">
            <el-button type="primary" @click="fetchWeatherData" :disabled="!selectedCity">查询</el-button>
          </el-col>
        </el-row>
      </div>

      <div v-if="weatherData" class="weather-info">
        <div class="current-weather">
          <h3>{{ weatherData.now.text }} ({{ selectedCityName }})</h3>
          <div class="weather-details">
            <div class="temp">
              <span class="temp-value">{{ weatherData.now.temp }}°C</span>
              <img :src="getWeatherIcon(weatherData.now.icon)" class="weather-icon">
            </div>
            <div class="extra-info">
              <p>湿度: {{ weatherData.now.humidity }}%</p>
              <p>风向: {{ weatherData.now.windDir }}</p>
              <p>风力: {{ weatherData.now.windScale }}级</p>
            </div>
          </div>
        </div>

        <div class="forecast">
          <h4>未来24小时预报</h4>
          <div class="hourly-list">
            <div v-for="hour in weatherData.hourly" :key="hour.fxTime" class="hourly-item">
              <p>{{ formatHour(hour.fxTime) }}</p>
              <img :src="getWeatherIcon(hour.icon)" class="weather-icon-small">
              <p>{{ hour.temp }}°C</p>
            </div>
          </div>
        </div>
      </div>

      <div v-else class="no-data">
        <p>请选择城市查询天气</p>
      </div>
    </div>
  </div>
</template>

<script>
import { provinces, cities } from '@/utils/chinaLocation';
export default {
  data() {
    return {
      weatherData: null,
      selectedProvince: '',
      selectedCity: '',
      cityList: [],
      provinces: provinces,
      selectedCityName: '',
    };
  },
  methods: {
    updateCities() {
      this.selectedCity = '';
      this.cityList = this.selectedProvince ? cities[this.selectedProvince] : [];
    },
  }
}
</script>

<style scoped>
.weather-container {
  padding: 20px;
  display: flex;
  justify-content: center;
}

.weather-card {
  width: 80%;
  max-width: 800px;
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.search-box {
  margin: 20px 0;
}

.weather-info {
  margin-top: 20px;
}

.current-weather {
  margin-bottom: 30px;
}

.weather-details {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.temp {
  display: flex;
  align-items: center;
}

.temp-value {
  font-size: 36px;
  font-weight: bold;
  margin-right: 10px;
}

.weather-icon {
  width: 60px;
  height: 60px;
}

.extra-info {
  text-align: right;
}

.forecast {
  margin-top: 20px;
}

.hourly-list {
  display: flex;
  overflow-x: auto;
  padding: 10px 0;
}

.hourly-item {
  min-width: 80px;
  text-align: center;
  margin-right: 15px;
  padding: 10px;
  background: #f5f7fa;
  border-radius: 4px;
}

.weather-icon-small {
  width: 30px;
  height: 30px;
}

.no-data {
  text-align: center;
  padding: 40px 0;
  color: #909399;
}
</style>
