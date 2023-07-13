import request from "@/utils/request";

// get_track_list
export function simulation_api(data){
    return request({url:'/simulation',method:'post',data})
}

export function simulationInitdata(data){
    return request({url:'/simulation_Initdata',method:'get'})
}



export function fake(){
    return request({url:'/simulation_fake',method:'get'})
}

export function downFile(data){
    return request({url:'/download',method:'post',data})
}

export function SimulationExcute(data){
    return request({url:'/SimulationExcute',method:'post',data})
}
