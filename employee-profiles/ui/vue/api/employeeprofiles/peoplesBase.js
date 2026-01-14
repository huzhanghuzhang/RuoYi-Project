import request from '@/utils/request'

// 查询基础资料管理列表
export function listPeoplesBase(query) {
  return request({
    url: '/employeeprofiles/peoplesBase/list',
    method: 'get',
    params: query
  })
}

// 查询基础资料管理详细
export function getPeoplesBase(peopleId) {
  return request({
    url: '/employeeprofiles/peoplesBase/' + peopleId,
    method: 'get'
  })
}

// 新增基础资料管理
export function addPeoplesBase(data) {
  return request({
    url: '/employeeprofiles/peoplesBase',
    method: 'post',
    data: data
  })
}

// 修改基础资料管理
export function updatePeoplesBase(data) {
  return request({
    url: '/employeeprofiles/peoplesBase',
    method: 'put',
    data: data
  })
}

// 删除基础资料管理
export function delPeoplesBase(peopleId) {
  return request({
    url: '/employeeprofiles/peoplesBase/' + peopleId,
    method: 'delete'
  })
}
