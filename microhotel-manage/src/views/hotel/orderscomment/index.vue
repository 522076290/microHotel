<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="客户评分" prop="customerRating">
        <el-input
          v-model="queryParams.customerRating"
          placeholder="请选择客户评分"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="评价日期" prop="reviewDate">
        <el-date-picker clearable
          v-model="queryParams.reviewDate"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择评价日期">
        </el-date-picker>
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
      <el-table-column label="订单号码" align="center" prop="orderNumber" />
      <el-table-column label="客户评分" align="center" prop="customerRating" />
      <el-table-column label="客户评价内容" align="center" prop="customerReview" />
      <el-table-column label="评价日期" align="center" prop="reviewDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.reviewDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['hotel:orders:edit']"
          >查看</el-button>
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
        <el-form-item label="订单号码，用于客户查询和酒店管理" prop="orderNumber">
          <el-input v-model="form.orderNumber" placeholder="请输入订单号码，用于客户查询和酒店管理" />
        </el-form-item>
        <el-form-item label="客户ID，关联客户信息表" prop="customerId">
          <el-input v-model="form.customerId" placeholder="请输入客户ID，关联客户信息表" />
        </el-form-item>
        <el-form-item label="房间ID，关联房间信息表" prop="roomId">
          <el-input v-model="form.roomId" placeholder="请输入房间ID，关联房间信息表" />
        </el-form-item>
        <el-form-item label="入住日期" prop="startDate">
          <el-date-picker clearable
            v-model="form.startDate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择入住日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="离店日期" prop="endDate">
          <el-date-picker clearable
            v-model="form.endDate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择离店日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="预订的房间数量" prop="numOfRooms">
          <el-input v-model="form.numOfRooms" placeholder="请输入预订的房间数量" />
        </el-form-item>
        <el-form-item label="订单总价" prop="totalPrice">
          <el-input v-model="form.totalPrice" placeholder="请输入订单总价" />
        </el-form-item>
        <el-form-item label="预订日期" prop="orderDate">
          <el-date-picker clearable
            v-model="form.orderDate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择预订日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="订单状态的整型表示，对应字典表中的状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择订单状态的整型表示，对应字典表中的状态">
            <el-option
              v-for="dict in dict.type.hotel_status"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="商家接单日期" prop="merchantAcceptDate">
          <el-date-picker clearable
            v-model="form.merchantAcceptDate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择商家接单日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="商家接单状态的整型表示，对应字典表中的状态" prop="merchantAcceptStatus">
          <el-select v-model="form.merchantAcceptStatus" placeholder="请选择商家接单状态的整型表示，对应字典表中的状态">
            <el-option
              v-for="dict in dict.type.accept_status"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="客户评分，1到5星" prop="customerRating">
          <el-input v-model="form.customerRating" placeholder="请输入客户评分，1到5星" />
        </el-form-item>
        <el-form-item label="客户评价内容" prop="customerReview">
          <el-input v-model="form.customerReview" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="评价日期" prop="reviewDate">
          <el-date-picker clearable
            v-model="form.reviewDate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择评价日期">
          </el-date-picker>
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
        orderNumber: [
          { required: true, message: "订单号码，用于客户查询和酒店管理不能为空", trigger: "blur" }
        ],
        customerId: [
          { required: true, message: "客户ID，关联客户信息表不能为空", trigger: "blur" }
        ],
        roomId: [
          { required: true, message: "房间ID，关联房间信息表不能为空", trigger: "blur" }
        ],
        startDate: [
          { required: true, message: "入住日期不能为空", trigger: "blur" }
        ],
        endDate: [
          { required: true, message: "离店日期不能为空", trigger: "blur" }
        ],
        numOfRooms: [
          { required: true, message: "预订的房间数量不能为空", trigger: "blur" }
        ],
        totalPrice: [
          { required: true, message: "订单总价不能为空", trigger: "blur" }
        ],
        status: [
          { required: true, message: "订单状态的整型表示，对应字典表中的状态不能为空", trigger: "change" }
        ],
        merchantAcceptStatus: [
          { required: true, message: "商家接单状态的整型表示，对应字典表中的状态不能为空", trigger: "change" }
        ],
      }
    };
  },
  created() {
    this.getList();
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
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getOrders(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改酒店订单";
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
    }
  }
};
</script>
