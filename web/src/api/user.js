import request from '@/utils/request';


// GetToken
export function getToken(data) {
  return request({ url: `/user/get_token/`, method: 'post', data }, false)
}

// RefreshToken
export function refreshToken(data) {
  return request({ url: `/user/refresh_token/`, method: 'post', data })
}

// GetInfo
export function getInfo(params) {
  return request({ url: `/user/info/`, method: 'get', params })
}

// SetPassword
export function setPassword(data) {
  return request({ url: `/user/set_password/`, method: 'post', data })
}

// 常用功能
export function getCommonFunctions(params) {
  return request({ url: `/user/common_functions/`, method: 'get', params })
}

// 设置常用功能
export function setCommonFunctions(data) {
  return request({ url: `/user/set_common_functions/`, method: 'post', data })
}

// reg
export function reg(data) {
  var temp = {...data};
  delete data.code;
  return request({ url: `/api_user/register/`+temp.code, method: 'post', data }, false)
}

export function sendemail(data) {
  return request({ url: `/api_user/`+data, method: 'get' },false)
}

export function login(data) {
  return request({ url: `/loginbyPassword`, method: 'post',data }, false)
}

export function checkToken(data) {
  return request({ url: `/verifyToken`, method: 'post',data }, false)
}


