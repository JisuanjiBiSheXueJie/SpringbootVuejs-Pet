<template>
<div>
  <el-dialog title="忘记密码" :visible.sync="dialogFormVisible">
    <el-form :model="cfrom">
      <el-form-item>
        <el-input type="text" v-model="cfrom.phone"
                  auto-complete="off" placeholder="电话号码">
          <el-button slot="suffix" @click="sendMsg" type="primary" :disabled="isDisabled">{{buttonName}}</el-button>
        </el-input>
      </el-form-item>
      <el-form-item>
        <el-input type="text" v-model="cfrom.code"
                  auto-complete="off" placeholder="验证码"></el-input>
      </el-form-item>
      <el-form-item label="新密码">
        <el-input v-model="cfrom.newpwd" autocomplete="off"></el-input>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogFormVisible = false">取 消</el-button>
      <el-button type="primary" @click="updatePwd">确 定</el-button>
    </div>
  </el-dialog>
  <body id="paper">
      <el-form :model="loginForm" :rules="rules" class="login-container" label-position="left"
             label-width="0px" v-loading="loading">
      <h3 class="login_title">流浪动物救助站登录</h3>
      <el-form-item prop="username">
        <el-input type="text" v-model="loginForm.username"
                  auto-complete="off" placeholder="账号"></el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input type="password" v-model="loginForm.password"
                  auto-complete="off" placeholder="密码"></el-input>
      </el-form-item>
      <el-form-item prop="code">
        <el-input type="code" v-model="loginForm.code"
                  auto-complete="off" placeholder="验证码">
          <img style="width: 120px; height: 40px" slot="suffix" @click="getCode" :src="imgSrc" alt="" />
        </el-input>
      </el-form-item>
      <el-button type="text" @click="openWanJi">忘记密码</el-button>
      <el-checkbox class="login_remember" v-model="checked"
                   label-position="left" style="margin-left: 10px"><span style="color: #505458">记住密码</span></el-checkbox>
      <el-form-item style="width: 100%">
        <el-button type="primary" style="width: 40%;background: #505458;border: none" v-on:click="login">登录</el-button>
        <router-link to="register"><el-button type="primary" style="width: 40%;background: #505458;border: none">注册</el-button></router-link>
      </el-form-item>
    </el-form>
  </body>
</div>
</template>
<script>
  export default{
    data () {
      return {
        rules: {
          username: [{required: true, message: '用户名不能为空', trigger: 'blur'}],
          password: [{required: true, message: '密码不能为空', trigger: 'blur'}]
        },
        checked: true,
        loginForm: {
          username: '',
          password: '',
          code: ''
        },
        cfrom: {
          phone: '',
          code: '',
          newpwd: ''
        },
        loading: false,
        imgSrc: '',
        code: '',
        code1: '',
        dialogFormVisible: false,
        buttonName: '发送验证码',
        time: 60,
        isDisabled: false
      }
    },
    mounted () {
      this.getCode()
    },
    methods: {
        login () {
          if (this.loginForm.code !== this.code) {
            this.$alert('验证码不正确！')
            this.getCode()
            return
          }
          var _this = this
          this.$axios
            .post('/login', {
              username: this.loginForm.username,
              password: this.loginForm.password
            })
            .then(resp => {
              if (resp.data.code === 200) {
                var data = resp.data.result
                _this.$store.commit('login', data)
                var path = _this.$route.query.redirect
                _this.$router.replace({path: path === '/' || path === undefined ? '/admin/dashboard' : path})
                _this.$router.replace('/login')
              } else {
                this.$alert(resp.data.message, '提示', {
                  confirmButtonText: '确定'
                })
              }
            })
            .catch(failResponse => {
            })
        },
        getCode () {
          this.$axios
            .get('/getCode')
            .then(resp => {
              console.log(resp)
              this.imgSrc = 'data:image/jpg;base64,' + resp.data.img
              this.code = resp.data.code
            })
        },
        sendMsg () {
          let me = this
          me.isDisabled = true
          this.$axios
            .get('/sendMsg?phone=' + this.cfrom.phone)
            .then(resp => {
              me.code1 = resp.data.result
            })
          let interval = window.setInterval(function () {
            me.buttonName = '（' + me.time + '秒）后重新发送'
            me.time--
            if (me.time < 0) {
              me.buttonName = '重新发送'
              me.time = 60
              me.isDisabled = false
              window.clearInterval(interval)
            }
          }, 1000)
        },
        updatePwd () {
          if (this.cfrom.code !== this.code1) {
            this.$alert('验证码不正确！')
            return
          }
          this.$axios.put('/admin/updatepassword', {
            phone: this.cfrom.phone,
            password: this.cfrom.newpwd
          }).then(resp => {
            if (resp && resp.data.code === 200) {
              this.dialogFormVisible = false
              this.$alert('密码已修改！')
            }
          })
        },
        openWanJi () {
          this.dialogFormVisible = true
        }
      }
    }
</script>
<style>
   #paper {
     background: url("../assets/cover.jpg") no-repeat center;
     height: 100%;
    width: 100%;
    background-size: cover;
    position: fixed;
  }
   body{
     margin: 0;
   }
  .login-container {
    border-radius: 15px;
    background-clip: padding-box;
    margin: 90px auto;
    width: 350px;
    padding: 35px 35px 15px 35px;
    background: #fff;
    border: 1px solid #eaeaea;
    box-shadow: 0 0 25px #cac6c6;
  }
  .login_title {
    margin: 0px auto 40px auto;
    text-align: center;
    color: #505458;
  }
  .login_remember {
    margin: 0px 0px 35px 0px;
    text-align: left;
  }
</style>
