import request from "@/utils/request";

// get_track_list
export function sound_api(data){
    return request({url:'/Sound/Excute',method:'post',data})
}

export function Sound_initdata(data){
    return request({url:'/Sound/Initdata',method:'get'})
}


export function Sound_GetFileDetail(data){
    return request({url:'/Sound/GetFileDetail',method:'post',data})
}

export function SoundTime_DuoXisngShi_Initdata(data){
    return request({url:'/SoundTime/DuoXiangShi/InitData',method:'get'})
}

export function SoundTime_DuoXisngShi_SspTime(data){
    return request({url:'/SoundTime/DuoXiangShi/SspTime',method:'post',data})
}

export function SoundTime_Zhichi_InitData(){
    return request({url:'/SoundTime/zhichi/Initdata',method:'get'})
}

export function SoundTime_BPAndRBP_InitData(){
    return request({url:'/SoundTime/BPAndRBP/Initdata',method:'get'})
}

export function SoundSpace_getPosition(data){
    return request({url:'/Sound/GetPosition',method:'post',data})
}

export function ExcuteMeta(data){
    return request({url:'/Sound/ExcuteMeta',method:'post',data})
}

export function ExcuteBP(data){
    return request({url:'/Sound/ExcuteBP',method:'post',data})
}

export function ExcuteKongJianOrEOF(data){
    return request({url:'/Sound/ExcuteKongJianOrEOF',method:'post',data})
}

export function ExcuteDuoXiang(data){
    return request({url:'/SoundTime/ExcuteDuoXiang',method:'post',data})
}


export function ExcuteSVM(data){
    return request({url:'/SoundTime/ExcuteSVM',method:'post',data})
}

export function ExcuteNCFanYan(data){
    return request({url:'/SoundTime/ExcuteNCFanYan',method:'post',data})
}

export function ExcuteSCFanYan(data){
    return request({url:'/SoundTime/ExcuteSCFanYan',method:'post',data})
}


export function UpdateServeData(data) {
    return request({url:'/SoundTime/updateServeData',method:'post',data})
}

