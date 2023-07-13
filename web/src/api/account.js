import request from '@/utils/request';


// Role
export function roleList(params) {
  return request({ url: `/roles/`, method: 'get', params })
}



export function roleDestroy(data) {
  return request({ url: `/api_user/roles/${data.id}/roleDestroy`, method: 'delete', data })
}


// 删除用户
export function userDestroy(data) {
  return request({ url: `/api_user/${data}/userDestory`, method: 'delete', data })
}
//重置密码
export function userResetPassword(data) {
  return request({ url: `/api_user/${data}/reset_password/`, method: 'get' })
}
//角色列表
export function get_role(params) {
  return request({ url: `/api_user/roles/get_role`, method: 'get' })
}
//用户更新
export function userUpdate(data) {
  return request({ url: `/api_user/userUpdate`, method: 'post', data })
}
// user list
export function userList(params) {
  return request({ url: `/api_user/userlist?skip=`+params, method: 'get', params })
}
// user add
export function userCreate(data) {
  return request({ url: `/api_user/userCreate`, method: 'post', data })
}
// permission list
export function permissionList(params) {
  return request({ url: `/api_user/permission_groups`, method: 'get' })
}
//role create
export function roleCreate(data) {
  return request({ url: `/api_user/roles/roleCreate`, method: 'post', data })
}
//role update
export function roleUpdate(data) {
  return request({ url: `/api_user/roles/${data.id}/roleUpdate`, method: 'post', data })
}
