import ApiClient from '../base-api'

//对应后台接口请求
const openApi = new ApiClient('/admin/open')

// 同步开放信息
openApi.syncOpen = () => {
  return openApi.post('sync')
}

// 恢复默认
openApi.reset = (id) => {
  return openApi.put(`reset/${id}`)
}

export default openApi
