<template>
  <div>
    <el-row style="margin-top: 20px">
      <div style="color: #36a3f7;font-size: 20px;font-family: '微软雅黑 Light'">团队信息</div>
    </el-row>
    <el-row><el-col :offset="6"><el-button @click="open" v-if="buttonFlag">加入我们(申请志愿者)</el-button></el-col></el-row>
    <el-row style="margin-top: 10px">
      <el-col :offset="5" >
      <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span style="float: left">团队介绍</span>
          </div>
          <div class="text item">
            {{teamList[0].content}}
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-row style="margin-top: 10px">
      <el-col :offset="5">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span style="float: left">团队成员</span>
          </div>
          <div v-for="o in volunteerList" :key="o.id" class="text item">
              <div>
                <el-row>
                  <img class="img" :src="require('../../assets/user.png')" />
                </el-row>
                <span>{{ o.name }}</span>
              </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: 'TeamInfo',
  data () {
    return {
      teamList: [],
      buttonFlag: true,
      volunteerList: []
    }
  },
  mounted: function () {
    this.loadTeam()
    this.loadVolunteer()
    this.listVolunteer()
  },
  methods: {
    loadTeam () {
      var _this = this
      this.$axios.get('/team').then(resp => {
        if (resp && resp.status === 200) {
          console.log(resp)
          _this.teamList = resp.data.result
        }
      })
    },
    loadVolunteer () {
      var username = window.localStorage.getItem('username')
      username = username.substring(1, username.length - 1)
      this.$axios.get('/volunteerauti?userName=' + username).then(resp => {
        if (resp && resp.status === 200) {
          this.buttonFlag = !resp.data.result
        }
      })
    },
    listVolunteer () {
      this.$axios.get('/userbyrole').then(resp => {
        if (resp && resp.status === 200) {
          this.volunteerList = resp.data.result
        }
      })
    },
    open () {
      this.$confirm('此操作将向团队管理员发送志愿者申请, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        var username = window.localStorage.getItem('username')
        username = username.substring(1, username.length - 1)
        this.$axios.get('/userbyname?userName=' + username).then(res => {
          this.$axios
            .post('/volunteer', {
              'uid': res.data.result.id,
              'status': '0'
            }).then(resp => {
            if (resp && resp.status === 200) {
              this.$alert('申请成功！')
            }
          })
        })
      }).catch(() => {
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
.divinfo {
 height: 60%;
  width: 50%;
  float:left;
  border-width: 100px;
  margin-top: 100px;
}
.text {
  font-size: 14px;
}

.item {
  margin-bottom: 18px;
  margin-left: 80px;
  float: left;
}

.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}
.clearfix:after {
  clear: both
}

.box-card {
  width: 800px;
  text-align: center;
}
.img {
  width: 100px;
}
</style>
