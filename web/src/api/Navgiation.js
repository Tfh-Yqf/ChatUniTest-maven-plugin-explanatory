import request from "@/utils/request";


export function NavgiationDanyi(data){
    return request({url:'/NavigationDanyi',method:'post',data})
}

export function Navgiation_initdata(data){
    return request({url:'/Navigation_initdata',method:'get'})
}

export function Navgiation_initdata_allow(){
    return request({url:'/test',method:'get'})
}

export function gravity_initdata(data){
    return request({url:'/gravity_initdata',method:'get'})
}

export function gravity_excute(data){
    return request({url:'/gravity_excute',method:'post',data})
}

export function NavigationGetConfig(data){
    return request({url:'/NavigationGetConfig',method:'post',data})
}

export function GravityT4(){
    return request({url:'/t4',method:'get'})
}

export function NavigationExcuteOther(data){
    return request({url:'/ExcuteOther',method:'post',data})
}