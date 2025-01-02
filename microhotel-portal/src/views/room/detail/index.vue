<template>
  <div class="app-container">
    <!-- 背景图块 -->
    <section class="breadcrumb-outer">
      <div class="container">
        <div class="breadcrumb-content">
          <h2>{{ roomDetail.title }}</h2>
          <el-breadcrumb separator="/">
            <el-breadcrumb-item v-for="item in breadcrumbs" :key="item.path" :to="{ path: item.path }"
              :class="{ active: item.path === $route.path }">
              {{ item.meta.title }}
            </el-breadcrumb-item>
          </el-breadcrumb>
        </div>
      </div>
    </section>

    <div class="room-detail">
      <!-- Room Detail Section -->
      <el-row :gutter="20">
        <!-- Left: Image Carousel -->
        <el-col :span="16">
          <div class="grid-content bg-purple">
            <el-carousel height="400px" arrow="always">
              <el-carousel-item v-for="(img, index) in imageList" :key="index">
                <img :src="img" class="carousel-image" alt="Room Image" />
              </el-carousel-item>
            </el-carousel>
          </div>
        </el-col>
        <!-- Right: Booking Info -->
        <el-col :span="8">
          <div class="grid-content bg-purple room-booking">
            <div class="room-price">
              <i class="el-icon-bed"></i>
              <h2>¥{{ roomDetail.price }}
                <span class="price">/晚</span>
              </h2>

            </div>
            <el-form :model="bookingInfo">
              <el-form-item label="入住日期">
                <el-date-picker v-model="bookingInfo.startDate" :picker-options="checkInOptions" type="date"
                  placeholder="请选择日期"></el-date-picker>
              </el-form-item>
              <el-form-item label="退房日期">
                <el-date-picker v-model="bookingInfo.endDate" :picker-options="checkOutOptions" type="date"
                  placeholder="请选择日期"></el-date-picker>
              </el-form-item>
              <el-button type="primary" size="large" class="book-button" @click="handleBooking">
                快速预定
              </el-button>
            </el-form>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="16">
          <!-- Room Description -->
          <div class="grid-content bg-purple">
            <h3 class="room-title">{{ roomDetail.title }}</h3>
            <div class="room-description" v-html="roomDetail.description"></div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="grid-content bg-purple customer-service">
            <h3 class="service-title">客服电话</h3>
            <p>24小时客服热线，快速咨询和预定</p>
            <p class="phone-number">21-2327-2888</p>
          </div>
        </el-col>
      </el-row>
    </div>



  </div>
</template>

<script>
import { getRooms } from "@/api/hotel/rooms";
import { addOrders } from "@/api/hotel/orders";
import { isExternal } from "@/utils/validate";
import { getUserProfile } from "@/api/system/user";

export default {
  dicts: ['hotel_room_type'],
  data() {
    return {
      roomDetail: {
        image: "",
      },
      checkInOptions: {
        disabledDate(time) {
          // 选择今天以及今天之后的日期
          return time.getTime() < Date.now() - 8.64e7// 如果没有后面的-8.64e7就是不可以选择
        }
      },
      imageList: [],
      bookingInfo: {
        customerId: null,
        roomId: null,
        startDate: new Date(),
        endDate: null,
      },
    };
  },
  watch: {
    'bookingInfo.startDate'(newValue, oldValue) {
      if (newValue && !oldValue) {
        // 第一次设置结束日期
        const end = new Date(newValue);
        end.setTime(end.getTime() + 3600 * 1000 * 24 * 1);
        this.bookingInfo.endDate = end;
      } else if (newValue && oldValue) {
        // 更新结束日期
        const end = new Date(newValue);
        end.setTime(end.getTime() + 3600 * 1000 * 24 * 1);
        this.bookingInfo.endDate = end;
      }
    }
  },
  methods: {
    /** 获取当前路由最后的数字 */
    getLastNumber() {
      const path = this.$route.path;
      const lastNumber = path.match(/\d+$/);
      return lastNumber ? parseInt(lastNumber[0]) : 1;
    },
    getRooms() {
      getRooms(this.getLastNumber()).then(response => {
        this.roomDetail = response.data;
        this.bookingInfo.roomId = this.roomDetail.id;
        // 更新组件
        this.$nextTick(() => {
          this.imageList = this.realSrcList();
        });
      });
    },
    realSrcList() {
      if (!this.roomDetail.image) {
        return;
      }
      let real_src_list = this.roomDetail.image.split(",");
      let srcList = [];
      real_src_list.forEach(item => {
        if (isExternal(item)) {
          return srcList.push(item);
        }
        return srcList.push(process.env.VUE_APP_BASE_API + item);
      });
      return srcList;
    },
    /** 设置默认日期 */
    defaultDateRange() {
      const start = new Date();
      const end = new Date();
      end.setTime(start.getTime() + 3600 * 1000 * 24 * 1); // 设置退房日期为入住日期的第二天
      this.bookingInfo.startDate = start;
      this.bookingInfo.endDate = end;
    },

    /** 预定房间 */
    async handleBooking() {
      await getUserProfile().then(response => {
        this.bookingInfo.customerId = response.data.userId;
        this.bookingInfo.startDate = this.bookingInfo.startDate.getTime();
        this.bookingInfo.endDate = this.bookingInfo.endDate.getTime();
        addOrders(this.bookingInfo).then(response => {
          this.$modal.msgSuccess("创建订单成功");
          this.open = false;
          // 页面跳转到支付页面 并携带订单id
          this.$router.push({
            path: '/room/order/pay' + '/' + response.data.id
          });
        });
      });
    },
  },
  created() {
    this.getRooms();
    this.defaultDateRange()
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
    },
    checkOutOptions() {
      return {
        disabledDate: (time) => {
          // 箭头函数里 this 还是指向 Vue 实例
          if (!this.bookingInfo.startDate) {
            return false;
          }
          return time.getTime() < this.bookingInfo.startDate.getTime();
        }
      };
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


.room-detail {
  display: grid;
  gap: 20px;
  margin: 20px auto;
  width: 90%;
  max-width: 1200px;
  padding-top: 60px;
}

.carousel-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 5px;
}

.room-booking {
  padding: 30px 20px;
  height: 100%;
  border: 1px solid #ddd;
  border-radius: 5px;
  background: #f9f9f9;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  text-align: center;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.room-price {
  font-size: 26px;
  font-weight: bold;
  color: #fe5459;
  margin-bottom: 20px;
  display: flex;
  flex-direction: column;
  align-items: center;

  .price {
    font-size: 18px;
    font-weight: bold;
  }

  i {
    font-size: 40px;
    color: #fe5459;
    margin-bottom: 10px;
  }
}

.book-button {
  width: 100%;
  background-color: #fe5459;
  border-color: #fe5459;
  color: white;

  &:hover {
    background-color: white;
    border-color: #fe5459;
    color: #fe5459;
  }
}

.room-title {
  font-size: 22px;
  font-weight: bold;
  margin-bottom: 10px;
}

.room-description {
  font-size: 16px;
  line-height: 1.8;
  color: #555;
}

.customer-service {
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 5px;
  margin: 20px auto;
  max-width: 1200px;
  background: #f9f9f9;
}

.service-title {
  font-size: 18px;
  font-weight: bold;
}

.phone-number {
  font-size: 20px;
  font-weight: bold;
  color: #333;
}
</style>
