import ApiClient from '../base-api'

//对应后台接口请求
const labBookingApi = new ApiClient('/admin/lab-booking')

// 审核
labBookingApi.process = (data) => {
  return labBookingApi.put('process', data)
}

// 获取预约规则
labBookingApi.getBookingRule = (data) => {
  return labBookingApi.post('bookingRule', data)
}
export default labBookingApi
