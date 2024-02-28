import Vue from 'vue';

import Vuex from 'vuex';
Vue.use(Vuex);

export default new Vuex.Store({
    state:{
        user:{
            id:'',
            phone:'',
            username:'',
            password:"",
        },
    },
    mutations:{
       login(state,value){
           state.user=value;
       }
    },
    actions:{

    },
    getters:{

    }
})