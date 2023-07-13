export default {
  state: () => ({
    email:'',
    token:''
  }),
  mutations: {
    setUser(state, item) {
      console.log(item)
      state.email = item.email;
      state.token = item.token;
    },
  }
}