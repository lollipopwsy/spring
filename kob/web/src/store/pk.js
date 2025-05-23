//存储所有和pk相关的变量

export default {
    state: {
       status:"matching",//匹配界面，playing表示对战界面
       socket:null,//前端和后端建立连接的socket
       opponent_username:"",//对手的用户名
       opponnet_photo:"",//对手的头像
       gamemap:null,//游戏地图
       a_id:0,//自己的id
       a_sx:0,
       a_sy:0,
       b_id:0,//对手的id
       b_sx:0,
       b_sy:0,
       gameObject:null,
       loser:"none",//none,all,A,B
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
        updateGame(state,game){
            state.gamemap = game.map;
            state.a_id=game.a_id;
            state.a_sx=game.a_sx;
            state.a_sy=game.a_sy;
            state.b_id=game.b_id;
            state.b_sx=game.b_sx;
            state.b_sy=game.b_sy;
        },
        // 
        updateGameObject(state,gameObject){
            state.gameObject = gameObject;
        },

        updateLoser(state,loser){
            state.loser = loser;
        },
    },
    actions: {//一般修改state的函数写在action里面

    },
    modules: {
    }

}