<template>
  <div>
    <el-row style="margin: 18px 0px 0px 18px ">
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/admin/dashboard' }">管理中心</el-breadcrumb-item>
        <el-breadcrumb-item>团队管理</el-breadcrumb-item>
        <el-breadcrumb-item>团队信息管理</el-breadcrumb-item>
      </el-breadcrumb>
    </el-row>
    <edit-form @onSubmit="loadTeam()" ref="edit"></edit-form>
    <el-card style="margin: 18px 2%;width: 95%">
      <el-table
        :data="team"
        stripe
        style="width: 100%"
        :max-height="tableHeight">
        <el-table-column
          prop="id"
          label="团队ID"
          width="100">
        </el-table-column>
        <el-table-column
          prop="content"
          label="团队信息内容"
          fit>
        </el-table-column>
        <el-table-column
          fixed="right"
          label="操作"
          width="120">
          <template slot-scope="scope">
            <el-button
              @click.native.prevent="editTeam(scope.row)"
              type="text"
              size="small">
              编辑
            </el-button>
            <el-button
              @click.native.prevent="deleteTeam(scope.row.id)"
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
import EditForm from './EditForm'
export default {
  name: 'TeamManagement',
  components: {EditForm},
  data () {
    return {
      team: []
    }
  },
  mounted () {
    this.loadTeam()
  },
  computed: {
    tableHeight () {
      return window.innerHeight - 320
    }
  },
  methods: {
    deleteTeam (id) {
      this.$confirm('此操作将永久删除该动物信息, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
          this.$axios
            .get('/team/delete?id=' + id).then(resp => {
            if (resp && resp.data.code === 200) {
              this.$alert('删除成功！')
              this.loadTeam()
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
    editTeam (item) {
      this.$refs.edit.dialogFormVisible = true
      this.$refs.edit.form = {
        id: item.id,
        content: item.content
      }
    },
    loadTeam () {
      var _this = this
      this.$axios.get('/team').then(resp => {
        if (resp && resp.data.code === 200) {
          _this.team = resp.data.result
        }
      })
    }
  }
}
</script>

<style scoped>
</style>
