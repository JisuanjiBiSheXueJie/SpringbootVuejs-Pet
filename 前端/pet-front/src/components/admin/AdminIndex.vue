<template>
  <el-container id="admin-body">
    <el-header style="z-index: 1;height: 60px;">
      <nav-menu class="nav-menu"></nav-menu>
      <Header style="float:right; width: 40px;"></Header>
    </el-header>
    <!--<el-row style="height: 100%;">-->
    <el-container>
      <el-aside style="height: 112%;margin-top: -80px;width: 200px">
        <admin-menu></admin-menu>
      </el-aside>
      <el-main>
        <router-view/>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
  import AdminMenu from './AdminMenu'
  import Header from './Header'
  import NavMenu from '../../components/common/NavMenu'

  export default {
    name: 'AdminIndex',
    components: {AdminMenu, Header, NavMenu},
    data () {
      return {
        dialogVisible: false
      }
    },
    mounted () {
      // this.$router.replace('/admin/dashboard')
    }
  }
</script>

<style scoped>
  #admin-body {
  height: 95vh;
}
</style>
