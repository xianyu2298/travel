<template>
  <div class="background">
  <v-container>
    <v-row>
      <v-col cols="12" md="6">
        <v-card>
          <v-card-title>添加旅行计划</v-card-title>
          <v-card-text>
            <v-form @submit.prevent="addTravelPlan">
              <v-text-field
                  v-model="newPlan.planName"
                  label="旅行计划名称"
                  :rules="[v => !!v || '必填项']"
                  required
              ></v-text-field>

              <v-menu
                  v-model="dateMenu"
                  :close-on-content-click="false"
                  transition="scale-transition"
                  offset-y
                  min-width="auto"
              >
                <template v-slot:activator="{ on, attrs }">
                  <v-text-field
                      v-model="formattedDate"
                      label="出行日期"
                      prepend-icon="mdi-calendar"
                      readonly
                      v-bind="attrs"
                      v-on="on"
                  ></v-text-field>
                </template>
                <v-date-picker
                    v-model="newPlan.travelDate"
                    @input="dateMenu = false"
                ></v-date-picker>
              </v-menu>

              <v-menu
                  v-model="dateMenu"
                  :close-on-content-click="false"
                  transition="scale-transition"
                  offset-y
                  min-width="auto"
              ></v-menu>

              <!-- 添加起始省份和城市选择 -->
              <v-row>
                <v-col cols="12" md="6">
                  <v-select
                      v-model="newPlan.startProvince"
                      :items="provinces"
                      label="起始省份"
                      required
                      @change="updateCities('start')"
                  ></v-select>
                </v-col>
                <v-col cols="12" md="6">
                  <v-select
                      v-model="newPlan.startCity"
                      :items="startCities"
                      label="起始城市"
                      required
                      :disabled="!newPlan.startProvince"
                  ></v-select>
                </v-col>
              </v-row>

              <!-- 添加目的省份和城市选择 -->
              <v-row>
                <v-col cols="12" md="6">
                  <v-select
                      v-model="newPlan.endProvince"
                      :items="provinces"
                      label="目的省份"
                      required
                      @change="updateCities('end')"
                  ></v-select>
                </v-col>
                <v-col cols="12" md="6">
                  <v-select
                      v-model="newPlan.endCity"
                      :items="endCities"
                      label="目的城市"
                      required
                      :disabled="!newPlan.endProvince"
                  ></v-select>
                </v-col>
              </v-row>

              <v-textarea
                  v-model="newPlan.itinerary"
                  label="行程安排"
                  :rules="[v => !!v || '必填项']"
                  required
              ></v-textarea>

              <v-btn
                  type="submit"

                  class="blue--text"
                  style="background-color: #ebeef1 !important"
                  :loading="isSubmitting"
              >保存计划</v-btn>

              <v-btn

                  style="background-color: #ebeef1 !important"
                  @click="$router.push('/travel')"
                  class="ml-2"
              >返回</v-btn>
            </v-form>
          </v-card-text>
        </v-card>
      </v-col>

      <v-col cols="12" md="6">
        <v-card>
          <v-card-title>我的旅行计划</v-card-title>
          <v-card-text>
            <v-progress-linear
                v-if="isLoading"
                indeterminate
            ></v-progress-linear>
            <v-list v-else class="expense-list">
              <v-list-item
                  v-for="(plan, index) in travelPlans"
                  :key="index"
              >
                <v-list-item-content>
                  <v-list-item-title class="font-weight-bold">{{ plan.planName || '未命名计划' }}</v-list-item-title>
                  <v-list-item-subtitle>
                    出行日期: {{ plan.travelDate ? formatDate(plan.travelDate) : '未设置日期' }}
                  </v-list-item-subtitle>
                  <v-list-item-subtitle>
                    行程安排: {{ plan.itinerary || '无安排' }}
                  </v-list-item-subtitle>
                  <v-list-item-subtitle>
                    行程路线: {{ plan.startProvince }}{{ plan.startCity }} → {{ plan.endProvince }}{{ plan.endCity }}
                  </v-list-item-subtitle>

                </v-list-item-content>
              </v-list-item>
            </v-list>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
  </div>
</template>

<script>
import { provinces, cities } from '@/utils/chinaLocation';
export default {
  data() {
    return {
      newPlan: {
        planName: '',
        travelDate: null,
        itinerary: '',
        userId: null,
        startProvince: '',  // 新增：起始省份
        startCity: '',      // 新增：起始城市
        endProvince: '',    // 新增：目的省份
        endCity: ''         // 新增：目的城市

      },
      travelPlans: [],
      dateMenu: false,
      isSubmitting: false,  // 添加提交状态
      isLoading: false,
      provinces: [],       // 所有省份
      startCities: [],     // 起始城市列表
      endCities: []        // 目的城市列表// 添加加载状态
    }
  },
  computed: {
    formattedDate() {
      return this.newPlan.travelDate ? this.formatDate(this.newPlan.travelDate) : ''
    }
  },
  created() {
    this.loadLocationData();
    const userId = window.sessionStorage.getItem('userId');
    if (userId) {
      this.newPlan.userId = parseInt(userId);
      this.fetchTravelPlans();
    } else {
      this.$message.error('用户未登录');
      this.$router.push('/login');
    }
  },
  methods: {
    formatDate(date) {
      if (!date) return '';
      const options = { year: 'numeric', month: '2-digit', day: '2-digit' };
      return new Date(date).toLocaleDateString('zh-CN', options);
    },
    fetchTravelPlans() {
      this.isLoading = true;
      this.$http.get('/travel/list', {
        params: { userId: this.newPlan.userId }
      }).then(response => {
        console.log('响应数据:', response.data); // 添加日志
        if (response.data && response.data.meta && response.data.meta.status === 200) {
          this.travelPlans = response.data.data || [];
          console.log('设置的计划数据:', this.travelPlans); // 添加日志
        } else {
          this.handleApiError(response);
        }
      }).catch(error => {
        console.error('获取旅行计划错误:', error);
        this.handleNetworkError(error);
      }).finally(() => {
        this.isLoading = false;
      });
    },
    addTravelPlan() {
      this.isSubmitting = true;
      console.log('提交的用户ID:', this.newPlan.userId); // 添加日志
      if (!this.newPlan.planName || !this.newPlan.travelDate || !this.newPlan.itinerary) {
        return this.$message.error('请填写完整信息');
      }

      const submitData = {
        ...this.newPlan
      };

      this.$http.post('/travel/add', submitData)
        .then(response => {
          if (response.data && response.data.meta && response.data.meta.status === 200) {
            this.$message.success('添加成功');
            this.fetchTravelPlans();
            this.newPlan.planName = '';
            this.newPlan.travelDate = null;
            this.newPlan.itinerary = '';
          } else {
            this.handleApiError(response);
          }
        })
        .catch(error => {
          this.handleNetworkError(error);
        })
          .finally(() => {
            this.isSubmitting = false;
          });
    },
    handleApiError(response) {
      const status = response.data.meta.status;
      const msg = response.data.meta.msg || '请求失败';

      if (status === 401) {
        this.$message.error('登录已过期，请重新登录');
        this.$router.push('/login');
      } else {
        this.$message.error(msg);
      }
    },
    handleNetworkError(error) {
      if (error.response) {
        const status = error.response.status;
        if (status === 401) {
          this.$message.error('登录已过期，请重新登录');
          this.$router.push('/login');
        } else if (status === 404) {
          this.$message.error('接口不存在');
        } else {
          this.$message.error('服务器错误: ' + status);
        }
      } else if (error.request) {
        this.$message.error('网络连接异常，请检查网络');
      } else {
        this.$message.error('请求配置错误: ' + error.message);
      }
      console.error('请求错误详情:', error);
    },
    // 加载地理位置数据
    loadLocationData() {
      this.provinces = provinces; // 使用本地数据
    },
    updateCities(type) {
      const province = type === 'start' ? this.newPlan.startProvince : this.newPlan.endProvince;
      if (province) {
        const cityList = cities[province] || []; // 使用本地数据
        if (type === 'start') {
          this.startCities = cityList;
          this.newPlan.startCity = '';
        } else {
          this.endCities = cityList;
          this.newPlan.endCity = '';
        }
      }
    },
  }
}
</script>

<style scoped>

.background{
  background-image: url('../assets/img/background.jpg');
  background-size: cover;      /* 背景图像填充容器 */
  background-position: center; /* 确保背景图居中 */
  background-repeat: no-repeat;
  position: fixed;  /* 改为 fixed 确保背景固定不动 */
  width: 100%;       /* 宽度填充整个视口 */
  height: 100vh;     /* 使用视口高度单位，确保背景高度填满屏幕 */
  top: 0;
  left: 0;
  z-index: -1;       /* 确保背景图位于最底层 */
  padding: 0;        /* 去除任何内边距 */
  display: flex;
  justify-content: center;  /* 横向居中 */
  align-items: center;      /* 纵向居中 */
}

.expense-list .v-list-item {
  border: 1px solid #eee;
  border-radius: 4px;
  margin-bottom: 8px;
  padding: 12px;
}

.expense-list .v-list-item:last-child {
  margin-bottom: 0;
}

</style>
