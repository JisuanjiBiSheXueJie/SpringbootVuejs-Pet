<template>
  <div>
    <el-row>
      <el-col :span="3">
        <div>
          <img style="width: 40px;height: 40px" src="../../assets/img/logo.jpeg">
        </div>
        <div>
          流浪动物救助站
        </div>
      </el-col>
      <el-col :span="18">
        <el-menu
          :default-active="currentPath"
          router
          mode="horizontal"
          background-color="#545c64"
          text-color="#fff"
          active-text-color="#ffd04b"
          style="width: 100%">
          <el-menu-item v-for="(item,i) in navList" :key="i" :index="item.name">
            {{ item.navItem }}
          </el-menu-item>
        </el-menu>
      </el-col>
    </el-row>

  </div>
</template>

<script>
  export default {
    name: 'NavMenu',
    data () {
      return {
        navList: [
          {name: '/jotter', navItem: '流浪动物知识'},
          {name: '/library', navItem: '动物信息'},
          {name: '/login', navItem: '个人中心'},
          {name: '/teaminfo', navItem: '团队信息'}
        ],
        keywords: '',
        size: 'large',
        circleUrl: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png',
        username: ''
      }
    },
    computed: {
      hoverBackground () {
        return '#ffd04b'
      },
      currentPath () {
        var x = this.$route.path.indexOf('/', 1)
        console.log('username')
        console.log(window.localStorage.getItem('username'))
        // eslint-disable-next-line vue/no-side-effects-in-computed-properties
        this.username = window.localStorage.getItem('username')
        if (x !== -1) {
          return this.$route.path.substring(0, x)
        } else {
          return this.$route.path
        }
      }
    }
  }
</script>

<style scoped>
  a{
    text-decoration: none;
  }

  span {
    pointer-events: none;
  }
  .username {
    color: white;
    font-size: 15px;
    text-align: center;
    background-color: #545c64;
  }
</style>
