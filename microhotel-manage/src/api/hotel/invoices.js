import request from '@/utils/request'

// 查询酒店账单列表
export function listInvoices(query) {
  return request({
    url: '/hotel/invoices/list',
    method: 'get',
    params: query
  })
}

// 查询酒店账单详细
export function getInvoices(id) {
  return request({
    url: '/hotel/invoices/' + id,
    method: 'get'
  })
}

// 新增酒店账单
export function addInvoices(data) {
  return request({
    url: '/hotel/invoices',
    method: 'post',
    data: data
  })
}

// 修改酒店账单
export function updateInvoices(data) {
  return request({
    url: '/hotel/invoices',
    method: 'put',
    data: data
  })
}

// 删除酒店账单
export function delInvoices(id) {
  return request({
    url: '/hotel/invoices/' + id,
    method: 'delete'
  })
}
