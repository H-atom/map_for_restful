import Vue from 'vue'
import App from './App.vue'
import axios from 'axios'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import store from "@/store/store";

Vue.config.productionTip = false

Vue.prototype.axios = axios
Vue.use(ElementUI);
new Vue({
  render: h => h(App),
  store
}).$mount('#app')
