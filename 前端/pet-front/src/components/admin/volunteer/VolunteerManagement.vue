<template>
  <div>
    <el-row style="margin: 18px 0px 0px 18px ">
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/admin/dashboard' }">管理中心</el-breadcrumb-item>
        <el-breadcrumb-item>志愿者管理</el-breadcrumb-item>
        <el-breadcrumb-item>志愿者申请审核</el-breadcrumb-item>
      </el-breadcrumb>
    </el-row>
    <edit-form @onSubmit="loadVolunteers()" ref="edit"></edit-form>
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
              @click.native.prevent="through(scope.row)"
              type="text"
              size="small">
              通过
            </el-button>
            <el-button
              @click.native.prevent="nothrough(scope.row)"
              type="text"
              size="small">
              未通过
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'VolunteerManagement',
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
    through (item) {
        item.status = '1'
        this.$axios
          .post('/volunteer', {
            id: item.id,
            status: item.status,
            uid: item.uid
          }).then(resp => {
          if (resp && resp.status === 200) {
            this.loadVolunteers()
            this.$alert('审核成功！')
          }
        })
    },
    nothrough (item) {
      item.status = '2'
      this.$axios
        .post('/volunteer', {
          id: item.id,
          status: item.status,
          uid: item.uid
        }).then(resp => {
        if (resp && resp.status === 200) {
          this.loadVolunteers()
          this.$alert('审核成功！')
        }
      })
    },
    loadVolunteers () {
      var _this = this
      this.$axios.get('/volunteer').then(resp => {
        if (resp && resp.data.code === 200) {
          _this.volunteer = resp.data.result
        }
      })
    }
  }
}
</script>

<style scoped>
</style>
