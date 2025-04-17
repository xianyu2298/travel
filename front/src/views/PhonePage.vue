<template>
  <div class="phone-container">
    <v-container fill-height>
      <v-row justify="center" align="start">
        <!-- 左侧部分：输入框和按钮 -->
        <v-col cols="12" md="5">
          <v-card class="elevation-12">
            <v-card-title class="headline primary white--text">
              手机号归属地查询
            </v-card-title>
            <v-card-text class="pa-6">
              <v-text-field
                  v-model="phoneInput"
                  label="请输入手机号"
                  outlined
                  dense
                  clearable
                  prepend-inner-icon="mdi-phone"
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
                    @click="queryPhone"
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
            <v-card-text v-if="phoneResult" class="pa-6">
              <v-list>
                <v-list-item>
                  <v-list-item-content>
                    <v-list-item-title class="font-weight-bold">
                      省份: {{ phoneResult.province }}
                    </v-list-item-title>
                    <v-list-item-subtitle>
                      城市: {{ phoneResult.city }}
                    </v-list-item-subtitle>
                    <v-list-item-subtitle>
                      区号: {{ phoneResult.areacode }}
                    </v-list-item-subtitle>
                    <v-list-item-subtitle>
                      邮编: {{ phoneResult.zip }}
                    </v-list-item-subtitle>
                    <v-list-item-subtitle>
                      运营商: {{ phoneResult.company }}
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
      phoneInput: '',
      phoneResult: null,
      loading: false,
      validCodeInput: '',
      trueValidCode: '',
    };
  },
  methods: {
    goBack() {
      this.$router.push('/travel')
    },
    queryPhone() {
      if (!this.phoneInput) {
        this.$message.error('请输入手机号');
        return;
      }

      if (this.validCodeInput !== this.trueValidCode) {
        this.$message.error('验证码错误');
        return;
      }

      this.loading = true;
      this.$http.get('/phone/query', {
        params: { phone: this.phoneInput }
      }).then(response => {
        if (response.data && response.data.meta.status === 200) {
          this.phoneResult = response.data.data;
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
.phone-container {
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