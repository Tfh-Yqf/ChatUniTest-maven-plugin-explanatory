import request from "@/utils/request";

// get_track_list
export function excuteAPI(data){
    return request({url:'/position/Excute',method:'post',data})
}


export function init(data){
    return request({url:'/position/Init',method:'get'})
}


export function WangpingchaInit(){
    return request({url:'/position/WangpingchaInit',method:'get'})
}

export function ExcuteWangpingcha(data){
    return request({url:'/position/WangPingChaExcute',method:'post',data})
}

export function GetConfig(data){
    return request({url:'/position/GetConfig',method:'post',data})
}
