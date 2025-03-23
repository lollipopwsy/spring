import { createStore } from 'vuex'
import ModuleUser from './user'//user加到全局Module里面
import ModulePk from './pk'//pk加到全局Module里面

export default createStore({
  state: {
  },
  getters: {
  },
  mutations: {
  },
  actions: {
  },
  modules: {
    user:ModuleUser,
    pk:ModulePk,
  }
})
