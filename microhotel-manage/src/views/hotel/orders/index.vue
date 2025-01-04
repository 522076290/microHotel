<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="订单号" prop="orderNumber">
        <el-input
          v-model="queryParams.orderNumber"
          placeholder="请输入订单号码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="入住日期" prop="startDate">
        <el-date-picker clearable
          v-model="queryParams.startDate"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择入住日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="离店日期" prop="endDate">
        <el-date-picker clearable
          v-model="queryParams.endDate"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择离店日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="预订日期" prop="orderDate">
        <el-date-picker clearable
          v-model="queryParams.orderDate"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择预订日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="订单状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择订单状态" clearable>
          <el-option
            v-for="dict in dict.type.hotel_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="接单日期" prop="merchantAcceptDate">
        <el-date-picker clearable
          v-model="queryParams.merchantAcceptDate"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择商家接单日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="接单状态" prop="merchantAcceptStatus">
        <el-select v-model="queryParams.merchantAcceptStatus" placeholder="请选择商家接单状态" clearable>
          <el-option
            v-for="dict in dict.type.accept_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['hotel:orders:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="ordersList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <!-- <el-table-column label="订单唯一标识ID" align="center" prop="id" /> -->
      <el-table-column label="订单号码" align="center" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <router-link :to="'/hotel/orders/info/' + scope.row.id" class="link-type">
            <span>{{ scope.row.orderNumber }}</span>
          </router-link>
        </template>
      </el-table-column>
      <el-table-column label="用户信息" align="center" prop="customerId" />
      <el-table-column label="入住日期" align="center" prop="startDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.startDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="离店日期" align="center" prop="endDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.endDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="订单总价" align="center" prop="totalPrice" />
      <el-table-column label="预订日期" align="center" prop="orderDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.orderDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="订单状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.hotel_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="商家接单日期" align="center" prop="merchantAcceptDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.merchantAcceptDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="商家接单" align="center" prop="merchantAcceptStatus">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.accept_status" :value="scope.row.merchantAcceptStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            @click="handleCheck(scope.row)"
            v-hasPermi="['hotel:orders:edit']"
          >查看</el-button>
          <el-button v-if="scope.row.status == 1 && scope.row.merchantAcceptStatus ==0"
            size="mini"
            type="text"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['hotel:orders:edit']"
          >接单</el-button>
          <el-button v-if="scope.row.status == 1 && scope.row.merchantAcceptStatus ==1 && scope.row.customerId == user.userId && scope.row.customerRating == null"
            size="mini"
            type="text"
            @click="handleComment(scope.row)"
            v-hasPermi="['hotel:orders:edit']"
          >评价</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['hotel:orders:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改酒店订单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="客户评分" prop="customerRating">
          <el-rate show-text v-model="form.customerRating"></el-rate>
        </el-form-item>
        <el-form-item label="内容" prop="customerReview">
          <el-input v-model="form.customerReview" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import { listOrders, getOrders, delOrders, addOrders, updateOrders } from "@/api/hotel/orders";
import { getUserProfile } from "@/api/system/user";

export default {
  name: "Orders",
  dicts: ['hotel_status', 'accept_status'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 酒店订单表格数据
      ordersList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        orderNumber: null,
        customerId: null,
        roomId: null,
        startDate: null,
        endDate: null,
        numOfRooms: null,
        totalPrice: null,
        orderDate: null,
        status: null,
        merchantAcceptDate: null,
        merchantAcceptStatus: null,
        customerRating: null,
        customerReview: null,
        reviewDate: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        roomId: [
          { required: true, message: "房间ID，关联房间信息表不能为空", trigger: "blur" }
        ],
        customerReview:[
          { required: true, message: "内容不能为空", trigger: "blur" }
        ],
        customerRating:[
          { required: true, message: "客户评分不能为空", trigger: "blur" }
        ]
      },
      // 用户信息
      user: {},
    };
  },
  created() {
    this.getList();
    this.getUserInfo();
  },
  methods: {
    /** 查询酒店订单列表 */
    getList() {
      this.loading = true;
      listOrders(this.queryParams).then(response => {
        this.ordersList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        orderNumber: null,
        customerId: null,
        roomId: null,
        startDate: null,
        endDate: null,
        numOfRooms: null,
        totalPrice: null,
        orderDate: null,
        status: null,
        merchantAcceptDate: null,
        merchantAcceptStatus: null,
        customerRating: null,
        customerReview: null,
        reviewDate: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加酒店订单";
    },
    /** 查看按钮操作 */
    handleCheck(row) {
      this.reset();
      const id = row.id || this.ids
      //打开新的查看页面
      this.$router.push({ path: '/hotel/orders/info/' + id });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const orderNumber = row.orderNumber
      this.$modal.confirm('是否确认酒店订单编号为"' + orderNumber + '"的订单？').then(function() {
      }).then(() => {
        row.merchantAcceptStatus = 1;
        row.merchantAcceptDate = new Date().getTime();
        updateOrders(row).then(response => {
          this.$modal.msgSuccess("已成功接单");
          this.getList();
        });
      }).catch(() => {});
    },
    /** 评论按钮操作 */
    handleComment(row){
      this.reset();
      const id = row.id || this.ids
      getOrders(id).then(response => {
        this.form = response.data;
        this.form.reviewDate = new Date().getTime();
        this.open = true;
        this.title = "订单评价";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateOrders(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addOrders(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除酒店订单编号为"' + ids + '"的数据项？').then(function() {
        return delOrders(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('hotel/orders/export', {
        ...this.queryParams
      }, `orders_${new Date().getTime()}.xlsx`)
    },
    /** 获取用户信息 */
    getUserInfo(){
      getUserProfile().then(response => {
        this.user = response.data;
        console.log(response.data)
      });
    },
  }
};
</script>
