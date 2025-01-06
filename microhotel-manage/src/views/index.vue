<template>
  <div class="app-container home">
   <div v-if="userInfo.roles[0].roleKey==='admin'">
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
   <div v-else-if="userInfo.roles[0].roleKey==='common'">
    <h2> {{ userInfo.nickName }}，你好！欢迎酒店预订管理系统。</h2>
    <h3>你是 {{userInfo.roles[0].roleName}}，具有以下功能：</h3>
    <p>客房管理：可以查询和修改所有客房信息</p>
    <p>客房分类管理：可以查询和修改客房分类信息</p>
    <p>订单管理：可以查询和处理所有订单信息</p>
    <p>订单评价管理：可以查询和删除订单评价信息</p>
    <p>账单管理：可以查询所有账单信息</p>
    <p>用户管理：可以查询所有用户信息</p>
    <p>个人信息：可以修改您的个人信息和登录密码</p>
   </div>
   <div v-else-if="userInfo.roles[0].roleKey==='coustomer'">
    <h2> {{ userInfo.nickName }}，你好！欢迎酒店预订管理系统。</h2>
    <h3>你是 {{userInfo.roles[0].roleName}}，具有以下功能：</h3>
    <p>订单管理：可以查看和处理您的订单信息</p>
    <p>订单评价管理：可以查看和处理您的订单评价信息</p>
    <p>账单管理：可以查看您的所有账单信息</p>
    <p>个人信息：可以修改您的个人信息和登录密码</p>
   </div>
    <el-row :gutter="20" v-if="userInfo.roles[0].roleKey=== 'admin' || userInfo.roles[0].roleKey=== 'common'">
      <el-col :span="8">
        <div class="stastics">
          <el-statistic :value="orderNum" title="今日总订单数">
            <template slot="suffix">
              <span class="like">
                <i
                  class="el-icon-s-order"
                ></i>
              </span>
            </template>
          </el-statistic>
        </div>
        <div class="stastics">
          <el-statistic :value="income" title="今日收入">
            <template slot="suffix">
              ¥
              <span class="like">
                <i
                  class="el-icon-money"
                  style="color:red"
                ></i>
              </span>
            </template>
          </el-statistic>
        </div>
        <div class="stastics">
          <el-statistic :value="outcome" title="今日退款">
            <template slot="suffix">
              ¥
              <span class="like">
                <i
                  class="el-icon-money"
                  style="color:blue"
                ></i>
              </span>
            </template>
          </el-statistic>
        </div>
      </el-col>

      <el-col :xs="16" :sm="16" :md="12" :lg="8">
        <el-card class="update-log">
          <div slot="header" class="clearfix">
            <span>每日流水</span>
          </div>
          <div class="body">
            <WeeklyTransactionRecordsChart :data="chartData" :dimensions="['product', '收入', '支出']" />
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getUserProfile} from "@/api/system/user";
import { getWeekOrders } from "@/api/hotel/orders";
import WeeklyTransactionRecordsChart from '@/components/Echarts/WeeklyTransactionRecords/index';


export default {
  name: "Index",
  components: {
    WeeklyTransactionRecordsChart
  },
  data() {
    return {
      chartData: [
        { product: 'Matcha', 2016: 85.8, 2017: 93.7 },
        { product: 'Milk', 2016: 73.4, 2017: 55.1 },
        { product: 'Cheese', 2016: 65.2, 2017: 82.5 },
        { product: 'Walnut', 2016: 53.9, 2017: 39.1 }
      ],
      userInfo:null,
      orderNum:0,
      income:0,
      outcome:0,
    };
  },
  created() {
    this.getUserProfile();
    this.getWeekOrders();
  },
  methods: {
    /** 获取用户信息 */
    getUserProfile(){
      getUserProfile().then(response => {
        this.userInfo = response.data;
      });
    },
    /** 获取一周订单 */
    getWeekOrders(){
      getWeekOrders(0).then(response => {
        // 把所有orderDate日期格式化 yyyy-MM-dd
        response.data.forEach(order => {
            order.orderDate = order.orderDate.substring(0, 10);
        });
        // 统计今天的订单数
        this.orderNum = response.data.filter(order => order.orderDate == new Date().toISOString().substring(0, 10)).length;
        // 处理订单，按日期统计收入和支出
        const result = response.data.reduce((acc, order) => {
            // 如果这个日期还没出现过，则初始化该日期
            if (!acc[order.orderDate]) {
                acc[order.orderDate] = { income: 0, outcome: 0 };
            }
            // 根据订单状态更新收入或支出
            if (order.status == 1 || order.status == 3) {
                acc[order.orderDate].income += order.totalPrice; // 用户支付的收入
            }
            if (order.status == 3) {
                acc[order.orderDate].outcome += order.totalPrice; // 商户未接受的退款支出
            }
            return acc;
        }, {});
        
        this.income = result[new Date().toISOString().substring(0, 10)].income;
        this.outcome = result[new Date().toISOString().substring(0, 10)].outcome;

        // 将结果转换为所需的数组格式
        const chartData = Object.keys(result).map(date => ({
            product: date,
            收入: result[date].income,
            支出: result[date].outcome
        }));
        this.chartData = chartData
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

  .stastics{
    margin-top: 40px;
  }
}
</style>

