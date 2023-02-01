<template>
  <div style="text-align: left">
    <el-dialog
      title="修改评论信息"
      :visible.sync="dialogFormVisible"
      @close="clear">
      <el-form v-model="form" style="text-align: left" ref="dataForm">
        <el-form-item label="评论内容" :label-width="formLabelWidth" prop="name">
          <el-input v-model="form.contents" autocomplete="off" placeholder="简单易记"></el-input>
        </el-form-item>
        <el-form-item prop="id" style="height: 0">
          <el-input type="hidden" v-model="form.id" autocomplete="off"></el-input>
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
  export default {
    name: 'EditForm',
    data () {
      return {
        dialogFormVisible: false,
        form: {
          id: '',
          contents: '',
          adoptname: '',
          aid: ''
        },
        formLabelWidth: '120px'
      }
    },
    methods: {
      clear () {
        // this.$refs.imgUpload.clear()
        this.form = {
          id: '',
          contents: '',
          adoptname: '',
          aid: ''
        }
      },
      onSubmit () {
        this.$axios
          .post('/comments', {
            id: this.form.id,
            contents: this.form.contents,
            adoptname: this.form.adoptname,
            aid: this.form.aid
          }).then(resp => {
            if (resp && resp.data.code === 200) {
              this.dialogFormVisible = false
              this.$alert('修改成功！')
              this.$emit('onSubmit')
            }
        })
      }
    }
  }
</script>

<style scoped>
  .add-button {
    margin: 18px 0 0 10px;
  }
</style>
