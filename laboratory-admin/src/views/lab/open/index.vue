<template>
  <div class="app-container">
    <!--条件搜索表单-->
    <el-row>
      <el-form :inline="true">
        <el-form-item>
          <el-input v-model="searchObj.name" placeholder="负责人姓名" />
        </el-form-item>
        <el-form-item>
          <el-input v-model="searchObj.phone" placeholder="负责人电话" />
        </el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="getAll()">查询</el-button>
        <el-button type="default" icon="el-icon-refresh" @click="reset()">清空</el-button>
      </el-form>
    </el-row>

    <el-row>
      <el-button v-if="hasRoles(1)" type="primary" @click="handleSync()">同步开放信息</el-button>
    </el-row>

    <el-row>
      <el-table
        :data="data.list"
        :border="false"
        v-loading="loading"
        element-loading-text="正在努力加载数据中..."
        stripe
        border
        style="width: 100%; margin: 15px 0px"
      >
        <el-table-column type="index" fixed="left" width="80" label="序号" />
        <el-table-column prop="labName" fixed="left" label="实验室名称" min-width="120" show-overflow-tooltip />
        <el-table-column prop="openDate" label="开放周期" min-width="120" show-overflow-tooltip>
          <template v-slot="{ row }">
            <template v-for="item in openDateOptions">
              {{ item.value == row.openDate.split('-')[0] ? item.label + ' 至' : '' }}
              {{ item.value == row.openDate.split('-')[1] ? item.label : '' }}
            </template>
          </template>
        </el-table-column>
        <el-table-column prop="openTime" label="开放时间" min-width="120" show-overflow-tooltip />
        <el-table-column prop="name" label="负责人姓名" min-width="100" show-overflow-tooltip />
        <el-table-column prop="sex" label="负责人性别" min-width="100" show-overflow-tooltip>
          <template v-slot="{ row }">
            <template v-for="item in sexOptions">
              <el-tag v-if="row.sex == item.value" :type="item.style" effect="plain">{{ item.label }}</el-tag>
            </template>
          </template>
        </el-table-column>
        <el-table-column prop="phone" label="负责人电话" min-width="120" show-overflow-tooltip sortable />
        <el-table-column prop="createAt" label="创建人" show-overflow-tooltip />
        <el-table-column prop="createTime" label="创建时间" min-width="120" show-overflow-tooltip sortable />
        <el-table-column prop="updateAt" label="最后修改人" min-width="100" show-overflow-tooltip />
        <el-table-column prop="updateTime" label="最后修改时间" min-width="140" show-overflow-tooltip sortable />

        <!-- 操作 -->
        <el-table-column label="操作" v-if="hasRoles(1)" fixed="right" min-width="180" align="center">
          <template v-slot="{ row }">
            <el-link type="success" icon="el-icon-edit" size="mini" @click="openEdit(row.id)">编辑</el-link>
            <el-divider direction="vertical" />
            <el-link icon="el-icon-refresh" size="mini" @click="resetOpen(row.id)">恢复默认</el-link>
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
        :total="data.total"
      >
      </el-pagination>
    </el-row>

    <!-- 表单 -->
    <el-dialog :title="dialogTitle" :visible.sync="showEdit" :before-close="close">
      <el-form ref="openForm" :model="open" :rules="rules" label-width="100px">
        <el-row :gutter="10">
          <el-col :xs="24" :sm="24" :md="24" :lg="12" :xl="8">
            <el-form-item label="ID" prop="id" :hidden="true">
              <el-input v-model="open.id" disabled />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :md="24" :lg="12" :xl="12">
            <el-form-item label="所属实验室:" prop="labId">
              <el-select v-model="open.labId" placeholder="请选择实验分室" style="width: 100%" disabled>
                <el-option v-for="item in labSub" :key="item.id" :label="item.labName" :value="item.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :md="24" :lg="12" :xl="12">
            <el-form-item label="负责人姓名:" prop="name">
              <el-input v-model.trim="open.name" placeholder="请输入负责人姓名" />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :md="24" :lg="12" :xl="12">
            <el-form-item label="负责人性别">
              <el-select v-model="open.sex" style="width: 100%" placeholder="请选择负责人性别">
                <el-option v-for="item in sexOptions" :value="item.value" :label="item.label"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :md="24" :lg="12" :xl="12">
            <el-form-item label="负责人电话:" prop="phone">
              <el-input v-model.trim="open.phone" placeholder="请输入负责人电话" />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :md="24" :lg="12" :xl="12">
            <el-form-item label="开放周期:" prop="openDate" required>
              <el-select
                v-model="openDate.startDate"
                placeholder="开始周期"
                style="width: 45%"
                filterable
                @change="handleOpenDateChange"
              >
                <el-option v-for="item in openDateOptions" :value="item.value" :label="item.label"></el-option>
              </el-select>
              至
              <el-select v-model="openDate.endDate" placeholder="结束周期" style="width: 45%" filterable>
                <el-option
                  v-for="item in openDateOptions"
                  :value="item.value"
                  :label="item.label"
                  :disabled="openDate.startDate > item.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :md="24" :lg="12" :xl="12">
            <el-form-item label="开放时间:" prop="openTime" required>
              <el-time-select
                style="width: 45%"
                placeholder="起始时间"
                v-model="openTime.startTime"
                :picker-options="{
                  start: '06:30',
                  step: '00:30',
                  end: '22:30'
                }"
                :editable="false"
                :clearable="false"
              >
              </el-time-select>
              至
              <el-time-select
                style="width: 45%"
                placeholder="结束时间"
                v-model="openTime.endTime"
                :picker-options="{
                  start: '06:30',
                  step: '00:30',
                  end: '22:30',
                  minTime: openTime.startTime
                }"
                :editable="false"
                :clearable="false"
              >
              </el-time-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="close">关 闭</el-button>
        <el-button @click="resetForm">重 置</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
//引入api接口定义的js文件
import openApi from '@/api/lab/open'
import labSubApi from '@/api/lab/sub'

export default {
  data() {
    // 定义变量和初始值
    return {
      sexOptions: [
        { value: '保密', label: '保密', style: 'info' },
        { value: '男', label: '男', style: 'success' },
        { value: '女', label: '女', style: 'danger' }
      ],
      openDateOptions: [
        { value: '1', label: '周一' },
        { value: '2', label: '周二' },
        { value: '3', label: '周三' },
        { value: '4', label: '周四' },
        { value: '5', label: '周五' },
        { value: '6', label: '周六' },
        { value: '7', label: '周日' }
      ],

      pIndex: 1, // 当前页
      pSize: 6, // 每页显示记录数
      searchObj: {}, // 条件封装对象
      data: {}, // 数据
      loading: false,
      ids: [],
      showEdit: false,
      dialogTitle: '',

      // 定义表单绑定对象
      openDate: {
        startDate: '1',
        endDate: '5'
      },
      openTime: {
        startTime: '08:30',
        endTime: '17:30'
      },

      open: {
        id: undefined,
        name: '',
        sex: '保密',
        phone: '',
        openDate: '',
        openTime: ''
      },
      labSub: [],
      isEdit: false,
      rules: {
        name: [{ required: true, message: '负责人姓名不能为空', trigger: 'blur' }],
        phone: [
          { required: true, message: '负责人电话不能为空', trigger: 'blur' },
          {
            pattern: /^1[3456789]\d{9}$/,
            message: '手机号码格式错误',
            trigger: ['blur', 'change']
          }
        ]
      }
    }
  },
  created() {
    this.getAll()
    this.getSub()
  },
  methods: {
    // 处理开放周期改变
    handleOpenDateChange(val) {
      if (this.openDate.endDate < val) {
        this.openDate.endDate = val
      }
    },

    // 查询所有分室信息
    getSub() {
      labSubApi.getList().then((response) => {
        this.labSub = response.data
      })
    },

    //实验室开放信息
    getAll(current = 1) {
      this.loading = true
      this.pIndex = current
      openApi
        .getPage(this.pIndex, this.pSize, this.searchObj)
        .then((response) => {
          this.data = response.data
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

    // 同步实验室开放信息
    handleSync() {
      this.$confirm('此操作将同步实验室开放信息, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          const loading = this.$loading({
            lock: true,
            text: '正在同步中...',
            spinner: 'el-icon-loading',
            background: 'rgba(0, 0, 0, 0.7)'
          })
          // 调用接口
          openApi
            .syncOpen()
            .then((resp) => {
              this.$message.success('同步成功!')
              // 刷新页面
              this.getAll()
            })
            .finally(() => {
              loading.close()
            })
        })
        .catch(() => {})
    },

    // 打开表单
    openEdit(id) {
      this.showEdit = true
      this.dialogTitle = '编辑实验室开放信息'
      this.getById(id)
    },

    // 恢复默认
    resetOpen(id) {
      this.$confirm('此操作将重置实验室开放信息, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          // 调用接口
          openApi.reset(id).then(() => {
            this.$message.success('重置成功!')
            // 刷新页面
            this.getAll()
          })
        })
        .catch(() => {})
    },

    // 清空
    reset() {
      this.searchObj = {}
      this.getAll()
    },

    // 根据id获取
    getById(id) {
      openApi.getById(id).then((res) => {
        this.open = res.data

        this.openDate.startDate = this.open.openDate.split('-')[0]
        this.openDate.endDate = this.open.openDate.split('-')[1]
        this.openTime.startTime = this.open.openTime.split('-')[0]
        this.openTime.endTime = this.open.openTime.split('-')[1]
      })
    },

    // 关闭表单
    close() {
      this.showEdit = false
      this.$refs['openForm'].resetFields()
    },

    // 重置表单
    resetForm() {
      if (this.open.id) {
        this.getById(this.open.id)
      }
      this.$refs['openForm'].resetFields()
    },

    // 添加
    save() {
      this.$refs['openForm'].validate((valid) => {
        if (valid) {
          this.open.openDate = this.openDate.startDate + '-' + this.openDate.endDate
          this.open.openTime = this.openTime.startTime + '-' + this.openTime.endTime

          openApi.update(this.open).then((res) => {
            this.$message.success(res.message)
            this.close()
            this.getAll()
          })
        }
      })
    }
  }
}
</script>

<style></style>
