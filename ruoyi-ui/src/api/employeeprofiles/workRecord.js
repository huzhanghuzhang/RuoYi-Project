import request from '@/utils/request'

// 查询履职档案列表
export function listWorkRecord(query) {
  return request({
    url: '/employeeprofiles/workRecord/list',
    method: 'get',
    params: query
  })
}

// 查询履职档案详细
export function getWorkRecord(id) {
  return request({
    url: '/employeeprofiles/workRecord/' + id,
    method: 'get'
  })
}

// 新增履职档案
export function addWorkRecord(data) {
  return request({
    url: '/employeeprofiles/workRecord',
    method: 'post',
    data: data
  })
}

// 修改履职档案
export function updateWorkRecord(data) {
  return request({
    url: '/employeeprofiles/workRecord',
    method: 'put',
    data: data
  })
}

// 删除履职档案
export function delWorkRecord(id) {
  return request({
    url: '/employeeprofiles/workRecord/' + id,
    method: 'delete'
  })
}
