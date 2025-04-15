<template>
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

              <v-textarea
                  v-model="newPlan.itinerary"
                  label="行程安排"
                  :rules="[v => !!v || '必填项']"
                  required
              ></v-textarea>

              <v-btn
                  type="submit"
                  color="primary"
                  :loading="isSubmitting"
              >保存计划</v-btn>
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
            <v-list v-else>
              <v-list-item
                  v-for="(plan, index) in travelPlans"
                  :key="index"
              >
                <v-list-item-content>
                  <v-list-item-title>{{ plan.planName }}</v-list-item-title>
                  <v-list-item-subtitle>
                    出行日期: {{ formatDate(plan.travelDate) }}
                  </v-list-item-subtitle>
                  <v-list-item-subtitle>
                    行程安排: {{ plan.itinerary }}
                  </v-list-item-subtitle>
                </v-list-item-content>
              </v-list-item>
            </v-list>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
export default {
  data() {
    return {
      newPlan: {
        planName: '',
        travelDate: null,
        itinerary: '',
        userId: null
      },
      travelPlans: [],
      dateMenu: false,
      isSubmitting: false,  // 添加提交状态
      isLoading: false     // 添加加载状态
    }
  },
  computed: {
    formattedDate() {
      return this.newPlan.travelDate ? this.formatDate(this.newPlan.travelDate) : ''
    }
  },
  created() {
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
      return new Date(date).toLocaleDateString();
    },
    fetchTravelPlans() {
      this.isLoading = true;
      this.$http.get('/travel/list', {
        params: { userId: this.newPlan.userId }
      }).then(response => {
        if (response.data && response.data.meta && response.data.meta.status === 200) {
          this.travelPlans = response.data.data || [];
        } else {
          this.handleApiError(response);
        }
      }).catch(error => {
        this.handleNetworkError(error);
      });
    },
    addTravelPlan() {
      this.isSubmitting = true;
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
    }
  }
}
</script>

