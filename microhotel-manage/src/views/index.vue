<template>
  <div class="app-container home">
   <div>
    <h2> {{ userInfo.nickName }}，你好！欢迎酒店预订管理系统。</h2>
    <h3>你是 {{userInfo.roles[0].roleName}}，具有以下功能：</h3>
    <p>客房管理：可以管理所有客房信息</p>
    <p>客房分类管理：可以管理所有客房分类信息</p>
    <p>订单管理：可以管理所有订单信息</p>
    <p> 订单评价管理：可以管理所有订单信息</p>
    <p>账单管理：可以管理所有账单信息</p>
    <p>用户管理：可以管理所有用户信息</p>
    <p>个人信息：可以修改您的个人信息和登录密码</p>
   </div>
    <el-row :gutter="20">
      <el-col :xs="24" :sm="24" :md="12" :lg="8">
        <el-card class="update-log">
          <div slot="header" class="clearfix">
            <span>联系信息</span>
          </div>
          <div class="body">
            <p>
              <i class="el-icon-s-promotion"></i> 官网：<el-link
                href="http://www.ruoyi.vip"
                target="_blank"
                >http://www.ruoyi.vip</el-link
              >
            </p>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import {store} from "@/store";
import { getUserProfile} from "@/api/system/user";

export default {
  name: "Index",
  data() {
    return {
      userInfo:null,
    };
  },
  created() {
    this.getUserProfile();
  },
  methods: {
    getUserProfile(){
      getUserProfile().then(response => {
        this.userInfo = response.data;
        console.log(this.userInfo)
      });
      
    },
    goTarget(href) {
      window.open(href, "_blank");
    }
  }
};
</script>

<style scoped lang="scss">
.home {
  blockquote {
    padding: 10px 20px;
    margin: 0 0 20px;
    font-size: 17.5px;
    border-left: 5px solid #eee;
  }
  hr {
    margin-top: 20px;
    margin-bottom: 20px;
    border: 0;
    border-top: 1px solid #eee;
  }
  .col-item {
    margin-bottom: 20px;
  }

  ul {
    padding: 0;
    margin: 0;
  }

  font-family: "open sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
  font-size: 13px;
  color: #676a6c;
  overflow-x: hidden;

  ul {
    list-style-type: none;
  }

  h4 {
    margin-top: 0px;
  }

  h2 {
    margin-top: 10px;
    font-size: 26px;
    font-weight: 100;
  }

  p {
    margin-top: 10px;

    b {
      font-weight: 700;
    }
  }

  .update-log {
    ol {
      display: block;
      list-style-type: decimal;
      margin-block-start: 1em;
      margin-block-end: 1em;
      margin-inline-start: 0;
      margin-inline-end: 0;
      padding-inline-start: 40px;
    }
  }
}
</style>

