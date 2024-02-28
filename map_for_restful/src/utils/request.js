import axios from 'axios'
const request = axios.create({
  timeout: 5000
})

// request拦截器
//可以自请求发送前对请求做些处理
//比如统加token, 对请求参数统加密
request.interceptors.request.use(config => {
    config.headers['Content-Type'] = 'application/json;charset=utf-8';
// config . headers[ 'token'] = user. token; // 设置请求头
  config.data=JSON.stringify(config.data);
    return config
  },
  error =>{
return Promise.reject(error)
});
// response 拦截器
//可以在接[响应后统处理结果
request.interceptors.response.use(
  response => {
    let res =response.data;
//如果是返回的文件
    if (response.config.responseType === 'blob') {
      return res
    }
//兼容服务端返回的字符串数据
    if (typeof res === 'string') {
      res = res ? JSON.parse(res) : res
    }
    return res;
  },
  error => {
    console.log('err'+ error) // for debug
    return Promise.reject(error)
  }
)
export default request