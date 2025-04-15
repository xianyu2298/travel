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
                    v-model="newPlan.travelDate"
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
                required
              ></v-textarea>

              <v-btn type="submit" color="primary">保存计划</v-btn>
            </v-form>
          </v-card-text>
        </v-card>
      </v-col>

      <v-col cols="12" md="6">
        <v-card>
          <v-card-title>我的旅行计划</v-card-title>
          <v-card-text>
            <v-list>
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
      dateMenu: false
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
      this.$http.get('/travel/list', {
        params: { userId: this.newPlan.userId }
      }).then(response => {
        this.travelPlans = response.data.data || [];
      });
    },
    addTravelPlan() {
      const submitData = {
        ...this.newPlan
      };

      this.$http.post('/travel/add', submitData)
        .then(() => {
          this.$message.success('添加成功');
          this.fetchTravelPlans();
          this.newPlan.planName = '';
          this.newPlan.travelDate = null;
          this.newPlan.itinerary = '';
        })
        .catch(error => {
          this.$message.error('添加失败');
          console.error(error);
        });
    }
  }
}
</script>
