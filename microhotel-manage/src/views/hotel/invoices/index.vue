<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="订单ID，关联订单信息表" prop="orderId">
        <el-input
          v-model="queryParams.orderId"
          placeholder="请输入订单ID，关联订单信息表"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="客户ID，关联客户信息表" prop="customerId">
        <el-input
          v-model="queryParams.customerId"
          placeholder="请输入客户ID，关联客户信息表"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="房间总费用" prop="totalRoomCharge">
        <el-input
          v-model="queryParams.totalRoomCharge"
          placeholder="请输入房间总费用"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="额外服务费用" prop="additionalCharges">
        <el-input
          v-model="queryParams.additionalCharges"
          placeholder="请输入额外服务费用"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="折扣金额" prop="discount">
        <el-input
          v-model="queryParams.discount"
          placeholder="请输入折扣金额"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="税费" prop="tax">
        <el-input
          v-model="queryParams.tax"
          placeholder="请输入税费"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="应付总额" prop="totalDue">
        <el-input
          v-model="queryParams.totalDue"
          placeholder="请输入应付总额"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="支付状态的整型表示，对应字典表中的状态" prop="paymentStatus">
        <el-select v-model="queryParams.paymentStatus" placeholder="请选择支付状态的整型表示，对应字典表中的状态" clearable>
          <el-option
            v-for="dict in dict.type.hotel_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="支付日期" prop="paymentDate">
        <el-date-picker clearable
          v-model="queryParams.paymentDate"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择支付日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="账单开具日期" prop="issueDate">
        <el-date-picker clearable
          v-model="queryParams.issueDate"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择账单开具日期">
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
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['hotel:invoices:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['hotel:invoices:edit']"
        >修改</el-button>
      </el-col>
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
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['hotel:invoices:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="invoicesList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="账单唯一标识ID" align="center" prop="id" />
      <el-table-column label="订单ID，关联订单信息表" align="center" prop="orderId" />
      <el-table-column label="客户ID，关联客户信息表" align="center" prop="customerId" />
      <el-table-column label="房间总费用" align="center" prop="totalRoomCharge" />
      <el-table-column label="额外服务费用" align="center" prop="additionalCharges" />
      <el-table-column label="折扣金额" align="center" prop="discount" />
      <el-table-column label="税费" align="center" prop="tax" />
      <el-table-column label="应付总额" align="center" prop="totalDue" />
      <el-table-column label="支付状态的整型表示，对应字典表中的状态" align="center" prop="paymentStatus">
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
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['hotel:invoices:edit']"
          >修改</el-button>
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

    <!-- 添加或修改酒店账单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="订单ID，关联订单信息表" prop="orderId">
          <el-input v-model="form.orderId" placeholder="请输入订单ID，关联订单信息表" />
        </el-form-item>
        <el-form-item label="客户ID，关联客户信息表" prop="customerId">
          <el-input v-model="form.customerId" placeholder="请输入客户ID，关联客户信息表" />
        </el-form-item>
        <el-form-item label="房间总费用" prop="totalRoomCharge">
          <el-input v-model="form.totalRoomCharge" placeholder="请输入房间总费用" />
        </el-form-item>
        <el-form-item label="额外服务费用" prop="additionalCharges">
          <el-input v-model="form.additionalCharges" placeholder="请输入额外服务费用" />
        </el-form-item>
        <el-form-item label="折扣金额" prop="discount">
          <el-input v-model="form.discount" placeholder="请输入折扣金额" />
        </el-form-item>
        <el-form-item label="税费" prop="tax">
          <el-input v-model="form.tax" placeholder="请输入税费" />
        </el-form-item>
        <el-form-item label="应付总额" prop="totalDue">
          <el-input v-model="form.totalDue" placeholder="请输入应付总额" />
        </el-form-item>
        <el-form-item label="支付状态的整型表示，对应字典表中的状态" prop="paymentStatus">
          <el-select v-model="form.paymentStatus" placeholder="请选择支付状态的整型表示，对应字典表中的状态">
            <el-option
              v-for="dict in dict.type.hotel_status"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="支付日期" prop="paymentDate">
          <el-date-picker clearable
            v-model="form.paymentDate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择支付日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="账单开具日期" prop="issueDate">
          <el-date-picker clearable
            v-model="form.issueDate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择账单开具日期">
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
import { listInvoices, getInvoices, delInvoices, addInvoices, updateInvoices } from "@/api/hotel/invoices";

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
