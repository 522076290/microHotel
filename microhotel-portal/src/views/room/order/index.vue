<template>
  <div class="app-container">
    <!-- 背景图块 -->
    <section class="breadcrumb-outer">
      <div class="container">
        <div class="breadcrumb-content">
          <el-breadcrumb separator="/">
            <el-breadcrumb-item v-for="item in breadcrumbs" :key="item.path" :to="{ path: item.path }"
              :class="{ active: item.path === $route.path }">
              {{ item.meta.title }}
            </el-breadcrumb-item>
          </el-breadcrumb>
        </div>
      </div>
    </section>

    <div class="room-order-detail">
      <el-row :gutter="40">
        <el-col :span="12">
          <el-card class="box-card">
            <div slot="header" class="clearfix">
              <span>订单信息</span>
            </div>
            <el-form :label-position="`left`" label-width="100px" :model="orderDetail">
              <el-form-item label="订单号">
                <span>{{ orderDetail.orderNumber }}</span>
              </el-form-item>
              <el-form-item label="总金额">
                <span>{{ orderDetail.totalPrice }}</span>
              </el-form-item>
              <el-form-item label="入住日期">
                <span>{{ orderDetail.startDate }}</span>
              </el-form-item>
              <el-form-item label="退房日期">
                <span>{{ orderDetail.endDate }}</span>
              </el-form-item>
              <el-form-item label="用户信息">
                <span>{{ orderDetail.sysUser.nickName }}  {{ orderDetail.sysUser.phonenumber }}</span>
              </el-form-item>
              <el-form-item label="订单状态">
                <span v-for="item in dict.type.hotel_status" 
                      :key="item.value" 
                      v-if="item.value == orderDetail.status"
                      :class="`el-tag--`+item.raw.cssClass" class="el-tag  el-tag--medium el-tag--light">
                  {{ item.label }}
                </span>
              </el-form-item>
              <el-form-item label="商家接单状态">
                <span v-for="item in dict.type.accept_status" 
                      :key="item.value" 
                      v-if="item.value == orderDetail.merchantAcceptStatus"
                      :class="`el-tag--`+item.raw.cssClass" class="el-tag el-tag--medium el-tag--light">
                  {{ item.label }}
                </span>
              </el-form-item>
              <el-form-item label="订单创建时间">
                <span>{{ orderDetail.orderDate }}</span>
              </el-form-item>
            </el-form>
            <el-row>
              <el-button type="primary" @click="goOrderManage">前往订单管理</el-button>
            </el-row>
          </el-card>
        </el-col>
        <!-- Right: Booking Info -->
        <el-col :span="12">
          <el-card class="box-card">
            <div slot="header" class="clearfix">
              <span>客房明细</span>
            </div>
            <el-form :label-position="`left`" label-width="100px" :model="orderDetail">
              <el-form-item label="客房名称">
                <a class="text-success"><span @click="goToRoomDetail(orderDetail.hotelRooms.id)">{{ orderDetail.hotelRooms.title }}</span></a>
              </el-form-item>
              <el-form-item label="客房号">
                <span>{{ orderDetail.hotelRooms.roomNumber }}</span>
              </el-form-item>
              <el-form-item label="金额">
                <span>{{ orderDetail.hotelRooms.price}}/晚</span>
              </el-form-item>
            </el-form>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { getOrders } from "@/api/hotel/orders";
export default {
  dicts: ['hotel_status','accept_status'],
  data() {
    return {
      orderDetail: {},
    };
  },
  methods: {
    /** 获取当前路由最后的数字 */
    getLastNumber() {
      const path = this.$route.path;
      const lastNumber = path.match(/\d+$/);
      return lastNumber ? parseInt(lastNumber[0]) : 1;
    },
    getOrders() {
      getOrders(this.getLastNumber()).then(response => {
        this.orderDetail = response.data;
        // 更新组件
        this.$nextTick(() => {
        });
      });
    },
    goToRoomDetail(id) {
      this.$router.push({ path: `/room/detail/${id}` });
    },
    goOrderManage(){
      //当前页面跳转网址不要新开页面
      window.location.href = process.env.VUE_APP_BACK_STAGE_URL + `/hotel/orders`;
    }
  },
  created() {
    this.getOrders();
  },
  computed: {
    breadcrumbs() {
      // 生成面包屑导航
      const breadcrumbs = [];
      const matched = this.$route.matched;
      matched.forEach((item) => {
        if (item.meta.title) {
          breadcrumbs.push({ path: item.path, meta: item.meta });
        }
      });
      return breadcrumbs;
    }
  },
};
</script>

<style lang="scss" scoped>
.app-container {
  width: 100%;
  margin: 0;
  padding: 0;
  padding-bottom: 80px;
  box-sizing: border-box;
}

/* 背景图块 */
.breadcrumb-outer {
  background-image: url('../../../assets/images/header-bgckground.png');
  /* 背景图片路径 */
  background-size: cover;
  /* 背景图片自适应 */
  background-position: center;
  /* 背景图片居中 */
  padding: 160px 0;
  text-align: center;
  position: relative;
  background-position: 50%;


  .breadcrumb-content {
    h2 {
      font-size: 32px;
      font-weight: bold;
      margin-bottom: 15px;
      text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.6);
      color: #fff;
      /* 文字阴影效果 */
    }

    .el-breadcrumb {
      display: inline-block;
      /* 添加内联块样式，确保居中 */
      margin: 0 auto;
      /* 自动居中 */
      text-align: center;
      /* 确保文字居中 */
      font-size: 16px;
      /* 字体大小 */
      color: #fff !important;
      /* 文字颜色 */

      .el-breadcrumb-item {
        color: #fff !important;

        /* 面包屑普通状态颜色 */
        &:last-child {
          color: #fe5459 !important;
          /* 当前页颜色 */
        }
      }
    }
  }
}

/* 被选中时的颜色 */
.el-breadcrumb__item:last-child ::v-deep .el-breadcrumb__inner {
  color: #ff6b6b !important;
  font-weight: 800 !important;
}

.room-order-detail {
  display: grid;
  gap: 20px;
  margin: 20px auto;
  width: 90%;
  max-width: 1200px;
  padding-top: 60px;
}

</style>
