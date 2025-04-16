import Vue from 'vue'
import App from './App.vue'
import router from './router'
import axios from 'axios'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import Vuetify from 'vuetify'
import 'vuetify/dist/vuetify.min.css'

Vue.use(Vuetify)
Vue.use(ElementUI)
Vue.config.productionTip = false

// 配置axios
Vue.prototype.$http = axios
axios.defaults.baseURL = 'http://localhost:8098/api/'
axios.interceptors.request.use(config => {
  // 排除weather路径的token验证
  if (!config.url.includes('/weather')) {
    config.headers.Authorization = window.sessionStorage.getItem('token')
  }
  return config
})

const vuetify = new Vuetify({
  theme: {
    dark: false,  // 这里是设置默认的主题，`false` 表示默认使用亮模式，`true` 则表示使用暗模式
    primary: '#1976D2', // 确保这是蓝色
    secondary: '#424242',
    accent: '#82B1FF',
    error: '#FF5252',
    info: '#2196F3',
    success: '#4CAF50',
    warning: '#FFC107'
  },
})

new Vue({
  router,
  vuetify,
  render: h => h(App)
}).$mount('#app')