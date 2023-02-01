<template>
  <div>
    <el-dialog
      title="发布动物信息"
      :visible.sync="dialogFormVisible"
      @close="clear">
      <el-form v-model="form" style="text-align: left" ref="dataForm">
<!--        zhaoruyan-->
<!--        <el-form-item label="图片" :label-width="formLabelWidth" prop="picture">-->
<!--          <el-input v-model="form.picture" autocomplete="off" placeholder="图片 URL"></el-input>-->
<!--          <img-upload @onUpload="uploadImg" ref="imgUpload"></img-upload>-->
<!--        </el-form-item>-->
        <el-form-item label="发现地址" :label-width="formLabelWidth" prop="description">
          <amap :default-value="postForm.detailedAddress" @getPosition="getPosition" />
        </el-form-item>
        <el-form-item label="发现时间" :label-width="formLabelWidth">
          <el-input type="date" v-model="form.date" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="简介" :label-width="formLabelWidth" prop="description">
          <el-input type="textarea" v-model="form.description" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="onSubmit">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import ImgUpload from './ImgUpload'
import Amap from '../Amap'

export default {
  name: 'EditForm',

  components: {ImgUpload, Amap},
  data () {
    return {
      dialogFormVisible: false,
      form: {
        // picture: '', <!--        zhaoruyan-->
        description: '',
        address: '',
        date: '',
        name: '默认昵称',
        status: '0'
      },
      formLabelWidth: '120px',
      postForm: {
        detailedAddress: '',
        longitude: '',
        latitude: ''
      }
    }
  },
  methods: {
    clear () {
      this.form = {
        // picture: '',<!--        zhaoruyan-->
        description: '',
        address: '',
        date: ''
      }
    },
    // 获取地址信息
    getPosition ({ address, lat, lng, name }) {
      this.postForm.detailedAddress = address
      this.postForm.longitude = String(lng)
      this.postForm.latitude = String(lat)
    },
    onSubmit () {
      var username = window.localStorage.getItem('username')
      username = username.substring(1, username.length - 1)
      this.$axios
        .post('/animal', {
          name: '默认昵称',
          status: '0',
          date: this.form.date,
          address: this.postForm.detailedAddress,
          description: this.form.description,
          adoptname: username
        }).then(resp => {
          if (resp && resp.status === 200) {
            this.$alert('发布成功！')
            this.dialogFormVisible = false
            this.$emit('onSubmit')
          }
        })
    },
    uploadImg () {
      this.form.picture = this.$refs.imgUpload.url
    },
    animalsMap () {
      this.$refs.amap.visible = true
    }
  }
}
</script>

<style scoped>
.el-icon-circle-plus-outline {
  margin: 50px 0 0 20px;
  font-size: 100px;
  float: left;
  cursor: pointer;
}
</style>
