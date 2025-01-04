<template>
  <div class="payment-page">
    <h1 class="title">订单支付</h1>
    <!-- QR Code -->
    <div class="qr-code-container">
      <img src="@/assets/images/pay.png" alt="QR Code" class="qr-code" />
    </div>
    <!-- Payment Details -->
    <div class="payment-details">
      <p class="order-amount">订单金额：<span>¥{{ orderDetail.totalPrice }}</span></p>
      <p class="order-id">订单编号：<span>{{ orderDetail.orderNumber }}</span></p>
    </div>
    <!-- Buttons -->
    <div class="action-buttons">
      <el-button type="primary" @click="goBack">返回</el-button>
      <el-button type="success" @click="markAsPaid">已完成支付</el-button>
    </div>
  </div>
</template>

<script>

import { getOrders,updateOrders } from "@/api/hotel/orders";
import { addInvoices } from "@/api/hotel/invoices";

export default {
  data() {
    return {
      orderDetail:{}
    };
  },
  created() {
    this.getOrderDetails();
  },
  methods: {
    goBack() {
      this.$router.push("/"); // Navigate to the homepage or previous page
    },
    markAsPaid() {
      // 先检查订单的状态 看看是否支付还是取消
      getOrders(this.getLastNumber()).then( response=>{
        this.orderDetail = response.data;
        if (this.orderDetail.status === 1) {
          this.$modal.msgError("订单已经支付！");
          this.$router.push("/room/order/"+this.getLastNumber()); // 跳转到订单详情页面
          return;
      }
      if (this.orderDetail.status === 2) {
          this.$modal.msgError("订单超时支付，已取消！");
          this.$router.push("/room/order/"+this.getLastNumber()); // 跳转到订单详情页面
          return;
      }
      this.$modal.confirm("是否确认已完成支付？").then(() => {
        updateOrders({
          id: this.getLastNumber(),
          status:1
        }).then(() => {
          this.$modal.msgSuccess("支付成功，等待商家接单");
          addInvoices({
            orderId: this.getLastNumber(),
            customerId: this.orderDetail.sysUser.userId,
            totalRoomCharge: this.orderDetail.totalPrice,
            paymentStatus: 1,
            paymentDate: new Date().getTime(),
          });
          this.$router.push("/room/order/"+this.getLastNumber()); // 跳转到订单详情页面
        });
      });
      })
    },
    /** 获取当前路由最后的数字 */
    getLastNumber() {
      const path = this.$route.path;
      const lastNumber = path.match(/\d+$/);
      return lastNumber ? parseInt(lastNumber[0]) : 1;
    },
    getOrderDetails() {
      getOrders(this.getLastNumber()).then( response=>{
        this.orderDetail = response.data;
      })
    }
  },
};
</script>

<style scoped>
.payment-page {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 40px;
  background-color: #f9f9f9;
}

.title {
  font-size: 48px;
  font-weight: bold;
  color: #333;
  margin-bottom: 30px;
}

.qr-code-container {
  margin-bottom: 20px;
}

.qr-code {
  width: 350px;
  height: 350px;
  object-fit: cover;
}

.payment-details {
  margin-bottom: 20px;
  text-align: center;
}

.order-amount,
.order-id {
  font-size: 16px;
  color: #555;
  margin: 5px 0;
}

.order-amount span,
.order-id span {
  font-weight: bold;
  color: #333;
}

.action-buttons {
  display: flex;
  gap: 10px;
}

.el-button {
  width: 120px;
}
</style>