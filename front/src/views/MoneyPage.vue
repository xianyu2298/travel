<template>
  <div class="login_container">
    <div class="">
  <v-container>
    <v-row>
      <!-- 左侧：旅行计划列表 -->
      <v-col cols="12" md="3">
        <v-card>
          <v-card-title>我的旅行计划</v-card-title>
          <v-card-text>
            <v-list class="fenge">
              <v-list-item
                  v-for="plan in travelPlans"
                  :key="plan.id"
                  @click="selectPlan(plan)"
                  :class="{ 'selected-plan': selectedPlan?.id === plan.id }"
              >
                <v-list-item-content>
                  <v-list-item-title>{{ plan.planName }}</v-list-item-title>
                  <v-list-item-subtitle>
                    {{ formatDate(plan.travelDate) }}
                  </v-list-item-subtitle>
                </v-list-item-content>
              </v-list-item>
            </v-list>
          </v-card-text>
        </v-card>
      </v-col>

      <!-- 中间：开支记录表单 -->
      <v-col cols="12" md="6">
        <v-card>
          <v-card-title>记录开支</v-card-title>
          <v-card-text>
            <v-form @submit.prevent="saveExpense">
              <v-text-field
                  v-model="expense.transportation"
                  label="交通费用"
                  type="number"
                  min="0"
                  prefix="¥"
              ></v-text-field>

              <v-text-field
                  v-model="expense.accommodation"
                  label="住宿费用"
                  type="number"
                  min="0"
                  prefix="¥"
              ></v-text-field>

              <v-text-field
                  v-model="expense.dining"
                  label="餐饮费用"
                  type="number"
                  min="0"
                  prefix="¥"
              ></v-text-field>

              <v-text-field
                  v-model="expense.tickets"
                  label="门票费用"
                  type="number"
                  min="0"
                  prefix="¥"
              ></v-text-field>

              <v-text-field
                  v-model="expense.shopping"
                  label="购物费用"
                  type="number"
                  min="0"
                  prefix="¥"
              ></v-text-field>

              <v-text-field
                  v-model="expense.others"
                  label="其他费用"
                  type="number"
                  min="0"
                  prefix="¥"
              ></v-text-field>

              <v-card class="mt-4">
                <v-card-text>
                  总费用：¥{{ totalExpense }}
                </v-card-text>
              </v-card>

              <div class="d-flex justify-space-between mt-6">
                <v-btn
                    color="blue"
                    @click="$router.push('/travel')"
                >
                  返回
                </v-btn>
                <v-btn
                    type="submit"
                    color="blue"
                    :disabled="!selectedPlan"
                >
                  保存记录
                </v-btn>
              </div>
            </v-form>
          </v-card-text>
        </v-card>
      </v-col>

      <!-- 右侧：开支记录列表 -->
      <v-col cols="12" md="3">
        <v-card>
          <v-card-title>我的开支记录</v-card-title>
          <v-card-text>
            <v-list class="fenge">
              <v-list-item
                  v-for="record in expenseRecords"
                  :key="record.id"
                  class="mb-2"
              >
                <v-list-item-content>
                  <v-list-item-title>
                    {{ record.planName || '未命名计划' }}
                  </v-list-item-title>
                  <v-list-item-subtitle>
                    总费用：¥{{ record.total }}
                  </v-list-item-subtitle>
                  <v-list-item-subtitle>
                    记录时间：{{ formatDateTime(record.createdAt) }}
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
  </div>
</template>

<script>
export default {
  data() {
    return {
      travelPlans: [],
      selectedPlan: null,
      expense: {
        transportation: 0,
        accommodation: 0,
        dining: 0,
        tickets: 0,
        shopping: 0,
        others: 0
      },
      expenseRecords: []
    };
  },
  computed: {
    totalExpense() {
      return Object.values(this.expense).reduce((sum, val) => sum + Number(val), 0);
    }
  },
  methods: {
    // 获取旅行计划
    async fetchTravelPlans() {
      const userId = window.sessionStorage.getItem('userId');
      if (userId) {
        const response = await this.$http.get('/travel/list', {
          params: { userId }
        });
        this.travelPlans = response.data.data || [];
      }
    },

    // 获取开支记录
    async fetchExpenseRecords() {
      const userId = window.sessionStorage.getItem('userId');
      if (userId) {
        const response = await this.$http.get('/expense/list', {
          params: { userId }
        });
        this.expenseRecords = response.data.data || [];
      }
    },

    // 选择旅行计划
    selectPlan(plan) {
      this.selectedPlan = plan;
    },

    // 保存开支记录
    async saveExpense() {
      if (!this.selectedPlan) {
        this.$message.error('请先选择一个旅行计划');
        return;
      }

      if (!this.selectedPlan.id) {
        this.$message.error('旅行计划ID无效');
        return;
      }

      const userId = window.sessionStorage.getItem('userId');
      if (!userId) {
        this.$message.error('用户未登录');
        return;
      }

      const payload = {
        ...this.expense,
        travelPlanId: this.selectedPlan.id, // 确保传递了travel_plan_id
        userId: parseInt(userId),
        total: this.totalExpense
      };

      try {
        await this.$http.post('/expense/add', payload);
        this.$message.success('保存成功');
        this.resetForm();
        this.fetchExpenseRecords();
      } catch (error) {
        this.$message.error('保存失败');
      }
    },

    // 重置表单
    resetForm() {
      this.expense = {
        transportation: 0,
        accommodation: 0,
        dining: 0,
        tickets: 0,
        shopping: 0,
        others: 0
      };
    },

    // 格式化日期时间
    formatDateTime(date) {
      if (!date) return '';
      const options = {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit'
      };
      return new Date(date).toLocaleString('zh-CN', options);
    },
    // 格式化日期
    formatDate(date) {
      return new Date(date).toLocaleDateString('zh-CN');
    }
  },
  created() {
    this.fetchTravelPlans();
    this.fetchExpenseRecords();
  }
};
</script>

<style scoped>
.selected-plan {
  background-color: #f5f5f5;
}

.fenge {
  border: 1px solid #eee;
  border-radius: 4px;
  margin-bottom: 8px;
  padding: 12px;
}

.fenge .v-list-item:last-child {
  margin-bottom: 0;
}


.d-flex {
  display: flex;
  justify-content: space-between;
  margin-top: 16px;
}

.login_container {
  background-image: url('../assets/img/background.jpg');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  position: fixed;  /* 改为fixed确保覆盖整个视口 */
  width: 100%;
  height: 100vh;   /* 使用视口高度单位 */
  top: 0;
  left: 0;
  z-index: -1;     /* 确保背景在底层 */
}
</style>