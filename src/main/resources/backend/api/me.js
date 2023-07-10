
function getMemberList (params) {
  return $axios({
    url: '/employe/page',
    method: 'get',
    params
  })
}

// 修改---启用禁用接口
function enableOrDisableEmployeee (params) {
  return $axios({
    url: '/employe',
    method: 'put',
    data: { ...params }
  })
}

// 新增---添加商家
function addEmployeee (params) {
  return $axios({
    url: '/employe',
    method: 'post',
    data: { ...params }
  })
}

// 修改---添加商家
function editEmployeee (params) {
  return $axios({
    url: '/employe',
    method: 'put',
    data: { ...params }
  })
}

// 修改页面反查详情接口
function queryEmployeeByIde (id) {
  return $axios({
    url: `/employe/${id}`,
    method: 'get'
  })
}

const deleCategorye = (ids) => {
  return $axios({
    url: '/employe',
    method: 'delete',
    params: { ids}
  })
}