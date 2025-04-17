import Vue from 'vue'
import VueRouter from 'vue-router'
import LoginPage from '../views/LoginPage.vue'
import TravelPage from '../views/TravelPage.vue'
import TravelPlan from "@/views/TravelPlan.vue";
import WeatherPage from '@/views/WeatherPage.vue'
import MoneyPage from "@/views/MoneyPage.vue";
import RegisterPage from '@/views/RegisterPage.vue';
import PostCodePage from '@/views/PostCodePage.vue';
import PhonePage from '@/views/PhonePage.vue';
import TrainPage from '@/views/TrainPage.vue'

Vue.use(VueRouter)

const routes = [
    { path: '/', redirect: '/login' },
    { path: '/login', component: LoginPage },
    { path: '/travel', component: TravelPage },
    { path: '/travel-plan', name: 'TravelPlan', component: TravelPlan},
    { path: '/weather', component: WeatherPage },
    { path: '/money', name: 'MoneyPage', component: MoneyPage },
    { path: '/register', component: RegisterPage },
    { path: '/postcode', name: 'PostCodePage', component: PostCodePage},
    { path: '/phone', name: 'PhonePage', component: PhonePage },
    { path: '/train', name: 'TrainPage', component: TrainPage }
]

const router = new VueRouter({
    routes
})

export default router