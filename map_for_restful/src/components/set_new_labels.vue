<!--在地图上建立标签-->
<template>
<div style="">
  <el-card class="box-card">
    <el-descriptions :column="1" :label-style="{color: '#E6A23C'}">
      <el-descriptions-item label="地点名">{{ data.addressName }}</el-descriptions-item>
      <el-descriptions-item label="经纬度">{{ data.lng }}(经)；{{data.lat}}(纬)</el-descriptions-item>
      <el-descriptions-item label="备注">
        <el-input
            type="textarea"
            placeholder="请输入内容"
            v-model="textarea"
            maxlength="20"
            show-word-limit
        >
        </el-input>
      </el-descriptions-item>
      <el-descriptions-item :colon="false" content-style="margin-top: 5px">
        <template>
          <el-radio v-model="radio" label="0">公有</el-radio>
          <el-radio v-model="radio" label="1">私人</el-radio>
        </template>
      </el-descriptions-item>
      <el-descriptions-item label="详细地址">{{ data.address }}</el-descriptions-item>
      <el-descriptions-item :colon="false" content-style="margin-top: -15px">
        <el-button @click="deleteMarker" type="primary" plain style="margin-left: 25px;padding: 10px">取消</el-button>
        <el-button @click="decideMarker" type="success" plain style="margin-top: 15px;margin-left: 60px;padding: 10px">确定</el-button>
      </el-descriptions-item>
    </el-descriptions>
  </el-card>
</div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "set_new_labels",
  props:{
    data:{},
  },
  data(){
    return{
      radio:"0",
      textarea:'',
    }
  },
  methods:{
    // 将地点数据提交到后端，保存到数据库
    decideMarker(){
      if (this.$store.state.user.phone!==""){
        let timestamp=new Date();
        let temp={
          lng:this.data.lng,
          lat:this.data.lat,
          open:this.radio !== 1,
          detail:this.textarea,
          owner:this.$store.state.user.phone,
          address:this.data.address,
          name:this.data.addressName,
          timestamp
        }

        request.post("/api/point",temp).then(res=>{
          if (Object.keys(res).length!==0){
            this.$message({
              message:"添加成功",
              type:'success'
            })
          }
        })
        temp.open=temp.open===1?"共有":"私人";
        this.$emit("closeLabels",temp)
      }else {
        this.$message({
          message:"请先登录，再标记地点",
          type:"warning",
          showClose:true,
        })
      }

    },
    deleteMarker(){
      this.$emit("remove_labels");
    },
  },
}
</script>

<style scoped>
.box-card{
  width: 500px;
  background-color: rgba(255,255,255,0.3)
}
/*/deep/.el-descriptions__body{*/
/*  background-color: rgba(255,255,255,0.5)*/
/*}*/
</style>