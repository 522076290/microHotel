<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="账单类型">
        <el-select v-model="queryParams.paymentStatus" placeholder="请选账单状态">
          <el-option label="已付款" value=1></el-option>
          <el-option label="已退款" value=3></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="创建时间" prop="issueDate">
        <el-date-picker clearable
          v-model="queryParams.issueDate"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期">
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
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['hotel:invoices:remove']"
        >删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="invoicesList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="订单号" align="center" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <router-link :to="'/hotel/orders/info/' + scope.row.orders.id" class="link-type">
            <span>{{ scope.row.orders.orderNumber }}</span>
          </router-link>
        </template>
      </el-table-column>
      <el-table-column label="用户信息" align="center" prop="sysUser.nickName" />
      <el-table-column label="房间总费用" align="center" prop="totalRoomCharge" />
      <el-table-column label="状态" align="center" prop="paymentStatus">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.hotel_status" :value="scope.row.paymentStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="支付日期" align="center" prop="paymentDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.paymentDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="账单开具日期" align="center" prop="issueDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.issueDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['hotel:invoices:remove']"
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

  </div>
</template>

<script>
import { listInvoices, getInvoices, delInvoices, addInvoices, updateInvoices } from "@/api/hotel/invoices";
import store from "@/store";

export default {
  name: "Invoices",
  dicts: ['hotel_status'],
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
      // 酒店账单表格数据
      invoicesList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        orderId: null,
        customerId: null,
        totalRoomCharge: null,
        additionalCharges: null,
        discount: null,
        tax: null,
        totalDue: null,
        paymentStatus: null,
        paymentDate: null,
        issueDate: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        orderId: [
          { required: true, message: "订单ID，关联订单信息表不能为空", trigger: "blur" }
        ],
        customerId: [
          { required: true, message: "客户ID，关联客户信息表不能为空", trigger: "blur" }
        ],
        totalRoomCharge: [
          { required: true, message: "房间总费用不能为空", trigger: "blur" }
        ],
        additionalCharges: [
          { required: true, message: "额外服务费用不能为空", trigger: "blur" }
        ],
        discount: [
          { required: true, message: "折扣金额不能为空", trigger: "blur" }
        ],
        tax: [
          { required: true, message: "税费不能为空", trigger: "blur" }
        ],
        totalDue: [
          { required: true, message: "应付总额不能为空", trigger: "blur" }
        ],
        paymentStatus: [
          { required: true, message: "支付状态的整型表示，对应字典表中的状态不能为空", trigger: "change" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询酒店账单列表 */
    getList() {
      // 普通客户只查询自己的记录
      const roles = store.getters.roles;
      const userid = store.getters.userid
      if (roles.includes("coustomer")) {
        this.queryParams.customerId = userid;
      }
      this.loading = true;
      listInvoices(this.queryParams).then(response => {
        this.invoicesList = response.rows;
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
        orderId: null,
        customerId: null,
        totalRoomCharge: null,
        additionalCharges: null,
        discount: null,
        tax: null,
        totalDue: null,
        paymentStatus: null,
        paymentDate: null,
        issueDate: null
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
      this.title = "添加酒店账单";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getInvoices(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改酒店账单";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateInvoices(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addInvoices(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除酒店账单编号为"' + ids + '"的数据项？').then(function() {
        return delInvoices(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('hotel/invoices/export', {
        ...this.queryParams
      }, `invoices_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
