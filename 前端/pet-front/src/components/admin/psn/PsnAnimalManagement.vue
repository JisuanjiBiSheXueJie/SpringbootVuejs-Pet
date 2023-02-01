<template>
  <div>
    <el-row style="margin: 18px 0px 0px 18px ">
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/admin/dashboard' }">管理中心</el-breadcrumb-item>
        <el-breadcrumb-item>动物管理</el-breadcrumb-item>
        <el-breadcrumb-item>动物信息</el-breadcrumb-item>
      </el-breadcrumb>
    </el-row>
    <edit-form @onSubmit="loadAnimals()" ref="edit"></edit-form>
    <el-card style="margin: 18px 2%;width: 95%">
      <el-table
        :data="animal"
        stripe
        style="width: 100%"
        :max-height="tableHeight">
        <el-table-column
          type="selection"
          width="55">
        </el-table-column>
        <el-table-column type="expand">
          <template slot-scope="props">
            <el-form label-position="left" inline>
              <el-form-item>
                <span>{{ props.row.description }}</span>
              </el-form-item>
            </el-form>
          </template>
        </el-table-column>
        <el-table-column
          prop="name"
          label="动物名称（展开查看详细信息）"
          fit>
        </el-table-column>
        <el-table-column
          prop="status"
          label="状态"
          width="100">
        </el-table-column>
        <el-table-column
          prop="breed"
          label="品种"
          fit>
        </el-table-column>
        <el-table-column
          prop="date"
          label="最近发现日期"
          width="120">
        </el-table-column>
        <el-table-column
          prop="age"
          label="年龄"
          fit>
        </el-table-column>
        <!--<el-table-column-->
          <!--prop="abs"-->
          <!--label="摘要"-->
          <!--show-overflow-tooltip-->
          <!--fit>-->
        <!--</el-table-column>-->
        <el-table-column
          fixed="right"
          label="操作"
          width="120">
          <template slot-scope="scope">
            <el-button
              @click.native.prevent="cancel(scope.row)"
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
    name: 'AnimalManagement',
    data () {
      return {
        animal: []
      }
    },
    mounted () {
      this.loadAnimals()
    },
    computed: {
      tableHeight () {
        return window.innerHeight - 320
      }
    },
    methods: {
      cancel (item) {
        item.adoptname = window.localStorage.getItem('username')
        item.adoptname = item.adoptname.substring(1, item.adoptname.length - 1)
        item.status = '1'
        this.$axios
          .post('/animal', {
            id: item.id,
            name: item.name,
            picture: item.picture,
            status: item.status,
            date: item.date,
            address: item.address,
            age: item.age,
            breed: item.breed,
            adoptname: '',
            description: item.description
          }).then(resp => {
          if (resp && resp.status === 200) {
            this.$alert('取消成功！')
          }
        })
      },
      loadAnimals () {
        let adoptname = window.localStorage.getItem('username')
        adoptname = adoptname.substring(1, adoptname.length - 1)
        var _this = this
        this.$axios.get('/animalbyadoptname?adoptname=' + adoptname).then(resp => {
          if (resp && resp.data.code === 200) {
            _this.animal = resp.data.result
          }
        })
      }
    }
  }
</script>

<style scoped>
</style>
