export default {
    init: function () {
        const AK = '';  //你的AK
        const BMap_URL = "https://api.map.baidu.com/api?v=2.0&ak=" +
            AK + "&s=1&callback=onBMapCallback";
        return new Promise((resolve) => {
            // 如果已加载直接返回
            if (typeof BMap !== 'undefined') {
                /* global BMap*/
                resolve(BMap);
                return true;
            }
            // 百度地图异步加载回调处理
            window.onBMapCallback = function () {
                resolve(BMap);
            };
            // eslint-disable-next-line no-unused-vars
            let getCurrentCityName = function () {
                return new Promise(function (resolve) {
                    let myCity = new BMap.LocalCity()
                    myCity.get(function (result) {
                        resolve(result.name)
                    })
                })
            }
            // 插入script脚本
            let scriptNode = document.createElement('script');
            scriptNode.setAttribute('type', 'text/javascript');
            scriptNode.setAttribute('src', BMap_URL);
            document.body.appendChild(scriptNode);
        });
    }
}

