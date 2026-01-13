import request from '@/utils/request'

// 查询项目模块功能列表
export function listFeatures(query) {
  return request({
    url: '/project/features/list',
    method: 'get',
    params: query
  })
}

// 查询项目模块功能详细
export function getFeatures(featureId) {
  return request({
    url: '/project/features/' + featureId,
    method: 'get'
  })
}

// 新增项目模块功能
export function addFeatures(data) {
  return request({
    url: '/project/features',
    method: 'post',
    data: data
  })
}

// 修改项目模块功能
export function updateFeatures(data) {
  return request({
    url: '/project/features',
    method: 'put',
    data: data
  })
}

// 删除项目模块功能
export function delFeatures(featureId) {
  return request({
    url: '/project/features/' + featureId,
    method: 'delete'
  })
}
