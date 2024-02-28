<template>
<div>
  <div @click.once="getAddress" id="BMap" style="height: 690px; z-index: 1; position: relative"></div>
  <el-container style="z-index: 2; position: absolute;top: 5px">
    <el-aside width="450px" style="height: 690px;">
      <el-card class="box-card" style=" margin: 25px; height: 86%; padding: 0">
        <el-carousel ref="carousel_main" trigger="click" indicator-position="outside" :autoplay="false" height="550px" style="margin: 0;padding: 0">
          <el-carousel-item style="position: relative">
            <el-card v-if="is_searched_succ" class="box-card" style="display: block;position: absolute;z-index: 1;float: end;width: 80px;height: 130px;margin-left: 78%;margin-top: 50px;padding: 0">
              <el-button @click="navigate()" type="primary" plain style="margin-left: -5px;padding: 10px">导航</el-button>
              <el-button @click="to_there(0)" type="success" plain style="margin-top: 15px;margin-left: -10px;padding: 10px">到这去</el-button>
            </el-card>
            <div style="z-index: 0">
              <el-progress v-if="is_process" :stroke-width="8" :percentage="percentage" :format="format" style="margin-top: -5px;margin-bottom: -3px;"></el-progress>
              <el-autocomplete
                  v-if="!is_searched"
                  class="inline-input"
                  v-model="content"
                  @input="input_data_set"
                  clearable
                  size="small"
                  suffix-icon="el-icon-search"
                  :fetch-suggestions="querySearch"
                  placeholder="请输入内容"
                  :trigger-on-focus="false"
                  @select="handleSelect"
                  style="width: 97%;margin: 5px"
              ></el-autocomplete>
              <el-page-header v-if="is_searched" @back="goBack" content="详情页面">
              </el-page-header>
              <el-descriptions title="地点信息" :column="1" style="margin-top: 15px">
                <el-descriptions-item label="名称">{{ city }}</el-descriptions-item>
                <el-descriptions-item label="具体地址">{{ address }}</el-descriptions-item>
                <el-descriptions-item label="经纬度">{{ x_y.x }}(经)；{{x_y.y}}(纬)</el-descriptions-item>
<!--                <el-descriptions-item label="备注">-->
<!--                  <el-tag size="small">{{ position_type }}</el-tag>-->
<!--                </el-descriptions-item>-->
                <el-descriptions-item label="天气情况">{{ weather }}</el-descriptions-item>
              </el-descriptions>
              <h4>周边推荐</h4>
              <el-carousel indicator-position="none" height="210px" style="background-color: rgba(153, 153, 255, 0.6); width: 99%">
                <el-carousel-item  v-for="(item, index) in surroundingPois" :key="index" style="display: flex;justify-content: center;align-items: center">
                  <template>
                      <el-descriptions :column="1" :label-style="{color: '#E6A23C'}">
                        <el-descriptions-item label="商铺名">{{ item.title }}</el-descriptions-item>
                        <el-descriptions-item label="联系电话">
                          <template v-if="item.phoneNumber != null">
                            {{ item.phoneNumber }}
                          </template>
                          <template v-else>无</template>
                        </el-descriptions-item>
                        <el-descriptions-item label="备注">
                          <el-tag size="small">{{item.Yi}}</el-tag>
                        </el-descriptions-item>
                        <el-descriptions-item label="商铺地址">{{ item.address }}</el-descriptions-item>
                        <el-descriptions-item :colon="false" content-style="margin-top: -25px">
                          <el-button @click="navigate()" type="primary" plain style="margin-left: -5px;padding: 10px">导航</el-button>
                          <el-button @click="to_there(0)" type="success" plain style="margin-top: 15px;margin-left: 25px;padding: 10px">到这去</el-button>
                        </el-descriptions-item>
                      </el-descriptions>
                  </template>
                </el-carousel-item>
              </el-carousel>
              <!--                  </el-descriptions>-->
            </div>
          </el-carousel-item>
          <el-carousel-item>
            <el-card id="card-2">
              <div>
                <h3>起始地：<span style="color: #409EFF; font-size: medium">我的位置</span></h3>
                <h3>目的地：<span style="color: #67C23A; font-size: medium">{{ destination }}</span></h3>
              </div>
            </el-card>
            <el-card style="margin-top: 5px; height: 71%">
              <el-tabs v-model="activeName" @tab-click="handleClick">
                <template v-if="!is_navigation">
                  <el-tab-pane label="公共交通" name="first" style="overflow: auto;height: 315px;width: fit-content;overflow-x: hidden">
                    <h3>{{navigate_res}}</h3>
                    <el-timeline v-for="(item, index) in navigation_router" :key="index">
                      <el-timeline-item :timestamp="item.description" placement="top" style="width: max-content;height: fit-content;padding: -5px;margin-left: -10px;margin-bottom: 5px" >
                        <el-card style="padding: -15px">
                          <h4>{{ item.type }}</h4>
                          <p style="width:150px;white-space: normal">{{ item.instruction }}</p>
                        </el-card>
                      </el-timeline-item>
                    </el-timeline>
                  </el-tab-pane>
<!--                  <el-tab-pane label="打车" name="second">打车</el-tab-pane>-->
<!--                  <el-tab-pane label="高铁" name="third">高铁</el-tab-pane>-->
<!--                  <el-tab-pane label="飞机" name="fourth">飞机</el-tab-pane>-->
                </template>
                <template v-if="is_navigation">
                  <el-tab-pane label="步行" name="fifth" style="overflow: auto;height: 315px;width: fit-content;overflow-x: hidden">
                    <h3>{{walk_content}}</h3>
                    <el-timeline v-for="(item, index) in walk_router" :key="index">
                      <el-timeline-item :timestamp="'总长：'+item.distance+'米；耗时：'+Math.floor(item.duration/60)+'分钟'" placement="top" style="width: max-content;height: fit-content;padding: -5px;margin-left: -10px;margin-bottom: 5px" >
                        <el-card style="padding: -15px">
                          <p style="width:150px;white-space: normal">{{ item.instruction }}</p>
                        </el-card>
                      </el-timeline-item>
                    </el-timeline>
                  </el-tab-pane>
<!--                  <el-tab-pane label="骑行" name="sixth">骑行</el-tab-pane>-->
                  <el-tab-pane label="驾车" name="seventh" style="overflow: auto;height: 315px;width: fit-content;overflow-x: hidden">
                    <h3>{{drive_content}}</h3>
                    <el-timeline v-for="(item, index) in driver_router" :key="index">
                      <el-timeline-item :timestamp="'总长：'+item.distance+'米；耗时：'+Math.floor(item.duration/60)+'分钟'" placement="top" style="width: max-content;height: fit-content;padding: -5px;margin-left: -10px;margin-bottom: 5px" >
                        <el-card style="padding: -15px">
                          <p style="width:150px;white-space: normal">{{ item.instruction }}</p>
                        </el-card>
                      </el-timeline-item>
                    </el-timeline>
                  </el-tab-pane>
                </template>
              </el-tabs>
            </el-card>
          </el-carousel-item>
        </el-carousel>
      </el-card>
    </el-aside>
    <el-main>
      <div  style="height: 85%;">
        <Set_new_labels @closeLabels="closeLabels" @remove_labels="remove_labels" style="position: fixed;" id="set_new_labels" v-show="is_set_new_labels" :data="set_new_labels_data"></Set_new_labels> <!---在地图上建立标签-->
          <el-card id="over_label" v-show="is_detail" style="position: fixed;width: 500px;background-color: rgba(255,255,255,0.3)">
            <el-descriptions :column="1" :label-style="{color: '#E6A23C'}">
              <el-descriptions-item label="地点名">{{ item.name }}</el-descriptions-item>
              <el-descriptions-item label="经纬度">{{ item.lng }}(经)；{{item.lat}}(纬)</el-descriptions-item>
              <el-descriptions-item label="备注">{{ item.detail }}</el-descriptions-item>
              <el-descriptions-item label="类型">{{item.open}}</el-descriptions-item>
              <el-descriptions-item label="详细地址">{{ item.address }}</el-descriptions-item>
              <el-descriptions-item label="创建时间">{{ item.timestamp }}</el-descriptions-item>
            </el-descriptions>
          </el-card>
      </div>
<!--      用于删除标记-->
      <el-card v-show="is_show" id="remove_label" style="background-color:rgba(255,255,255,0.3);position: fixed;width: 250px;height:120px;text-align: center">
        <span style="color: crimson">是否决定要删除？</span>
        <div style="margin-top: 15px">
          <el-button @click="is_show=false" size="small" type="info" style="margin-right: 50px">取消</el-button>
          <el-button @click="remove_label" size="small" type="primary">确定</el-button>
        </div>
      </el-card>
    </el-main>
  </el-container>
</div>
</template>

<script>
import myBMap from "@/utils/myBMap";
import Set_new_labels from "@/components/set_new_labels";
import iconB from "@/assets/address.png"
import request from "@/utils/request";

let BMap = {};
let map = {};
let label,deleteMarker;
export default {
  name: "show_page",
  components: {Set_new_labels},
  data(){
    return{
      is_process: false,
      is_searched: false,
      percentage: 0,
      navigate_res:"",
      content:'',
      router_content:'',
      data_set:[{"value":"无相关数据，请重新搜索","address":""}],
      city:"山东省济南市",
      old_city:"山东省济南市",
      address:"山东大学软件园学院",
      old_address:"山东大学软件园学院",
      weather:"",
      old_weather:"",
      x_y: {
        x: '',
        y: '',
      },
      old_x_y: {
        x: '',
        y: '',
      },
      precise_lng:'',
      precise_lat:'',
      old_precise_lng:'',
      old_precise_lat:'',
      surroundingPois: [],
      old_surroundingPois: [],
      destination:'',
      activeName:"",
      is_navigation:true,
      is_searched_succ:false,

      set_new_labels_data:{
        lng:'',
        lat:'',
        address:'',
        addressName:'',
      },
      is_set_new_labels:false,
      is_show:false,
      is_detail:false,
      // 用于保存后端传回来的标记信息
      markerList:[],
      item:{},
      navigation_router:[],
      walk_router:[],
      driver_router:[],
      walk_content:"",
      drive_content:"",
    }
  },
  methods:{
    format(percentage) {
      if (percentage>100){
        this.is_process=false;
      }
      return "";
    },
    to_there(index){
      this.$refs.carousel_main.setActiveItem(1);
      this.is_navigation=true;
      this.activeName="fifth";
      let BMapGL=window.BMapGL;

      var map = new BMapGL.Map("BMap");
      map.enableScrollWheelZoom(true);
      map.centerAndZoom(new BMapGL.Point(this.x_y.x, this.x_y.y), 19);
      var start=new BMapGL.Point(this.old_precise_lng,this.old_precise_lat);
      var end=new BMapGL.Point(this.precise_lng,this.precise_lat);
      let start_poi=this.old_precise_lat+","+this.old_precise_lng;
      let end_poi=this.precise_lat+","+this.precise_lng;
      if (index===0){
        var walking = new BMapGL.WalkingRoute(map, {renderOptions:{map: map, autoViewport: true}});
        request.get("/api/baidu/walk/"+start_poi+"/"+end_poi).then(res=>{
          this.walk_router=[];
          this.walk_content="总距离："+res.distance+"米；耗时："+Math.floor(res.duration/60)+"分钟";
          res.steps.forEach((e)=>{
            let temp = e.instruction.split("</b>")
            let data="";
            for (let i = 0; i < temp.length; i++) {
              data+=temp[i].split(",<b>").join().split("<b>").join();
            }
            e.instruction=data;
            this.walk_router.push(e)
          })
        })
        walking.search(start, end);
      }else if (index===1){
        var driving = new BMapGL.DrivingRoute(map, {renderOptions:{map: map, autoViewport: true}});
        request.get("/api/baidu/drive/"+start_poi+"/"+end_poi).then(res=>{
          for (let i = 0; i < Object.keys(res).length; i++) {
            if (res[i].traffic_condition===1){
              this.driver_router=[];
              this.drive_content="总距离："+res[i].distance+"米；耗时："+Math.floor(res[i].duration/60)+"分钟";
              res[i].steps.forEach((e)=>{
                let temp = e.instruction.split("</b>")
                let data="";
                for (let i = 0; i < temp.length; i++) {
                  data+=temp[i].split(",<b>").join().split("<b>").join();
                }
                e.instruction=data;
                this.driver_router.push(e)
              })
              break;
            }
          }
        })
        driving.search(start, end);
      }
    },
    navigate(){
      this.is_navigation=false;
      this.$refs.carousel_main.setActiveItem(1);
      this.activeName='first';

      let BMapGL=window.BMapGL;
      var map = new BMapGL.Map('BMap');
      map.centerAndZoom(new BMapGL.Point(this.precise_lng, this.precise_lat), 19);
      map.draggable=true
      map.enableScrollWheelZoom(true);
      let output='';
      var transit = new BMapGL.TransitRoute(map,{
        renderOptions: {map: map},
        onSearchComplete: (results)=>{
          // if (transit.getStatus() !== BMAP_STATUS_SUCCESS){
          //   return ;
          // }
          var plan = results.getPlan(0);
          output += '总时长：' + plan.getDuration(true);  //获取时间
          output += '总路程：' + plan.getDistance(true);  //获取距离
          this.navigate_res=output;
        },
      });
      var start=new BMapGL.Point(this.old_precise_lng,this.old_precise_lat);
      var end=new BMapGL.Point(this.precise_lng,this.precise_lat);
      let start_poi=this.old_precise_lat+","+this.old_precise_lng;
      let end_poi=this.precise_lat+","+this.precise_lng;
      let type_set=["火车","飞机","公交","驾车","步行","大巴"]
      request.get("/api/baidu/transit/"+start_poi+"/"+end_poi).then(res=>{
        this.navigation_router=[]
        this.navigate_res+="总需花费"+res.price+"元"
        let temp=res.steps;
        for (let i = 0; i < temp.length; i++) {
          let data={};
          for (let j = 0; j < temp[i].length; j++) {
            if (temp[i][j].instruction!==""){
              data.description="总长："+temp[i][j].distance+"米；耗时："+Math.floor(temp[i][j].duration/60)+"分钟";
              data.type=type_set[temp[i][j].type-1];
              data.instruction=temp[i][j].instruction
              break;
            }
          }
          this.navigation_router.push(data);
        }
      })
      transit.search(start, end);
    },
    navigation(item){
      this.destination=item.address
      this.precise_lng=item.lng
      this.precise_lat=item.lat
      this.navigate();
    },
    to_there_0(item){
      this.destination=item.address
      this.precise_lng=item.lng
      this.precise_lat=item.lat
      this.to_there(0);
    },

    input_data_set(){
      if (this.content!==""){
        request.get("/api/baidu/suggest?query="+this.content+"&region="+this.city+"&lat="+this.old_precise_lat
        +"&lng="+this.old_precise_lng).then(res=>{
          this.data_set=res
        })
      }
    },
    querySearch(queryString, cb) {
      var dataSet = [];
      this.data_set.forEach(e=>{
        dataSet.push({"value":e.name,"address":e.address,"lng":e.lng,"lat":e.lat})
      })
      // var results = dataSet.filter(queryString)!==null ? dataSet.filter(queryString) : dataSet;
      // 调用 callback 返回建议列表的数据

      cb(dataSet);
    },
    handleSelect(item) {
      this.is_process=true;
      this.percentage=0;
      let i=0;

      let interval =setInterval(()=>{
        if (this.percentage>100){
          this.is_searched=true;
          this.content=null;
          //这里最后要改成获取数据成功后加载完成；不能单纯地让其直接加载
          let BMap=window.BMap;
          let point = new BMap.Point(item.lng,item.lat);
          this.precise_lat=item.lat;
          this.precise_lng=item.lng;
          this.x_y.x=item.lng.toString().slice(0,6);
          this.x_y.y=item.lat.toString().slice(0,6);
          let gc = new BMap.Geocoder();
          this.destination=this.city=item.value;
          let that=this;
          setTimeout(()=>{
            gc.getLocation(point, function (rs) {
              that.address=rs.address;
              that.surroundingPois=rs.surroundingPois;
              //<<<<<<<<<<<<<<<<需要的位置信息在这获取
            });
            this.is_searched_succ=true;
          },50)
          request.get("/api/baidu/weather/"+item.lat+"/"+item.lng).then(res=>{
            this.weather=res.forecasts[0].text_day+"，最高气温："+res.forecasts[0].high+"，最低气温："+res.forecasts[0].low
                +"，"+res.forecasts[0].wd_day+res.forecasts[0].wc_day;
          })
          clearInterval(interval)
        }
        i+=2;
        if (i===20){
          this.percentage=this.percentage+i;
          i=0;
        }
      },30)

    },
    handleClick(tab) {
      if (tab.label==="驾车"){
        this.to_there(1);
      }else if (tab.label==="步行"){
        this.to_there(0);
      }
    },
    // 从搜索页结果页返回搜索页
    goBack() {
      this.is_searched=false;
      this.address=this.old_address;
      this.weather=this.old_weather;
      this.city=this.old_city;
      this.x_y=this.old_x_y;
      this.surroundingPois=this.old_surroundingPois;
      this.destination="";
      map = new BMap.Map("BMap");
      map.centerAndZoom(new BMap.Point(this.old_precise_lng,this.old_precise_lat), 19);
      map.enableScrollWheelZoom(true);
    },

    // 移除地图上的标记
    // 要修改为是自己创建的才可以删除
    remove_label(){
      let markerArr = this.get_right_marker();
      map.removeOverlay(markerArr[label]);
      this.is_show=false;
      if (markerArr.length===1) this.is_set_new_labels=false;
      request.delete("/api/point/"+this.$store.state.user.phone+"/"+this.markerList[label].id
      ).then(res=>{
        if (Object.keys(res).length!==0){
          this.$message({
            message:"删除成功",
            type:'success',
            showClose:true,
          })
          request.get("/api/point/"+this.$store.state.user.phone).then(res=>{
            for (let i = 0; i < res.length; i++) {
              res[i].timestamp=new Date(res[i].timestamp);
              if (res[i].open===0){
                res[i].open="共有"
              }else {
                res[i].open="私人"
              }
              res[i].is_change=false;
            }
            this.markerList=res
          })
        }
      })
    },
    remove_labels(){
      this.is_set_new_labels=false;
      this.is_detail=false;
      map.removeOverlay(deleteMarker);
    },
    closeLabels(item){
      this.is_set_new_labels=false;
      this.is_detail=false;
      this.markerList.push(item)
    },
    set_marker(item){
      let point = new BMap.Point(item.lng,item.lat);
      let icon= new BMap.Icon(iconB,new BMap.Size(40,40),{anchor: new BMap.Size(30,30), imageSize: new BMap.Size(40,40)})
      let marker= new BMap.Marker(point,{icon:icon})
      marker.lng=item.lng;
      marker.lat=item.lat;
      if (!this.markerList.includes(item)){
        map.addOverlay(marker);
        this.markerList.push(item)
      }
    },
    get_right_marker(){
      let markerArr = map.getOverlays();
      let points=[];
      for (let i = 0; i < markerArr.length; i++) {
        if (markerArr[i].isVisible()){
          points.push(markerArr[i])
        }
      }
      return points;
    },
    // 在地图中对某地点进行标注
    getAddress(){
      let that = this;let marker;
      map.addEventListener('dragstart', function () {
        that.is_set_new_labels=false;
        that.is_show=false;
        that.is_detail=false;
      })
      map.addEventListener('zoomstart', function () {
        that.is_set_new_labels=false;
        that.is_show=false;
        that.is_detail=false;

        // 对图标进行缩放
        let markerArr=that.get_right_marker();
        let zoom=map.getZoom();
        for (let i = 0; i < markerArr.length; i++) {
          let icon= new BMap.Icon(iconB,new BMap.Size(40*zoom/19,40*zoom/19),
              {anchor: new BMap.Size(30*zoom/19,30*zoom/19), imageSize: new BMap.Size(40*zoom/19,40*zoom/19)})
          markerArr[i].setIcon(icon);
        }
      })

      map.addEventListener("click", function (e) {
        let point = new BMap.Point(e.point.lng,e.point.lat);
        // let lng=e.point.lng.toString().slice(0,6);
        // let lat=e.point.lat.toString().slice(0,6);

        let bool=true;
        let markerArr=that.get_right_marker();
        for (let i = 0; i < markerArr.length; i++) {

          if (Math.abs(markerArr[i].lng-e.point.lng)<0.0005&&Math.abs(markerArr[i].lat-e.point.lat)<0.0005){
            bool=false;
            if (Math.abs(markerArr[i].lng-e.point.lng)<0.0001&&Math.abs(markerArr[i].lat-e.point.lat)<0.0001){
              let labels = document.getElementById('remove_label');
              labels.style.left=e.screenX-100+'px'
              labels.style.top=e.screenY-200+'px'
              that.is_show=true;
              label=i;
            }
            break;
          }
        }
        if (bool){
          let gc = new BMap.Geocoder();
          that.set_new_labels_data.lng=e.point.lng;
          that.set_new_labels_data.lat=e.point.lat;
          let icon= new BMap.Icon(iconB,new BMap.Size(40,40),{anchor: new BMap.Size(30,30), imageSize: new BMap.Size(40,40)})
          marker= new BMap.Marker(point,{icon:icon})
          marker.lng=e.point.lng;
          marker.lat=e.point.lat;
          deleteMarker=marker;
          map.addOverlay(marker);


          gc.getLocation(point, function (rs) {
            that.set_new_labels_data.address=rs.address;
            that.set_new_labels_data.addressName = rs.business!==''?rs.business.split("/")[0]:rs.address;
          });
          that.is_set_new_labels=true;
          that.is_detail=false;
          that.change_position(e,"set_new_labels")
        }
      })
      map.addEventListener("mouseover", (e)=> {
          let markerArr=this.markerList;
          let point=e.currentTarget.re;
          let temp=null;
          let min=100,j=-1;
          for (let i = 0; i < markerArr.length; i++) {
            let x=Math.pow(markerArr[i].lng-point.lng,2)+Math.pow(markerArr[i].lat-point.lat,2)
            if (x<min){
              min=x;
              j=i;
            }
          }
          if (j!==-1){temp=markerArr[j]}
          if (!this.is_set_new_labels&&temp!==null){
            this.item=temp;
            this.is_detail=true;
            this.change_position(e,"over_label")
          }
      })
    },
    change_position(e,str){
      let labels = document.getElementById(str);
      labels.style.left=e.screenX-50+'px'
      labels.style.top=e.screenY-400+'px'
      if (e.screenY<500&&e.screenX>window.innerWidth-200){
        labels.style.left=e.screenX-400+'px'
        labels.style.top=e.screenY-50+'px'
      }else if (e.screenY-400<100){
        labels.style.top=e.screenY-50+'px'
      }else if (e.screenX>window.innerWidth-200){
        labels.style.left=e.screenX-400+'px'
      }
    }
  },
  created() {
    myBMap.init().then(()=>{
      BMap = window.BMap;
      let that=this;
      let geolocation = new BMap.Geolocation();
      // 创建百度地理位置实例，代替 navigator.geolocation
      geolocation.getCurrentPosition( function (e){
        // eslint-disable-next-line no-undef
        if (this.getStatus() === BMAP_STATUS_SUCCESS) {
          // const mk = new BMap.Marker(e.point);
          // map.addOverlay(mk);
          // map.panTo(e.point);
          // 百度 geolocation 的经纬度属性不同，此处是 point.lat 而不是 coords.latitude

          let point = new BMap.Point(e.point.lng,e.point.lat);
          that.old_precise_lng=e.point.lng;
          that.old_precise_lat=e.point.lat;
          that.old_x_y.x=that.x_y.x=e.point.lng.toString().slice(0,6);
          that.old_x_y.y=that.x_y.y=e.point.lat.toString().slice(0,6);
          let gc = new BMap.Geocoder();

          gc.getLocation(point, function (rs) {
            that.old_address=that.address=rs.address;
            that.old_city=that.city = rs.business.split("/")[0];
            if (rs.business===''){
              that.old_city=that.city=rs.address
            }
            that.old_surroundingPois=that.surroundingPois=rs.surroundingPois;
            //<<<<<<<<<<<<<<<<需要的位置信息在这获取
          });
          let temp="";
          request.get("/api/baidu/suggest?query="+that.city+"&region="+that.city+"&lat="+e.point.lat
              +"&lng="+e.point.lng).then(res=>{
                temp=res[0];
            request.get("/api/baidu/weather/"+temp.lat+"/"+temp.lng).then(res=>{
              that.old_weather=that.weather=res.forecasts[0].text_day+"，最高气温："+res.forecasts[0].high+"，最低气温："+res.forecasts[0].low
              +"，"+res.forecasts[0].wd_day+res.forecasts[0].wc_day;
            })
          })
          map = new BMap.Map("BMap");
          map.centerAndZoom(new BMap.Point(e.point.lng, e.point.lat), 19);
          map.enableScrollWheelZoom(true);
          // map.setHeading(64.5);
          // map.setTilt(73);

        } else {
          alert("定位失败，请手动选择区域或重新定位");
          this.showloading = false;
        }
      },function (err) {
        console.log(err.code);
      },{
        enableHighAccuracy: true, //位置是否精确获取
        timeout: 5000,//获取位置允许的最长时间
        maximumAge: 0 //多久更新获取一次位置
      });
    });

  }

}
</script>

<style scoped>
/deep/.el-progress-bar__outer{
  width: 355px;
}
</style>