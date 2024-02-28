<template>
<div>
  <el-dialog title="用户登录" :visible.sync="dialogFormVisible" style="width: 50%;margin: auto;min-width: 800px" @closed="reset">
  <LoginView v-show="loginOrRegister" @login="loginSuccess" @changeRegisterView="changeViewOnLoginOrRegister" ref="resetForm"></LoginView>
  <RegisterView v-show="!loginOrRegister" @changeLoginView="changeViewOnLoginOrRegister" ref="resetForm"></RegisterView>
  </el-dialog>
  <el-container>
    <el-header style="background: linear-gradient(70deg, #0000FF, #6666FF, #9999FF); display: flex">
      <h1 style="color: greenyellow">欢迎使用我们的地图应用</h1>
      <el-button type="text" @click="enter" class="button" v-if="!visibleEnter">登录</el-button>
      <el-dropdown @command="handleCommand" v-if="visibleEnter" style="margin-left: 75%">
        <span><el-button :icon="icon" circle size="medium" style="margin-top: 16px"></el-button><span style="color: white;margin-left: 10px">{{$store.state.user.username}}</span></span>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item command="个人资料">个人资料</el-dropdown-item>
          <el-dropdown-item command="地点标记">地点标记</el-dropdown-item>
          <el-dropdown-item command="e" divided>退出登录</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
      <el-drawer
          :title="personTitle"
          :visible.sync="drawer"
          :direction="direction"
          :before-close="handleClose"
          size="40%"
          v-if="visibleEnter">
        <!--          个人资料-->
        <el-form label-position="right" label-width="80px" v-if="userInfor" style="margin: 5px" :rules="rules" ref="changeMessForm" :model="form">
          <el-button :icon="icon" circle size="medium" style="margin-left: 40%"></el-button>
          <el-form-item label="昵称: " label-width="80px" style="align-items: center" prop="newName">
            <template v-if="!changeName">
              <span class="span">{{$store.state.user.username}}</span>
              <i class="el-icon-edit" style="margin-left: 5px" @click="changeMess('changeName')"></i>
            </template>
            <template v-if="changeName">
              <el-input placeholder="请输入内容" v-model="form.newName" clearable size="small" style="width: max-content"></el-input>
              <el-button type="primary" round size="small" @click="changeUsername" style="margin-left: 10px">确定</el-button>
            </template>
          </el-form-item>
          <el-form-item label="密码: " label-width="80px" style="align-items: center;">
            <template v-if="!changePassword">
              <span class="span">**********</span>
              <i class="el-icon-edit" style="margin-left: 5px" @click="changeMess('changePassword')"></i>
            </template>
            <template v-if="changePassword">
              <el-form-item prop="password" class="el-form-item">
                <el-input placeholder="请输入旧密码" size="small" v-model="form.password" show-password style="width: max-content"></el-input>
              </el-form-item>
              <el-form-item prop="newPassword" class="el-form-item">
                <el-input placeholder="请输入新密码" size="small" v-model="form.newPassword" show-password style="width: max-content"></el-input>
              </el-form-item>
              <el-form-item prop="againPassword" class="el-form-item">
                <el-input placeholder="请确认密码" size="small" v-model="form.againPassword" show-password style="width: max-content"></el-input>
              </el-form-item>
              <el-form-item class="el-form-item">
                <el-button type="success" round size="small" @click="changeUserPassword">确定</el-button>
              </el-form-item>
            </template>
          </el-form-item>
        </el-form>
        <template v-if="is_show_markerList">
          <el-card style="margin: 10px" v-for="(item,index) in markerList" :key="index">
            <el-descriptions :column="1" :label-style="{color: '#E6A23C'}">
              <el-descriptions-item label="地点名">{{ item.name }}</el-descriptions-item>
              <el-descriptions-item label="经纬度">{{ item.lng }}(经)；{{item.lat}}(纬)</el-descriptions-item>
              <el-descriptions-item v-if="!item.is_change" label="备注">{{ item.detail }}</el-descriptions-item>
              <el-descriptions-item v-if="item.is_change" label="备注">
                <el-input
                    type="textarea"
                    placeholder="请输入内容"
                    v-model="textarea"
                    maxlength="20"
                    show-word-limit
                >
                </el-input>
              </el-descriptions-item>
              <el-descriptions-item v-if="item.is_change" :colon="false" content-style="margin-top: 5px">
                <template>
                  <el-radio v-model="radio" label="1">公有</el-radio>
                  <el-radio v-model="radio" label="0">私人</el-radio>
                </template>
              </el-descriptions-item>
              <el-descriptions-item v-if="!item.is_change" label="类型">{{item.open}}</el-descriptions-item>
              <el-descriptions-item label="详细地址">{{ item.address }}</el-descriptions-item>
              <el-descriptions-item label="创建时间">{{ item.timestamp }}</el-descriptions-item>
              <el-descriptions-item v-if="!item.is_change" :colon="false" content-style="margin-top: -25px">
                <el-button @click="navigate({address:item.address,lng:item.lng,lat:item.lat})" type="primary" plain style="margin-left: -5px;padding: 10px">导航</el-button>
                <el-button @click="to_there({address:item.address,lng:item.lng,lat:item.lat})" type="success" plain style="margin-top: 15px;margin-left: 25px;padding: 10px">到这去</el-button>
              </el-descriptions-item>
            </el-descriptions>
            <el-button @click="item.is_change=true" v-if="!item.is_change" type="primary" plain style="margin-left: 25px;padding: 10px">修改</el-button>
            <template v-if="item.is_change">
              <el-button @click="item.is_change=false" type="primary" plain style="margin-left: 25px;padding: 10px">取消</el-button>
              <el-button @click="changeMarker(item)" type="success" plain style="margin-top: 15px;margin-left: 60px;padding: 10px">确定</el-button>
            </template>
          </el-card>
        </template>
      </el-drawer>
    </el-header>
    <div style="position: relative">
      <Show_page ref="show_page"></Show_page>
    </div>
  </el-container>
</div>
</template>

<script>
import LoginView from "@/components/loginView";
import RegisterView from "@/components/registerView";
import Show_page from "@/components/show_page";
import request from "@/utils/request";

export default {
  name: "header_page",
  components: {Show_page, RegisterView, LoginView},
  data() {
    var checkAgainPassword=(rule,value,callback)=>{
      if (!value){
        return callback(new Error("密码输入为空！"))
      }else if (value!==this.newPassword){
        return callback(new Error("密码输入不一致！"))
      }
      callback();
    }

    var checkPassword=(rule,value,callback)=>{
      if (!value){
        return callback(new Error("密码输入为空！"))
      }else if (value!==this.$store.state.user.password){
        return callback(new Error("密码输入错误！"))
      }
      callback();
    }
    var checkNull=(rule,value,callback)=>{
      if (!value){
        return callback(new Error("内容输入为空！"))
      }
      callback();
    }
    return {
      is_show_markerList:false,
      markerList:[],
      radio:1,
      textarea:"",
      visibleEnter:false,
      dialogFormVisible:false,
      loginOrRegister:true,
      icon:'el-icon-user-solid',
      drawer: false,
      direction: 'rtl',
      personTitle:'',
      userInfor:false,
      changeName:false,
      changePassword:false,
      form:{
        newName:"",
        password:"",
        newPassword:"",
        againPassword:"",
      },
      rules: {
        newName:[{validator:checkNull, trigger: 'blur' }],
        password:[{validator:checkPassword, trigger: 'blur' }],
        newPassword:[{validator:checkNull, trigger: 'blur' }],
        againPassword:[{validator:checkAgainPassword, trigger: 'blur' }],
      }
    }
  },
  methods:{
    reset(){
      this.$refs.resetForm.resetForm();
    },
    loginSuccess(){
      this.visibleEnter=true;
      this.dialogFormVisible=false;
      request.get("/api/point/"+this.$store.state.user.phone).then(res=>{
        for (let i = 0; i < res.length; i++) {
          this.$refs.show_page.set_marker(res[i]);
          res[i].timestamp=new Date(res[i].timestamp);
          if (res[i].open===1){
            res[i].open="共有"
          }else {
            res[i].open="私人"
          }
          res[i].is_change=false;
        }
        this.markerList=res
      })
    },
    changeViewOnLoginOrRegister(){
      this.loginOrRegister=!this.loginOrRegister;
    },
    enter(){
      this.dialogFormVisible=true;
    },

    handleCommand(command){
      this.personTitle=command;
      if (command==="个人资料"){
        this.drawer=true;
        this.userInfor=true;
        this.is_show_markerList=false;
      }else if (command==="地点标记"){
        this.drawer=true;
        this.userInfor=false;
        this.is_show_markerList=true;
        request.get("/api/point/"+this.$store.state.user.phone).then(res=>{
          for (let i = 0; i < res.length; i++) {
            res[i].timestamp=new Date(res[i].timestamp);
            if (res[i].open===1){
              res[i].open="共有"
            }else {
              res[i].open="私人"
            }
            res[i].is_change=false;
            this.$refs.show_page.set_marker(res[i]);
          }
          this.markerList=res

        })
      }else if (command==='e'){
        this.drawer=false;
        this.visibleEnter=false;
        this.$refs.resetForm.resetForm();
        this.$store.state.user.phone=null;
      }
    },
    handleClose(done) {//个人资料的关闭
      this.$confirm('确认关闭？')
          .then(()=> {
            done();
          })
          .catch(()=> {});
    },
    //修改个人信息
    changeMess(change){
      if (change==="changeName"){
        this.changeName=true;
      }else if (change==="changePassword"){
        this.changePassword=true;
      }
    },
    changeUsername(){
      request.post("/api/user/changeUsername",{username:this.username,newName:this.newName}).then(res=>{
        if (res.status===202){
          this.username=res.data;
        }
      });
    },
    changeUserPassword(){
      this.$refs.changeMessForm.validate((valid)=>{
        if (valid){

          // request.post("/api/game/changePassword",{newPassword:this.form.againPassword,username:this.username}).then(res=>{
          //   if (res.code===200){
          //     this.userPassword=res.object;
          //   }
          // })
        }else {
          return false;
        }
      })
    },
    // 修改地点标记的数据，提交到后端，保存到数据库
    changeMarker(item){
      let timestamp=new Date();
      let temp={
        lng:item.lng,
        lat:item.lat,
        open:this.radio !==1,
        detail:this.textarea,
        owner:this.$store.state.user.phone,
        address:item.address,
        name:item.addressName,
        timestamp
      }
      request.patch("/api/point/"+this.$store.state.user.phone,temp).then(res=>{
        if (Object.keys(res).length!==0){
          this.$message({
            message:"修改成功",
            type:"success"
          })
        }
      })
    },
    // 和搜索界面的功能相同
    navigate(item){
      this.$refs.show_page.navigation(item);
    },
    to_there(item){
      this.$refs.show_page.to_there_0(item);
    },
  },
}
</script>

<style scoped>
.el-card__body, .el-main {
  padding: 10px;
}
.card-2 ::v-deep .el-card__body{
  padding: 15px;
}
.button{
  font-size: medium;
  font-weight: bolder;
  margin-top: 10px;
  color: white;
  margin-left: 75%;
}
</style>