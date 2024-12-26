<template>
  <div class="navbar">
    <div class="header-content">
      <ul>
        <li class="left-menu-item" @click="toggleSideBar">
          <i class="el-icon-message">admin@libing.com</i>
        </li>
        <li class="left-menu-item" @click="toggleSideBar">
          <i class="el-icon-phone">0591-8881-2888</i>
        </li>
        <li class="left-menu-item" @click="toggleSideBar">
          <i class="el-icon-add-location">福建省福州市溪源宫路 200号</i>
        </li>
        <li class="left-menu-item" @click="toggleSideBar">
          <i class="el-icon-postcard">邮编:200002</i>
        </li>
        <li class="right-menu-item">
          <i class="el-icon-caret-right" @click="toregister">注册</i>
        </li>
        <li class="right-menu-item">
          <i class="el-icon-user" @click="toLogin">登录</i>
        </li>
      </ul>
    </div>
    <div class="header-menu">
      <a class="navbar-brand" href="/">
        <img src="@/assets/images/logo.png" alt="">
      </a>
      <el-menu :default-active="`-1`" class="el-menu-demo" mode="horizontal" active-text-color="#fe5459">
        <el-menu-item index="-1" @click="$router.push('/index')">首页</el-menu-item>
        <!-- 遍历 data 数组，显示每个的 dictLabel -->
        <el-menu-item v-for="dict in dict.type.hotel_room_type" 
            :key="dict.value"
            :index="dict.value"
            @click="(navigateTo(dict.value))"> <!-- 动态路由路径 -->
          {{ dict.label }}
        </el-menu-item>
      </el-menu>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
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
.navbar {
  width: 100%;
  height: 125px;
  overflow: hidden;
  position: relative;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  border-bottom: 1px solid hsla(0, 0%, 100%, 0.2);
  display: flex;
  flex-direction: column; /* 让 header-content 和 header-menu 垂直排列 */
  align-items: center; /* 将子元素居中 */

  .header-content {
    width: 100%;
    max-width: 970px; /* 限制宽度，保持居中 */
    height: 44px;
    display: flex;
    justify-content: space-between; /* 左右两部分对齐 */
    align-items: center;

    .left-menu-item {
      display: inline-block;
      padding: 0 8px;
      font-size: 16px;
      color: #5a5e66;
      vertical-align: middle;
    }

    .left-menu-item:hover {
      cursor: pointer;
      color: #fe5459;
    }

    .right-menu-item {
      display: inline-block;
      padding: 0 8px;
      font-size: 16px;
      color: #5a5e66;
      vertical-align: middle;
    }

    .right-menu-item:hover {
      cursor: pointer;
      color: #fe5459;
    }
  }

  .header-menu {
    width: 100%;
    max-width: 940px; /* 限制菜单的宽度 */
    height: 80px;
    display: flex;
    justify-content: space-between; /* 左对齐 Logo，右对齐菜单 */
    align-items: center;
    padding: 0 20px;

    .navbar-brand {
      display: inline-block;
    }

    .el-menu {
      flex: 1; /* 占据剩余空间 */
      margin-left: 20px;
    }
  }
}

/* 自定义 el-menu-item 的 hover 样式 */
.el-menu-item:hover {
  color: #ff5252 !important; /* 自定义 hover 状态字体颜色 */
}

</style>
