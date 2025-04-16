import Vue from 'vue'
import VueRouter from 'vue-router'
import LoginPage from '../views/LoginPage.vue'
import TravelPage from '../views/TravelPage.vue'
import TravelPlan from "@/views/TravelPlan.vue";
import WeatherPage from '@/views/WeatherPage.vue'


Vue.use(VueRouter)

const routes = [
    { path: '/', redirect: '/login' },
    { path: '/login', component: LoginPage },
    { path: '/travel', component: TravelPage },
    { path: '/travel-plan', name: 'TravelPlan', component: TravelPlan},
    { path: '/weather', component: WeatherPage }
]

const router = new VueRouter({
    routes
})

// 路由守卫
// router.beforeEach((to, from, next) => {
//     if (to.path === '/login') return next()
//     const tokenStr = window.sessionStorage.getItem('token')
//     if (!tokenStr) return next('/login')
//     next()
// })

export default router