import request from '@/utils/request';



export function startProject(data) {
    return request({ url: `/project/start`, method: 'post', data},false)
}

export function getProjectStatus(data) {
    return request({ url: `/project/getStatus`, method: 'post', data }, false)
}

