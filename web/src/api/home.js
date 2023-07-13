import request from "@/utils/request";

export function get_home(data){
    return request({url:'/api_task/get_home?province='+data,method:'get'});
}