import request from '@/utils/request'

// 查询酒店房间列表
export function listRooms(query) {
  return request({
    url: '/hotel/rooms/list',
    method: 'get',
    params: query
  })
}

// 查询酒店房间详细
export function getRooms(id) {
  return request({
    url: '/hotel/rooms/' + id,
    method: 'get'
  })
}

// 新增酒店房间
export function addRooms(data) {
  return request({
    url: '/hotel/rooms',
    method: 'post',
    data: data
  })
}

// 修改酒店房间
export function updateRooms(data) {
  return request({
    url: '/hotel/rooms',
    method: 'put',
    data: data
  })
}

// 删除酒店房间
export function delRooms(id) {
  return request({
    url: '/hotel/rooms/' + id,
    method: 'delete'
  })
}
