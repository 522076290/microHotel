import request from '@/utils/request'

// 查询酒店订单列表
export function listOrders(query) {
  return request({
    url: '/hotel/orders/list',
    method: 'get',
    params: query
  })
}

// 查询酒店订单详细
export function getOrders(id) {
  return request({
    url: '/hotel/orders/' + id,
    method: 'get'
  })
}

// 新增酒店订单
export function addOrders(data) {
  return request({
    url: '/hotel/orders',
    method: 'post',
    data: data
  })
}

// 修改酒店订单
export function updateOrders(data) {
  return request({
    url: '/hotel/orders',
    method: 'put',
    data: data
  })
}

// 删除酒店订单
export function delOrders(id) {
  return request({
    url: '/hotel/orders/' + id,
    method: 'delete'
  })
}
