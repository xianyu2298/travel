<template>
  <div class="postcode-container">
    <v-container fill-height>
      <v-row justify="center" align="start">
        <!-- 左侧部分：输入框和按钮 -->
        <v-col cols="12" md="5">
          <v-card class="elevation-12">
            <v-card-title class="headline primary white--text">
              邮政编码查询
            </v-card-title>
            <v-card-text class="pa-6">
              <v-text-field
                  v-model="postCodeInput"
                  label="请输入邮政编码"
                  outlined
                  dense
                  clearable
                  prepend-inner-icon="mdi-map-marker"
                  class="mb-4"
              ></v-text-field>
              <v-text-field
                  v-model="validCodeInput"
                  label="请输入验证码"
                  outlined
                  dense
                  clearable
                  prepend-inner-icon="mdi-shield-check"
                  class="mb-4"
              ></v-text-field>
              <valid-code :value.sync="trueValidCode"></valid-code>
              <!-- 按钮区域：返回按钮和查询按钮在同一行 -->
              <v-row justify="space-between">
                <v-btn
                    color="deep-purple accent-4"
                    @click="goBack"
                    large
                    class="mb-4"
                    style="width: 48%; color: black;"
                >
                  返回
                </v-btn>
                <v-btn
                    color="deep-purple accent-4"
                    @click="queryPostCode"
                    large
                    :loading="loading"
                    class="mb-4"
                    style="width: 48%; color: black;"
                >
                  查询
                </v-btn>
              </v-row>
            </v-card-text>
          </v-card>
        </v-col>

        <!-- 右侧部分：查询结果 -->
        <v-col cols="12" md="7">
          <v-card class="elevation-12">
            <v-card-text v-if="postCodeResult" class="pa-6">
              <v-list>
                <v-list-item
                    v-for="(item, index) in postCodeResult.list"
                    :key="index"
                    class="mb-2"
                >
                  <v-list-item-icon>
                    <v-icon color="primary">mdi-map-marker</v-icon>
                  </v-list-item-icon>
                  <v-list-item-content>
                    <v-list-item-title class="font-weight-bold">
                      {{ item.Province }} {{ item.City }} {{ item.District }}
                    </v-list-item-title>
                    <v-list-item-subtitle>
                      {{ item.Address }}
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
import ValidCode from "@/views/ValidCode.vue";
export default {
  components: { ValidCode },
  data() {
    return {
      postCodeInput: '',
      validCodeInput: '',
      trueValidCode: '',
      postCodeResult: null,
      loading: false
    };
  },
  methods: {
    // 返回上一页
    goBack() {
      this.$router.push('/travel')
    },

    queryPostCode() {
      if (!this.postCodeInput) {
        this.$message.error('请输入邮政编码');
        return;
      }

      if (this.validCodeInput !== this.trueValidCode) {
        this.$message.error('验证码错误');
        return;
      }

      this.loading = true;
      this.$http.get('/postcode/query', {
        params: { postcode: this.postCodeInput }
      }).then(response => {
        if (response.data && response.data.meta.status === 200) {
          this.postCodeResult = response.data.data;
        } else {
          this.$message.error('查询失败');
        }
      }).catch(error => {
        console.error('查询失败:', error);
        this.$message.error('查询失败');
      }).finally(() => {
        this.loading = false;
      });
    }
  }
}
</script>

<style scoped>
.postcode-container {
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


</style>
