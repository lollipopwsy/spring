//存储所有和pk相关的变量

export default {
    state: {
       status:"matching",//匹配界面，playing表示对战界面
       socket:null,//前端和后端建立连接的socket
       opponent_username:"",//对手的用户名
       opponnet_photo:"",//对手的头像
       gamemap:null,//游戏地图
    },
    getters: {
    },
    mutations: {//用于修改数据
        updateSocket(state,socket){
            state.socket = socket;
        },
        updateOpponent(state,opponent){
            state.opponent_username = opponent.username;
            state.opponent_photo = opponent.photo;
        },
        updateStatus(state,status){
            state.status = status;
        },
        // 写一个辅助函数更新地图
        updateGamemap(state,gamemap){
            state.gamemap = gamemap;
        },
    },
    actions: {//一般修改state的函数写在action里面

    },
    modules: {
    }

}