function loginApi(data) {
  return $axios({
    'url': '/employe/login',
    'method': 'post',
    data
  })
}

function logoutApi(){
  return $axios({
    'url': '/employe/logout',
    'method': 'post',
  })
}
