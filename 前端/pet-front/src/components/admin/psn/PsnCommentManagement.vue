<template>
  <div>
    <el-row style="margin: 18px 0px 0px 18px ">
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/admin/dashboard' }">管理中心</el-breadcrumb-item>
        <el-breadcrumb-item>内容管理</el-breadcrumb-item>
        <el-breadcrumb-item>评论管理</el-breadcrumb-item>
      </el-breadcrumb>
    </el-row>
    <edit-form @onSubmit="loadComment()" ref="edit"></edit-form>
    <el-card style="margin: 18px 2%;width: 95%">
      <el-table
        :data="comment"
        stripe
        style="width: 100%"
        :max-height="tableHeight">
        <el-table-column
          prop="id"
          label="评论ID">
        </el-table-column>
        <el-table-column
          prop="contents"
          label="评论内容"
          fit>
        </el-table-column>
        <el-table-column
          prop="adoptname"
          label="评论者用户名"
          width="100">
        </el-table-column>
        <el-table-column
          prop="aid"
          label="动物Id">
        </el-table-column>
        <el-table-column
          prop="animalName"
          label="动物名称">
        </el-table-column>
        <el-table-column
          prop="upttime"
          label="评论/更新时间"
          fit>
        </el-table-column>
        <el-table-column
          fixed="right"
          label="操作"
          width="120">
          <template slot-scope="scope">
            <el-button
              @click.native.prevent="editComment(scope.row)"
              type="text"
              size="small">
              编辑
            </el-button>
            <el-button
              @click.native.prevent="deleteComment(scope.row.id)"
              type="text"
              size="small">
              移除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
import EditForm from './CommentEditForm'
export default {
  name: 'PsnCommentManagement',
  components: {EditForm},
  data () {
    return {
      comment: []
    }
  },
  mounted () {
    this.loadComment()
  },
  computed: {
    tableHeight () {
      return window.innerHeight - 320
    }
  },
  methods: {
    deleteComment (id) {
      this.$confirm('此操作将永久删除该评论信息, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
          this.$axios
            .get('/deletecomments?id=' + id).then(resp => {
            if (resp && resp.data.code === 200) {
              this.loadComment()
            }
          })
        }
      ).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    editComment (item) {
      this.$refs.edit.dialogFormVisible = true
      this.$refs.edit.form = {
        id: item.id,
        contents: item.contents,
        adoptname: item.adoptname,
        aid: item.aid
      }
      // this.$refs.edit.category = {
      //   id: item.category.id.toString()
      // }
    },
    loadComment () {
      var _this = this
      var username = window.localStorage.getItem('username')
      username = username.substring(1, username.length - 1)
      this.$axios.get('/commentsbyname?adoptname=' + username).then(resp => {
        if (resp && resp.data.code === 200) {
          _this.comment = resp.data.result
        }
      })
    }
  }
}
</script>

<style scoped>
</style>
