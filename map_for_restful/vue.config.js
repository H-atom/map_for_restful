module.exports = {
  // publicPath: './',
  productionSourceMap: false,
  devServer: {
    proxy: {
      '/api': {
        target: "http://localhost:8888",//这个是你要访问的接口地址
        changeOrigin: true,
        pathRewrite: {//重写地址  比如说 你的接口地址是'https://188.62.63/xxyy/getUser' 你请求得时候地址只需要写'/test/getUser'
          '^/api': ''
        }
      }
    }
  }
}
