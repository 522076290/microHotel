<template>
  <div class="header">
    <div class="header-content">
      <!-- 顶部信息 -->
      <div class="container">
        <div class="links links-left">
          <ul class="left-menu">
            <li class="left-menu-item">
              <a><i class="el-icon-message"></i> admin@liuyanzhao.com</a>
            </li>
            <li class="left-menu-item">
              <a><i class="el-icon-phone"></i> 21-2327-2888</a>
            </li>
            <li class="left-menu-item">
              <a><i class="el-icon-location"></i> 中国上海市中山东一路32号 邮编：200002</a>
            </li>
          </ul>
        </div>
        <div class="links links-right pull-right" v-if="!token">
          <ul class="right-menu" v-if="!token">
            <li class="right-menu-item" @click="toLogin">
              <a> <i class="el-icon-user"></i> 登录</a>
            </li>
            <li class="right-menu-item" @click="toregister">
              <a><i class="el-icon-caret-right"></i> 注册</a>
            </li>
          </ul>
        </div>
        <div class="links links-right pull-right" v-if="token">
          <ul class="right-menu" >
            <li class="right-menu-item" @click="tobakestage">
              <a> <i class="el-icon-user"></i>进入后台</a>
            </li>
            <li class="right-menu-item" @click="logout">
              <a><i class="el-icon-caret-right" ></i> 退出</a>
            </li>
          </ul>
        </div>
      </div>
    </div>
    <!-- 导航栏 -->
    <div class="navbar">
      <div class="content">
        <div class="navbar-header">
          <a href="/">
            <img src="@/assets/images/logo.png" alt="Logo">
          </a>
        </div>
        <div class="navbar-menu">
          <el-menu class="el-menu-demo" mode="horizontal" active-text-color="#fe5459">
            <el-menu-item index="-1" @click="$router.push('/index')">首页</el-menu-item>
            <el-menu-item v-for="dict in dict.type.hotel_room_type" :key="dict.value" :index="dict.value"
              @click="navigateTo(dict.value)">
              {{ dict.label }}
            </el-menu-item>
          </el-menu>
        </div>
      </div>

    </div>
  </div>

</template>


<script>
import { mapGetters,mapState } from 'vuex'
import TopNav from '@/components/TopNav'
import Screenfull from '@/components/Screenfull'
import SizeSelect from '@/components/SizeSelect'
import Search from '@/components/HeaderSearch'
import RuoYiGit from '@/components/RuoYi/Git'
import RuoYiDoc from '@/components/RuoYi/Doc'

export default {
  components: {
    TopNav,
    Screenfull,
    SizeSelect,
    Search,
    RuoYiGit,
    RuoYiDoc
  },
  dicts: ['hotel_room_type'],
  computed: {
    ...mapGetters([
      'sidebar',
      'avatar',
      'device'
    ]),
    ...mapState({
      token: state => state.user.token
    }),
    setting: {
      get() {
        return this.$store.state.settings.showSettings
      },
      set(val) {
        this.$store.dispatch('settings/changeSetting', {
          key: 'showSettings',
          value: val
        })
      }
    },
    topNav: {
      get() {
        return this.$store.state.settings.topNav
      }
    }
  },
  methods: {
    toggleSideBar() {
      this.$store.dispatch('app/toggleSideBar')
    },
    toregister() {
      this.$router.push('/register')
    },
    toLogin() {
      this.$router.push('/login')
    },
    tobakestage(){
      // 打开一个新的页签 进入管理后台
      window.open(process.env.VUE_APP_BACK_STAGE_URL);
    },
    handleSelect(index) {
      console.log('Selected menu item index:', index);
    },
    navigateTo(value) {
      // 假设路由基地址为 /room-type
      const route = `/room-type/${value}`;
      this.$router.push(route); // 跳转到对应路由
    },
    async logout() {
      this.$confirm('确定注销并退出系统吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$store.dispatch('LogOut').then(() => {
          location.href = '/index';
        })
      }).catch(() => { });
    }
  }
}
</script>

<style lang="scss" scoped>
.header {
  width: 100%;
  position: absolute;
  z-index: 10;

  .header-content {
    border-bottom: 1px solid hsla(0, 0%, 100%, .2);
    height: 40px;
    /* 顶部信息栏高度 */
    background: rgba(0, 0, 0, 0);
    /* 背景为半透明黑色 */
    color: #fff;
    /* 文字颜色为白色 */
    display: flex;
    justify-content: space-between;
    /* 左右布局 */
    align-items: center;
    /* 垂直居中 */
    padding: 0 20px;
    /* 左右内边距 */

    .links {
      display: inline-block;
    }

    

    /* 顶部信息区域 */
    .container {
      //居中
      margin: 0 auto;
      width: 970px;
      height: 40px;
      ul {
        display: flex;
        align-items: center;
        padding: 0;
        margin: 0;
        list-style: none;
        /* 去掉列表样式 */
      }
      .pull-right {
        margin-left: auto; /* 将右侧内容推到右边 */
        display: flex;
        align-items: center;
        }
      .left-menu,
      .right-menu {
        height: 40px;
      }

      .left-menu-item,
      .right-menu-item {
        margin-right: 20px;
        height: 100%;
        display: inline-block;
        text-align: center;
        /* 每个项之间的间距 */
        font-size: 16px;
        /* 字体大小 */
        color: #fff;
        border-left: 1px solid hsla(0, 0%, 100%, .2);

        a{
          color: #fff;
          text-decoration: none;
          margin-left: 10px;
          line-height: 40px;
        }
      }

      .left-menu-item:hover,
      .right-menu-item:hover {
        color: #fe5459;
        /* 鼠标悬停时字体变蓝色 */
      }
    }
  }

  /* 导航栏区域 */
  .navbar {
    //居中
    margin: 0 auto;
    width: 100%;
    height: 80px;
    /* 导航栏高度 */
    background: rgba(0, 0, 0, 0);
    /* 背景为半透明黑色 */
    display: flex;
    align-items: center;
    /* 垂直居中 */
    justify-content: center;
    /* 内容居中 */
    padding: 0 20px;

    /* 内边距 */
    .content {
      width: 970px;
    }

    .navbar-header {
      float: left;
    }
    .navbar-menu {
      float: right;
    }

    .el-menu {
      flex: 1;
      /* 占据剩余空间 */
      display: flex;
      justify-content: center;
      /* 菜单项居中 */
      background: transparent;
      /* 菜单背景透明 */
      border-bottom: none;
      /* 去掉底部边框 */
    }

    .el-menu-item {
      font-size: 16px;
      /* 菜单字体大小 */
      color: #fff;
      /* 菜单字体颜色 */
      padding: 0 15px;
      /* 增加菜单项间距 */
    }

    .el-menu-item:hover {
      color: #fe5459 !important;
      /* 鼠标悬停时字体变红 */
    }
  }
}
</style>
