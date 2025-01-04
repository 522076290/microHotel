<template>
  <div class="app-container">

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
  name: "Info",
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
    goOrderManage(){
      //当前页面跳转网址不要新开页面
      window.location.href = process.env.VUE_APP_BACK_STAGE_URL + `/hotel/orders`;
    },
    goToRoomDetail(id) {
      window.open(process.env.VUE_APP_PORTAL_URL + `/room/detail/${id}`)
    },
  },
  created() {
    this.getOrders();
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
