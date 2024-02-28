<template>
  <div>
    <el-form :model="ruleForm"
             status-icon
             :rules="rules"
             ref="ruleForm"
             label-width="100px"
             class="demo-ruleForm"
             @keyup.enter.native="submitForm(ruleForm)">
      <el-form-item label="电话" prop="phone" class="el-form-item">
        <el-input type="text" v-model="ruleForm.phone" autocomplete="off" clearable class="el-input"></el-input>
      </el-form-item>
      <el-form-item label="昵称" prop="username" class="el-form-item">
        <el-input type="text" v-model="ruleForm.username" autocomplete="off" clearable class="el-input"></el-input>
      </el-form-item>
      <el-form-item label="密码" prop="password" class="el-form-item">
        <el-input type="password" v-model="ruleForm.password" autocomplete="off" clearable ></el-input>
      </el-form-item>
      <el-form-item label="确认密码" prop="againPassword" class="el-form-item">
        <el-input type="password" v-model="ruleForm.againPassword" autocomplete="off" clearable ></el-input>
      </el-form-item>
      <el-form-item class="el-form-item" style="margin-top: 20px">
        <el-button @click="backLoginView">返回</el-button>
        <el-button type="primary" @click="submitForm(ruleForm)" style="margin-left: 50px">确定</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "registerView",
  data() {
    let phone = /^1[2,3,4,5,7,8,9][0-9]{9}$/
    var checkNumber = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('电话不能为空'));
      }
      setTimeout(() => {
        if (!phone.test(value)||value.length!==11){
          callback(new Error("电话号码格式错误"))
        }else {
          callback();
        }
      }, 1000);
    };
    let checkedName=(rule,value,callback)=>{
      if (!value){
        callback(new Error("请输入昵称"))
      }
      callback();
    }
    var validatePass = (rule, value, callback) => {
      if (!value) {
        callback(new Error('请输入密码'));
      } else {
        this.$refs.ruleForm.validateField('checkPass');
      }
      callback();
    };
    let checkedPassword=(rule,value,callback)=>{
      if (!value){
        callback(new Error("请确认密码"))
      }else {
        if (value!==this.ruleForm.password){
          callback(new Error("密码输入不一致"))
        }
      }
      callback();
    }
    return {
      ruleForm: {
        phone:'',
        username:'',
        password:'',
        againPassword:"",
      },
      rules: {
        phone: [
          { validator: checkNumber, trigger: 'blur' }
        ],
        username: [
          {validator: checkedName, trigger:'blur'}
        ],
        password: [
          { validator: validatePass, trigger: 'blur' }
        ],
        againPassword: [
          {validator: checkedPassword, trigger: 'blur'}
        ]
      }
    };
  },
//props:['vis','dia'],
  methods: {
    submitForm(formName) {
      this.$refs.ruleForm.validate((valid) => {
        if (valid) {
          request.post("/api/user", formName).then(res=>{
            if (res.status===400){
              this.$message({
                showClose:true,
                message:"用户已存在，请重新注册",
                type:'error'
              })
            }else if (res.status===200){
              this.$message({
                showClose:true,
                message:"注册成功，请登录",
                type:'success'
              });
              setTimeout(()=>{
                this.$emit("changeLoginView")
                this.resetForm();
              },500)
            }
          })
        } else {
          return false;
        }
      });
    },
    resetForm(){
      this.$refs.ruleForm.resetFields();
    },
    backLoginView(){
      this.resetForm();
      this.$emit("changeLoginView")
    }
  }
}
</script>

<style scoped>
.demo-ruleForm{
  width: 100%;
}
.el-input{
  width: 80%;
}
.el-form-item{
  margin-top: -12px;
}
/deep/.el-input__inner{
  height: 31px;
  line-height: 31px;
}
</style>