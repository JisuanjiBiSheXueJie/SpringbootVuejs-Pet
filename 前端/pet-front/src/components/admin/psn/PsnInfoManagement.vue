<template>
  <div>
    <el-dialog title="收货地址" :visible.sync="dialogFormVisible">
      <el-form :model="form">
        <el-form-item label="姓名" :label-width="formLabelWidth">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="手机号" :label-width="formLabelWidth">
          <el-input v-model="form.phone" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" :label-width="formLabelWidth">
          <el-input v-model="form.email" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="onSubmit">确 定</el-button>
      </div>
    </el-dialog>
    <el-button @click="editPsnInfo">修改个人信息</el-button>
    <el-descriptions title="用户信息">
      <el-descriptions-item label="用户名">{{psnInfo.username}}</el-descriptions-item>
      <el-descriptions-item label="手机号">{{ psnInfo.phone }}</el-descriptions-item>
      <el-descriptions-item label="姓名">{{ psnInfo.name }}</el-descriptions-item>
      <el-descriptions-item label="邮箱">
        <el-tag size="small">{{ psnInfo.email }}</el-tag>
      </el-descriptions-item>
    </el-descriptions>
  </div>
</template>

<script>
export default {
  name: 'EditForm',
  data () {
    return {
      psnInfo: {},
      formLabelWidth: '120px',
      dialogFormVisible: false,
      form: {
        id: '',
        username: '',
        phone: '',
        email: '',
        name: ''
      }
    }
  },
  created () {
    this.loadPsnInfo()
  },
  methods: {
    loadPsnInfo () {
      var username = window.localStorage.getItem('username')
      username = username.substring(1, username.length - 1)
      this.$axios.get('/userbyname?userName=' + username).then(res => {
          this.psnInfo = res.data.result
      })
    },
    clear () {
      // this.$refs.imgUpload.clear()
      this.form = {
        id: '',
        username: '',
        phone: '',
        email: '',
        name: ''
      }
    },
    onSubmit () {
      this.$axios
        .put('/admin/usera', {
          id: this.form.id,
          phone: this.form.phone,
          email: this.form.email,
          name: this.form.name,
          username: this.form.username
        }).then(resp => {
        if (resp && resp.data.code === 200) {
          this.dialogFormVisible = false
          this.$alert('修改成功！')
          this.loadPsnInfo()
        }
      })
    },
    editPsnInfo () {
      this.dialogFormVisible = true
      this.form.phone = this.psnInfo.phone
      this.form.name = this.psnInfo.name
      this.form.id = this.psnInfo.id
      this.form.email = this.psnInfo.email
      this.form.username = this.psnInfo.username
    }
  }
}
</script>

<style scoped>

</style>
