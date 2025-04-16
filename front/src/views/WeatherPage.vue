<template>
  <div class="weather-container">
    <div class="weather-card">
      <h2 class="card-title">天气查询</h2>
      <div class="search-box">
        <el-row>
          <el-col :span="8">
            <el-select v-model="selectedProvince" placeholder="选择省份" @change="updateCities" class="input-select">
              <el-option v-for="province in provinces" :key="province" :label="province" :value="province"></el-option>
            </el-select>
          </el-col>
          <el-col :span="8">
            <el-select v-model="selectedCity" placeholder="选择城市" :disabled="!selectedProvince" class="input-select">
              <el-option v-for="city in cityList" :key="city" :label="city" :value="city"></el-option>
            </el-select>
          </el-col>
          <el-col :span="8">
            <el-button
                type="primary"
                @click="fetchWeatherData"
                :disabled="!selectedCity"
                :loading="loading"
                class="query-button"
            >
              查询
            </el-button>
          </el-col>
        </el-row>
      </div>

      <div v-if="weatherData" class="weather-info">
        <div class="current-weather">
          <h3>{{ selectedCityName }} - {{ weatherData.weather1 }}</h3>
          <div class="temp">
            <span class="temp-value">{{ weatherData.temperature }}°C</span>
            <img :src="getWeatherIcon(weatherData.weather1)" class="weather-icon" />
          </div>
          <div class="extra-info">
            <p>湿度: {{ weatherData.humidity }}%</p>
            <p>风向: {{ weatherData.windDirection }}</p>
            <p>风力: {{ weatherData.windScale }}</p>
            <p>气压: {{ weatherData.pressure }} hPa</p>
          </div>
        </div>
      </div>
      <div v-else class="no-data">
        <p>请选择城市查询天气</p>
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
import { provinces, cities } from '@/utils/chinaLocation'; // 引入省市数据

export default {
  data() {
    return {
      weatherData: null, // Stores the weather data
      selectedProvince: '', // Selected province
      selectedCity: '', // Selected city
      cityList: [], // List of cities based on selected province
      provinces: provinces, // List of provinces
      selectedCityName: '', // City name for display
      loading: false // For the loading state of the button
    };
  },
  methods: {
    // Function to get the weather icon URL based on the weather condition
    getWeatherIcon(weather) {
      const map = {
        '晴': '01d',
        '多云': '02d',
        '阴': '04d',
        '雨': '09d',
        '小雨': '09d',
        '中雨': '09d',
        '大雨': '09d',
        '雷阵雨': '11d',
        '雪': '13d',
        '小雪': '13d',
        '中雪': '13d',
        '大雪': '13d',
        '雾霾': '50d',
        '霾': '50d',
      };

      const iconCode = map[weather] || '01d';  //未匹配到天气默认图标晴天
      const iconUrl = `https://openweathermap.org/img/wn/${iconCode}@2x.png`;

      console.log('Weather icon URL:', iconUrl);
      return iconUrl;
    },


    updateCities() {
      this.selectedCity = '';
      this.cityList = this.selectedProvince ? cities[this.selectedProvince] : [];
    },


    fetchWeatherData() {
      this.loading = true;
      this.$http.get('weather/now', {
        params: {
          province: this.selectedProvince,
          city: this.selectedCity
        }
      })
          .then(response => {

            console.log('API response data:', response.data);
            if (response.data && response.data.data.code === 200) {
              console.log(234567890);

              this.weatherData = {
                weather1: response.data.data.weather1,
                temperature: response.data.data.temperature,
                humidity: response.data.data.humidity,
                windDirection: response.data.data.windDirection,
                windScale: response.data.data.windScale,
                pressure: response.data.data.pressure,
                icon: this.getWeatherIcon(response.data.weather1)
              };
              console.log('Weather data updated:', this.weatherData);  // Debug log to check weatherData
              // Set city name based on the response data
              this.selectedCityName = response.data.data.place.split(', ')[2];
            } else {
              console.error('API Error:', response.data);
              this.$message.error('天气数据加载失败');
            }
          })
          .catch(error => {
            console.error('获取天气数据失败:', error);
            this.$message.error('获取天气数据失败');
          })
          .finally(() => {
            this.loading = false; // Set loading state to false after data is fetched
          });
    }
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

.weather-info {
  margin-top: 20px;
  background: #fff;
  padding: 15px;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.current-weather {
  text-align: center;
}

.temp {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-top: 10px;
}

.temp-value {
  font-size: 40px;
  font-weight: bold;
  margin-right: 10px;
}

.weather-icon {
  width: 60px;
  height: 60px;
}

.extra-info {
  margin-top: 10px;
  font-size: 16px;
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
