<template>
  <div class="app-container">
    <!-- 背景图块 -->
    <section class="breadcrumb-outer">
      <div class="container">
        <div class="breadcrumb-content">
          <h2>希望给您留下一段美好的回忆</h2>
          <el-breadcrumb separator="/">
            <el-breadcrumb-item v-for="item in breadcrumbs" :key="item.path" :to="{ path: item.path }"
              :class="{ active: item.path === $route.path }">
              {{ item.meta.title }}
            </el-breadcrumb-item>
          </el-breadcrumb>
        </div>
      </div>
    </section>

    <!-- 信息框 -->
    <div class="info-box">
      <p>1. 本站为演示站，<a href="#">详细介绍地址点击</a>，每小时会重新覆盖数据。</p>
      <p>2. 用户登录账号等信息，请参考说明。</p>
      <p>3. 博主长期接受专业设计项目，收徒带新人，诚信可靠。</p>
    </div>

    <!-- 房型选择和日期搜索 -->
    <div class="filter">
      <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" label-width="68px">
        <el-form-item label="客房类型" prop="type">
          <el-select v-model="queryParams.type" placeholder="请选择客房类型" clearable>
            <el-option v-for="dict in dict.type.hotel_room_type" :key="dict.value" :label="dict.label"
              :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="预定日期" prop="endDate">
          <el-date-picker v-model="queryParams.dateRange" type="daterange" align="right" unlink-panels range-separator="至"
            start-placeholder="开始日期" end-placeholder="结束日期" :picker-options="pickerOptions">
          </el-date-picker>
        </el-form-item>
        <el-button type="primary" @click="handleQuery">搜索</el-button>
        <el-button type="primary" @click="resetQuery">重置</el-button>
      </el-form>
    </div>

    <!-- 房型展示 -->
    <div class="room-list">
      <el-card v-for="room in roomsList" :key="room.id" class="room-card">
        <!-- 图片部分 -->
        <div class="room-image-wrapper">
          <image-preview class="room-image" :src="room.image" />
        </div>
        <!-- 房间信息部分 -->
        <div class="room-info">
          <h3 class="room-title">{{ room.title }}</h3>
          <p class="room-price">¥{{ room.price }}/晚</p>
          <div class="room-rating">
            <el-rate v-model="roomRating" disabled></el-rate>
          </div>
          <div class="room-tags">
              <div class="room-type">
                <i class="el-icon-s-grid"></i>
                <span class="info-label">{{ getLabel(room.type) }}</span>
              </div>
              <div class="room-number">
                <i class="el-icon-office-building"></i>
                <span>{{ room.roomNumber }}</span>
              </div>
          </div>
          <p class="room-description" v-html="truncatedDescription(room.description)"></p>
        </div>
        <!-- 按钮部分 -->
        <el-button type="primary" size="small" class="room-button" @click="toRoomDetal(room.id)">查看&预定</el-button>
      </el-card>
    </div>

    <!-- 分页 -->
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" :page-sizes="[6, 12, 24, 48]"
      @pagination="getList" />

  </div>
</template>

<script>
import { listRooms, getRooms, delRooms, addRooms, updateRooms } from "@/api/hotel/rooms";
export default {
  dicts: ['hotel_room_type'],
  data() {
    return {
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 6,
        type: null,
        dateRange: null,
      },
      // 总条数
      total: 0,
      roomRating: 5,
      roomsList: [
      ],
      // 预定选项
      pickerOptions: {
          disabledDate(time) {
            // 选择今天以及今天之后的日期
            return time.getTime() < Date.now() - 8.64e7// 如果没有后面的-8.64e7就是不可以选择
            // 选择今天以及今天之前的日期
            // return time.getTime() > Date.now() - 8.64e7// 如果没有后面的-8.64e7就是不可以选择今天的
          },
          shortcuts: [{
            text: '预定一天',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              end.setTime(start.getTime() + 3600 * 1000 * 24 * 1);
              picker.$emit('pick', [start, end]);
            }
          }, {
            text: '预定两天',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              end.setTime(start.getTime() + 3600 * 1000 * 24 * 2);
              picker.$emit('pick', [start, end]);
            }
          }, {
            text: '预定三天',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              end.setTime(start.getTime() + 3600 * 1000 * 24 * 3);
              picker.$emit('pick', [start, end]);
            }
          }, {
            text: '预定一周',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              end.setTime(start.getTime() + 3600 * 1000 * 24 * 7);
              picker.$emit('pick', [start, end]);
            }
          }]
      },
    };
  },
  methods: {
    /** 查询酒店房间列表 */
    getList() {
      this.loading = true;
      listRooms(this.queryParams).then(response => {
        this.roomsList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.queryParams.dateRange = null; 
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 禁用日期  */
    disabledDate(time) {
      return time.getTime() < new Date().setHours(0, 0, 0, 0);
    },
    /** 获取字典标签 */
    getLabel(value) {
      const dict = this.dict.type.hotel_room_type;
      const found = dict.find(item => item.value === value);
      return found ? found.label : ''; // Return label if found, otherwise empty
    },

    /** 限制描述长度 */
    truncatedDescription(description) {
      const maxWords = 13; // 设置限制的字数
      if (!description) return '';
      const words = description.split(/\s+/); // Split description into words
      return words.length > maxWords
        ? words.slice(0, maxWords).join(' ') + '...' // Truncate and add ellipsis
        : description; // Return the full description if under the limit
    },

    /** 禁用过去日期 */
    disablePastDates(date) {
      // Disable dates before today
      return date < new Date(new Date().setHours(0, 0, 0, 0));
    },

    /** 获取当前路由最后的数字 */
    getLastNumber() {
      const path = this.$route.path;
      const lastNumber = path.match(/\d+$/);
      return lastNumber ? parseInt(lastNumber[0]) : 1;
    },
    /** 跳转到房间详情页面 */
    toRoomDetal(roomid){
      this.$router.push({ path: `/room/detail/${roomid}` });
    }
  },
  created() {
    this.queryParams.type = String(this.getLastNumber());
    this.getList();
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

/* 信息框 */
.info-box {
  margin: 20px auto;
  padding: 15px;
  background: #f9f9f9;
  border: 1px solid #ddd;
  width: 90%;
  max-width: 1200px;
  line-height: 1.8;
  text-align: left;
  font-size: 14px;
  color: #333;

  a {
    color: #fe5459;
    text-decoration: none;

    &:hover {
      text-decoration: underline;
    }
  }
}

/* 过滤栏 */
.filter {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 15px;
  margin: 20px auto;
  padding: 15px;
  background: #fff;
  border-radius: 5px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  width: 90%;
  max-width: 1200px;
}

.pagination-container {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 15px;
  margin: 20px auto;
  padding: 15px;
  background: #fff;
  border-radius: 5px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  width: 90%;
  max-width: 1200px;
  height: 96px;
}


.room-list {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 20px;
  margin: 20px auto;
  width: 90%;
  max-width: 1200px;
}

.room-card {
  padding: 0;
  width: 310px;
  border: 1px solid #ddd;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.room-image-wrapper {
  width: 100%;
  height: 200px;
  overflow: hidden;
}

.room-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.room-info {
  padding: 15px;
  text-align: left;

  .room-title {
    font-size: 18px;
    font-weight: bold;
    color: #333;
    margin-bottom: 10px;
  }

  .room-price {
    color: #fe5459;
    font-size: 16px;
    font-weight: bold;
    margin: 5px 0;
  }

  .room-rating {
    color: #ffcc00;
    font-size: 14px;
    margin: 5px 0;

    i {
      margin-right: 2px;
    }
  }

  .room-tags {
    margin: 5px 0;
    color: #000000;
    font-size: 16px;
    display: flex;
    justify-content: space-between;
    align-items: center;

    span {
      margin-right: 30px;
      font-size: 14px;
    }
    .room-number {
      margin-left: auto;
    }
  }

  .room-description {
    color: #666;
    font-size: 14px;
    margin: 10px 0;
  }
}

.room-button {
  margin: 10px;
  align-self: center;
  background-color: #fe5459;
  border-color: #fe5459;
  color: white;

  &:hover {
    background-color: #ff6b6b;
    border-color: #ff6b6b;
  }
}

/* 被选中时的颜色 */
.el-breadcrumb__item:last-child ::v-deep .el-breadcrumb__inner {
        color: #ff6b6b !important;
        font-weight:800 !important;
}
</style>
