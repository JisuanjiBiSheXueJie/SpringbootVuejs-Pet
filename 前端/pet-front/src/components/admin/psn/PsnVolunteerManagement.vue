<template>
  <div>
    <el-row style="margin: 18px 0px 0px 18px ">
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/admin/dashboard' }">管理中心</el-breadcrumb-item>
        <el-breadcrumb-item>志愿者管理</el-breadcrumb-item>
        <el-breadcrumb-item>志愿者申请审核</el-breadcrumb-item>
      </el-breadcrumb>
    </el-row>
    <el-card style="margin: 18px 2%;width: 95%">
      <el-table
        :data="volunteer"
        stripe
        style="width: 100%"
        :max-height="tableHeight">
        <el-table-column
          prop="id"
          label="申请ID"
          fit>
        </el-table-column>
        <el-table-column
          prop="statusName"
          label="状态"
          width="100">
        </el-table-column>
        <el-table-column
          prop="uid"
          label="申请者用户id"
          width="120">
        </el-table-column>
        <el-table-column
          prop="userName"
          label="申请者姓名"
          fit>
        </el-table-column>
        <el-table-column
          fixed="right"
          label="操作"
          width="120">
          <template slot-scope="scope">
            <el-button
              @click.native.prevent="deletevolunteer(scope.row)"
              type="text"
              size="small">
              取消申请
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'PsnVolunteerManagement',
  data () {
    return {
      volunteer: []
    }
  },
  mounted () {
    this.loadVolunteers()
  },
  computed: {
    tableHeight () {
      return window.innerHeight - 320
    }
  },
  methods: {
    loadVolunteers () {
      var username = window.localStorage.getItem('username')
      username = username.substring(1, username.length - 1)
      this.$axios.get('/userbyname?userName=' + username).then(res => {
        var _this = this
        this.$axios.get('/volunteerbyuid?uid=' + res.data.result.id).then(resp => {
          if (resp && resp.data.code === 200) {
            _this.volunteer = resp.data.result
          }
        })
      })
    },
    deletevolunteer (row) {
      this.$confirm('此操作将取消申请, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
          this.$axios
            .get('/volunteer/delete?id=' + row.id).then(resp => {
            if (resp && resp.data.code === 200) {
              this.loadVolunteers()
            }
          })
        }
      ).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    }
  }
}
</script>

<style scoped>
</style>
