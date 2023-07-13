import request from '@/utils/request';


// GetToken
export function getToken(data) {
  return request({ url: `/user/get_token/`, method: 'post', data }, false)
}

export function login(data) {
  return request({ url: `/login`, method: 'post', data }, false)
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



// reg
export function reg(data) {
  return request({ url: `register`, method: 'post', data }, false)
}

export function sendemail(data) {
  return request({ url: `sendEmail`, method: 'post',data }, false)
}

