<template>
  <el-menu
    class="categories"
    default-active="1"
    @select="handleSelect"
    active-text-color="red">
    <el-menu-item index="1">
      <i class="el-icon-star-off"></i>
      <span slot="title">已救助待领养</span>
    </el-menu-item>
    <el-menu-item index="0" v-if="showFlag">
      <i class="el-icon-ship"></i>
      <span slot="title">已发布待救助</span>
    </el-menu-item>
  </el-menu>
</template>

<script>
    export default {
      name: 'SideMenu',
      data () {
        return {
          cid: '',
          showFlag: false
        }
      },
      mounted () {
         this.loadVolunteer()
      },
      methods: {
        handleSelect (key, keyPath) {
          this.cid = key
          this.$emit('indexSelect')
        },
        loadVolunteer () {
          var username = window.localStorage.getItem('username')
          username = username.substring(1, username.length - 1)
          this.$axios.get('/volunteerauti?userName=' + username).then(resp => {
            if (resp && resp.status === 200) {
              this.showFlag = resp.data.result
            }
          })
        }
      }
    }
</script>

<style scoped>
  .categories {
    position: fixed;
    margin-left: 50%;
    left: -600px;
    top: 100px;
    width: 150px;
  }
</style>
