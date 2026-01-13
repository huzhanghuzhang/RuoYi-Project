import request from '@/utils/request'

// 查询项目模块列表
export function listModels(query) {
  return request({
    url: '/project/models/list',
    method: 'get',
    params: query
  })
}

// 查询项目模块详细
export function getModels(moduleId) {
  return request({
    url: '/project/models/' + moduleId,
    method: 'get'
  })
}

// 新增项目模块
export function addModels(data) {
  return request({
    url: '/project/models',
    method: 'post',
    data: data
  })
}

// 修改项目模块
export function updateModels(data) {
  return request({
    url: '/project/models',
    method: 'put',
    data: data
  })
}

// 删除项目模块
export function delModels(moduleId) {
  return request({
    url: '/project/models/' + moduleId,
    method: 'delete'
  })
}
