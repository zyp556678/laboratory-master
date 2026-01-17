<template>
  <div class="app-container">
    <!--条件搜索表单-->
    <el-row>
      <el-form :inline="true">
        <el-form-item>
          <el-select v-model="searchObj.labId" placeholder="实验室分室" filterable clearable @change="getAll()">
            <el-option v-for="item in labSub" :key="item.id" :label="item.labName" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-select v-model="searchObj.bookingStatus" placeholder="预约状态" clearable @change="getAll()">
            <el-option v-for="item in bookingStateOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="getAll()">查询</el-button>
        <el-button type="default" icon="el-icon-refresh" @click="reset()">清空</el-button>
      </el-form>
    </el-row>

    <el-row>
      <el-button type="danger" icon="el-icon-delete" :disabled="ids.length < 1" @click="batchDelete"
        >批量删除</el-button
      >
      <el-button v-if="hasRoles(1)" type="primary" icon="el-icon-download" @click="exportData">批量导出</el-button>
    </el-row>

    <el-row>
      <el-table
        :data="page.list"
        :border="false"
        v-loading="loading"
        element-loading-text="正在努力加载数据中..."
        stripe
        border
        style="width: 100%; margin: 15px 0px"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" fixed="left" />
        <el-table-column type="index" width="80" label="序号" fixed="left" />
        <el-table-column prop="labName" min-width="150" label="预约实验室" fixed="left" show-overflow-tooltip />
        <el-table-column v-if="hasRoles(1)" prop="username" label="预约用户" min-width="130" show-overflow-tooltip>
          <template v-slot="{ row }">
            <el-tag v-if="row.roleId === 2" type="primary" effect="plain">
              {{ row.username }}
              <el-divider direction="vertical" />
              教师
            </el-tag>
            <el-tag v-if="row.roleId === 3" type="info" effect="plain">
              {{ row.username }}
              <el-divider direction="vertical" />
              学生
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="bookingDate" label="预约日期" min-width="120" show-overflow-tooltip sortable />
        <el-table-column prop="timeSlot" label="预约时段" min-width="120" show-overflow-tooltip>
          <template v-slot="{ row }">
            <el-tag v-if="row.timeSlot == '上午'" type="primary" effect="plain">上午</el-tag>
            <el-tag v-if="row.timeSlot == '下午'" type="success" effect="plain">下午</el-tag>
            <el-tag v-if="row.timeSlot == '晚上'" type="warning" effect="plain">晚上</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="bookingNum" label="预约人数" min-width="120" show-overflow-tooltip sortable />
        <el-table-column prop="description" label="预约说明" min-width="120" show-overflow-tooltip />
        <el-table-column prop="bookingStatus" label="预约状态" min-width="100" show-overflow-tooltip>
          <template v-slot="{ row }">
            <el-tag v-if="row.bookingStatus === 0" type="warning" effect="plain">审核中</el-tag>
            <el-tag v-else-if="row.bookingStatus === 1" type="success" effect="plain">预约成功</el-tag>
            <el-tag v-else-if="row.bookingStatus === 2" type="danger" effect="plain">预约失败</el-tag>
            <el-tag v-else-if="row.bookingStatus === 3" type="info" effect="plain">已取消</el-tag>
            <el-tag v-else>未知状态</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="errReason" label="失败原因" min-width="120" show-overflow-tooltip>
          <template v-slot="{ row }">
            <span style="color: #f56c6c">{{ row.errReason }}</span>
          </template>
        </el-table-column>
        <el-table-column v-if="hasRoles(1)" prop="createAt" min-width="100" label="创建人" show-overflow-tooltip />
        <el-table-column prop="createTime" label="创建时间" min-width="120" show-overflow-tooltip sortable />
        <el-table-column v-if="hasRoles(1)" prop="updateAt" min-width="100" label="最后修改人" show-overflow-tooltip />
        <el-table-column
          v-if="hasRoles(1)"
          prop="updateTime"
          min-width="140"
          label="最后修改时间"
          show-overflow-tooltip
          sortable
        />

        <!-- 操作 -->
        <el-table-column label="操作" min-width="180" align="center" fixed="right">
          <template v-slot="{ row }">
            <el-link v-if="hasRoles(2, 3) && row.bookingStatus == 0" type="primary" @click="process(row.id, 3)"
              >取消预约</el-link
            >

            <el-popconfirm v-if="row.bookingStatus != 0" title="确定删除吗？" @confirm="remove(row.id)">
              <template #reference>
                <el-link type="danger" size="mini">删除</el-link>
              </template>
            </el-popconfirm>

            <template v-if="hasRoles(1) && row.bookingStatus == 0">
              <el-link type="success" @click="process(row.id, 1)">通过</el-link>
              <el-divider direction="vertical" />
              <el-link type="danger" @click="process(row.id, 2)">不通过</el-link>
            </template>
          </template>
        </el-table-column>
      </el-table>
    </el-row>

    <!-- 分页导航栏 -->
    <el-row>
      <el-pagination
        @current-change="getAll"
        @size-change="handleSizeChange"
        :current-page="pIndex"
        :page-size="pSize"
        :page-sizes="[3, 6, 9, 12, 15]"
        background
        layout="total, sizes, ->, prev, pager, next, jumper"
        :total="page.total"
      >
      </el-pagination>
    </el-row>

    <!-- 表单 -->
    <el-dialog :title="dialogTitle" :visible.sync="visible" @close="closeForm('labBookingForm')">
      <el-form ref="labBookingForm" :model="labBooking" :rules="rules" label-width="150px">
        <el-row :gutter="10">
          <el-col :xs="24" :sm="24" :md="24" :lg="12" :xl="12">
            <el-form-item label="ID" prop="id" :hidden="true">
              <el-input v-model="labBooking.id" disabled />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :md="24" :lg="12" :xl="12">
            <el-form-item label="所属实验室:" prop="labId">
              <el-select v-model="labBooking.labId" style="width: 100%" placeholder="请选择实验室">
                <el-option v-for="item in labSub" :key="item.id" :label="item.labName" :value="item.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :md="24" :lg="12" :xl="12">
            <el-form-item label="预约开始时间:" prop="startTime" required>
              <el-date-picker
                style="width: 100%"
                v-model.trim="labBooking.startTime"
                value-format="yyyy-MM-dd HH:mm:ss"
                type="datetime"
                placeholder="请选择预约开始时间"
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :md="24" :lg="12" :xl="12">
            <el-form-item label="预约结束时间:" prop="endTime" required>
              <el-date-picker
                style="width: 100%"
                v-model.trim="labBooking.endTime"
                value-format="yyyy-MM-dd HH:mm:ss"
                type="datetime"
                placeholder="请选择预约结束时间"
              />
            </el-form-item>
          </el-col>
          <el-col>
            <el-form-item label="备注" prop="remark">
              <el-input
                v-model="labBooking.remark"
                :rows="3"
                type="textarea"
                placeholder="请输入备注"
                maxlength="255"
                show-word-limit
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="visible = false">取 消</el-button>
        <el-button @click="resetForm">重 置</el-button>
        <el-button type="primary" @click="save('labBookingForm')">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog title="原因" :visible.sync="bookingErrVisible" @close="closeForm('bookingErrForm')">
      <el-form :model="bookingErr" :rules="bookingErrRules" ref="bookingErrForm">
        <el-form-item prop="errReason">
          <el-input v-model="bookingErr.errReason" type="textarea" placeholder="请输入失败原因"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="bookingErrVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleProcessErr">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
//引入api接口定义的js文件
import labBookingApi from '@/api/lab/booking'
import labSubApi from '@/api/lab/sub'
import { exportDataExcel } from '@/utils/index'

export default {
  data() {
    return {
      // 列表相关
      pIndex: 1, //当前页
      pSize: 6, //每页显示记录数
      searchObj: {}, //条件封装对象
      page: {},
      loading: false,
      ids: [],
      labSub: [],
      bookingStateOptions: [
        { value: 0, label: '审核中' },
        { value: 1, label: '预约成功' },
        { value: 2, label: '预约失败' }
      ],
      dialogTitle: '',

      // 表单相关
      visible: false,
      labBooking: {
        id: undefined,
        labId: undefined,
        userId: undefined,
        bookingTime: '',
        remark: ''
      },

      rules: {
        labId: [{ required: true, message: '请选择实验室', trigger: 'change' }],
        bookingTime: [{ required: true, message: '请选择预约时间', trigger: 'change' }]
      },

      bookingErrVisible: false,
      bookingErr: {
        id: undefined,
        bookingStatus: undefined,
        errReason: ''
      },
      bookingErrRules: {
        errReason: [{ required: true, message: '请输入失败原因', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.getAll()
    this.getSub()
  },
  methods: {
    getSub() {
      labSubApi.getList().then((res) => {
        this.labSub = res.data
      })
    },
    //实验室信息
    getAll(current = 1) {
      this.loading = true
      // 加载列表数据
      this.pIndex = current //添加当前页参数
      // axios异步获取远程数据（ajax），通过api
      labBookingApi
        .getPage(this.pIndex, this.pSize, this.searchObj)
        .then((response) => {
          this.page = response.data
        })
        .finally(() => {
          this.loading = false
        })
    },

    // 改变页码
    handleSizeChange(pageSize) {
      this.pSize = pageSize
      this.getAll()
    },

    // 复选框选中
    handleSelectionChange(r) {
      // 得到选中的id
      this.ids = r.map((v) => v.id)
    },

    //删除实验室信息
    remove(id) {
      const loading = this.$loading({
        lock: true,
        text: '正在删除中...',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      })
      //确定执行then方法
      //调用接口
      labBookingApi
        .remove(id)
        .then((resp) => {
          //提示
          this.$message.success('删除成功!')
          //刷新页面
          this.getAll()
        })
        .finally(() => {
          loading.close()
        })
    },

    // 批量删除
    batchDelete() {
      if (this.ids.length > 0) {
        this.$confirm('此操作将永久删除实验室信息, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          const loading = this.$loading({
            lock: true,
            text: '正在删除中...',
            spinner: 'el-icon-loading',
            background: 'rgba(0, 0, 0, 0.7)'
          })
          // 调用接口
          labBookingApi
            .batchRemove(this.ids)
            .then((resp) => {
              // 提示
              this.$message.success('删除成功!')
              // 刷新页面
              this.getAll()
            })
            .finally(() => {
              loading.close()
            })
        })
      } else {
        this.$message.warning('请至少选择一条数据')
      }
    },

    // 清空
    reset() {
      this.searchObj = {}
      this.getAll()
    },

    // 打开表单
    openEdit(id) {
      this.dialogTitle = '新增实验室预约'
      if (id != null) {
        this.dialogTitle = '编辑实验室预约'
        this.getById(id)
      }

      this.visible = true
    },

    // 根据Id查询
    getById(id) {
      labBookingApi.getById(id).then((res) => {
        this.labBooking = res.data
      })
    },

    // 关闭表单
    closeForm(form) {
      if (!form) return
      this.visible = false
      console.log(form)
      this.$refs[form].resetFields()
    },

    // 重置表单
    resetForm() {
      if (this.labBooking.id) {
        this.getById(this.labBooking.id)
      }
      this.$refs['labBookingForm'].resetFields()
    },

    // 保存
    save(form) {
      this.$refs[form].validate((valid) => {
        if (valid) {
          const apiCall = this.labBooking.id
            ? labBookingApi.update(this.labBooking)
            : labBookingApi.save(this.labBooking)

          apiCall.then((res) => {
            this.$message.success('操作成功')
            this.visible = false
            this.getAll()
          })
        }
      })
    },

    // 导出
    async exportData() {
      try {
        const res = await labBookingApi.exportData()
        exportDataExcel(res)
        this.$message.success('文件导出成功')
      } catch (error) {
        console.log('🚀 ~ labBookingApi.exportData ~ error:', error)
        this.$message.error('文件导出失败')
      }
    },

    // 审核
    process(id, bookingStatus) {
      let text
      let color
      if (bookingStatus == 0) {
        text = '确定要取消预约吗'
        color = 'warning'
      } else if (bookingStatus == 1) {
        text = '确定要标记该预约记录为通过吗？'
        color = 'success'
      } else if (bookingStatus == 2) {
        this.bookingErrVisible = true
        this.bookingErr.id = id
        this.bookingErr.bookingStatus = bookingStatus
        return
      }
      this.$confirm(text, `提示`, {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: color
      }).then(() => {
        // 调用接口
        labBookingApi.process({ id, bookingStatus, errReason: this.errReason }).then((resp) => {
          // 提示
          this.$message.success('操作成功!')
          // 刷新页面
          this.getAll()
        })
      })
    },

    // 处理审核不通过
    handleProcessErr() {
      this.$refs['bookingErrForm'].validate((valid) => {
        if (valid) {
          // 调用接口
          labBookingApi.process(this.bookingErr).then((resp) => {
            this.bookingErrVisible = false
            // 提示
            this.$message.success('操作成功!')
            // 刷新页面
            this.getAll()
          })
        }
      })
    }
  }
}
</script>

<style></style>
