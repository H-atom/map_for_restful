<template>
<div>
    <el-form :model="ruleForm"
             status-icon
             :rules="rules"
             ref="ruleForm"
             label-width="100px"
             class="demo-ruleForm"
             @keyup.enter.native="submitForm(ruleForm)"
    @close="resetForm">
      <el-form-item label="电话" prop="username" class="el-form-item">
        <el-input type="text" v-model="ruleForm.phone" autocomplete="off" clearable class="el-input"></el-input>
      </el-form-item>
      <el-form-item label="密码" prop="password" class="el-form-item">
        <el-input type="password" v-model="ruleForm.password" autocomplete="off" clearable ></el-input>
      </el-form-item>
      <el-form-item class="el-form-item">
        <el-button type="text">忘记密码</el-button>
        <el-button type="text" style="margin-left: 60px">手机验证码登录</el-button>
      </el-form-item>
      <el-form-item class="el-form-item">
        <el-button type="primary" @click="submitForm(ruleForm)">确定</el-button>
        <el-button style="margin-left: 50px" @click="toRegisterView">注册</el-button>
      </el-form-item>
    </el-form>
</div>
</template>

<script>
import request from "@/utils/request";


export default {
  name: "loginView",

  data() {
    let phone = /^1[2,3,4,5,7,8,9][0-9]{9}$/
    var checkPhone = (rule, value, callback) => {
      if (value==='') {
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
    var validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'));
      } else {
        if (this.ruleForm.password !== '') {
          this.$refs.ruleForm.validateField('checkPass');
        }
        callback();
      }
    };
    return {
      ruleForm: {
        phone:'',
        password:'',
      },
      rules: {
        phone: [
          { validator: checkPhone, trigger: 'blur'}
        ],
        password: [
          { validator: validatePass, trigger: 'blur' }
        ]
      }
    };
  },
//props:['vis','dia'],
  methods: {
    submitForm(formName) {
      this.$refs.ruleForm.validate((valid) => {
        if (valid) {
          request.post("/api/user/login", formName).then(res=>{
            this.$store.commit("login",res)
            this.$emit("login");
            this.$message({
              message:"登录成功",
              type:"success",
              showClose:true
            });
          // }else if (res.status===400){
          //   this.$message({
          //     message:res.statusText,
          //     type:"warning",
          //     showClose:true
          //   });
          // }
        })
        } else {
          return false;
        }
      });
    },
    resetForm(){
      this.$refs.ruleForm.resetFields();
    },
    toRegisterView(){
      this.$emit("changeRegisterView")
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