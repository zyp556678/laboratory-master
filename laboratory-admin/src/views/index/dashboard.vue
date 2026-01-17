<template>
  <div class="dashboard-container">
    <el-row :gutter="20">
      <!-- 用户信息卡片 -->
      <el-col :xs="24" :sm="24" :md="12" :lg="8" :xl="8">
        <el-card class="welcome-card" shadow="hover">
          <div class="user-info">
            <el-avatar :src="avatar" :size="80" class="user-avatar" />
            <div class="user-details">
              <h2 class="greeting">{{ initTime }}尊敬的
                <span v-if="hasRoles(1)">管理员</span>
                <span v-if="hasRoles(2)">教师</span>
                <span v-if="hasRoles(3)">学生</span>
                : {{ name }}
              </h2>
              <p class="motivation">愿您的每一天都充满创新与发现！</p>
            </div>
          </div>
        </el-card>
      </el-col>

      <!-- 统计卡片 -->
      <el-col :xs="12" :sm="12" :md="6" :lg="4" :xl="4">
        <el-card class="statistic-card" shadow="hover" @click.native="$router.push('/lab')">
          <div class="statistic-content">
            <div class="statistic-icon bg-blue">
              <i class="el-icon-office-building"></i>
            </div>
            <div class="statistic-info">
              <p class="statistic-value">{{ labSubTotal }}</p>
              <p class="statistic-label">实验室总数</p>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :xs="12" :sm="12" :md="6" :lg="4" :xl="4">
        <el-card class="statistic-card" shadow="hover" @click.native="$router.push('/open')">
          <div class="statistic-content">
            <div class="statistic-icon bg-green">
              <i class="el-icon-office-building"></i>
            </div>
            <div class="statistic-info">
              <p class="statistic-value">{{ labSubFreeTotal }}</p>
              <p class="statistic-label">实验室空闲数</p>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col v-if="hasRoles(1, 2)" :xs="12" :sm="12" :md="6" :lg="4" :xl="4">
        <el-card class="statistic-card" shadow="hover" @click.native="$router.push('/device')">
          <div class="statistic-content">
            <div class="statistic-icon bg-purple">
              <i class="el-icon-monitor"></i>
            </div>
            <div class="statistic-info">
              <p class="statistic-value">{{ deviceTotal }}</p>
              <p class="statistic-label">设备总数</p>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col v-if="hasRoles(1, 2)" :xs="12" :sm="12" :md="6" :lg="4" :xl="4">
        <el-card class="statistic-card" shadow="hover" @click.native="$router.push('/device/repair')">
          <div class="statistic-content">
            <div class="statistic-icon bg-red">
              <i class="el-icon-document-delete"></i>
            </div>
            <div class="statistic-info">
              <p class="statistic-value">{{ deviceRepairTotal }}</p>
              <p class="statistic-label">设备维修数</p>
            </div>
          </div>
        </el-card>
      </el-col>

      <!-- 我的日程 -->
      <el-col v-if="hasRoles(2, 3)" :span="24">
        <el-card header="我的计划" class="calendar-card" shadow="hover">
          <el-calendar>
            <template slot="dateCell" slot-scope="{ date, data }">
              <div class="calendar-cell">
                <div class="calendar-date">{{ data.day.split('-').slice(1).join('-') }}</div>
                <div class="calendar-events">
                  <template v-for="(item, index) in getPlan(data.day)">
                    <div v-if="index < 2" :key="index" class="event-item">
                      <span class="event-tag">预约</span>
                      <span class="event-content">{{ item.labName }}-{{ item.timeSlot }}</span>
                    </div>
                    <div v-else-if="index === 2 && getPlan(data.day).length > 2" :key="index" class="event-more">
                      ...
                    </div>
                  </template>
                </div>
              </div>
            </template>
          </el-calendar>
        </el-card>
      </el-col>

      <!-- 预约消息 -->
      <el-col v-if="hasRoles(1)" :xs="24" :sm="24" :md="12" :lg="8" :xl="8">
        <el-card class="message-card" shadow="hover">
          <div slot="header" class="message-header">
            <span class="message-title">
              预约消息 <el-tag type="danger" size="small" effect="plain">{{ bookingData.total }}</el-tag>
            </span>
            <el-link type="primary" @click="navTo('/lab/booking')">前往处理</el-link>
          </div>
          <div class="booking-wrapper" ref="bookingWrapper">
            <el-table :data="bookingData.list" class="message-table">
              <el-table-column prop="username" label="申请人" show-overflow-tooltip></el-table-column>
              <el-table-column prop="createTime" label="申请时间" show-overflow-tooltip sortable></el-table-column>
              <el-table-column prop="labName" label="申请实验室" show-overflow-tooltip></el-table-column>
            </el-table>
          </div>
        </el-card>
      </el-col>

      <!-- 系统消息 -->
      <el-col v-if="hasRoles(1)" :xs="24" :sm="24" :md="12" :lg="8" :xl="8">
        <el-card class="message-card" shadow="hover">
          <div slot="header" class="message-header">
            <span class="message-title">
              系统消息
              <el-tag v-if="notificationCount > 0" type="danger" size="small" effect="plain">{{ notificationCount }}</el-tag>
            </span>
            <el-link type="primary" @click="navTo('/sys/log')">更多</el-link>
          </div>
          <div class="timeline-wrapper" ref="timelineWrapper">
            <el-timeline class="system-timeline">
              <el-timeline-item
                v-for="(item, index) in activities"
                :type="item.loginState == 1 ? 'success' : 'danger'"
                :key="index"
                :timestamp="item.loginTime"
                placement="top"
              >
                <el-card class="timeline-card">
                  <p>{{ item.username }} - {{ item.loginResult }} - {{ item.loginIp }}</p>
                </el-card>
              </el-timeline-item>
            </el-timeline>
          </div>
        </el-card>
      </el-col>

      <!-- 快捷导航 -->
      <el-col v-if="hasRoles(1)" :xs="24" :sm="24" :md="12" :lg="8" :xl="8">
        <el-card header="快捷导航" class="quick-nav-card" shadow="hover">
          <div class="nav-grid">
            <div
              v-for="item in navList"
              :key="item.title"
              class="nav-item"
              @click="navTo(item.path)"
            >
              <div class="nav-icon" :style="item.style">
                <i :class="item.icon"></i>
              </div>
              <div class="nav-title">{{ item.title }}</div>
            </div>
          </div>
        </el-card>
      </el-col>

      <!-- 图表区域 -->
      <el-col v-if="hasRoles(1, 2)" :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
        <el-card class="chart-card" shadow="hover">
          <div ref="lineChart" class="chart-container"></div>
        </el-card>
      </el-col>

      <el-col v-if="hasRoles(1, 2)" :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
        <el-card class="chart-card" shadow="hover">
          <div ref="barChart" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import * as echarts from 'echarts'
import deviceApi from '@/api/device'
import labSubApi from '@/api/lab/sub'
import { initTime } from '@/utils/index'
import logApi from '@/api/sys/login-log'
import labBookingApi from '@/api/lab/booking'
import wsService from '@/utils/websocket'

export default {
  name: 'Dashboard',
  computed: {
    ...mapGetters(['name', 'avatar', 'roleId'])
  },
  data() {
    return {
      initTime: initTime(),
      labSubTotal: 388,
      labSubFreeTotal: 268,
      deviceTotal: 299,
      deviceRepairTotal: 168,

      srcList: [
        'https://img0.baidu.com/it/u=2123036823,827931345&fm=253&fmt=auto&app=120&f=JPEG?w=889&h=500',
        'https://img0.baidu.com/it/u=3429084431,3824519370&fm=253&fmt=auto&app=138&f=JPEG?w=889&h=500'
      ],
      navList: [
        { icon: 'el-icon-setting', title: '系统管理', path: '/sys', style: { color: '#67C23A' } },
        { icon: 'el-icon-s-tools', title: '实验室管理', path: '/lab', style: { color: '#F56C6C' } },
        { icon: 'el-icon-document', title: '课程管理', path: '/course', style: { color: '#323232' } },
        { icon: 'el-icon-s-platform', title: '设备管理', path: '/device', style: { color: '#764755' } }
      ],
      activities: [],

      labSubData: [],
      deviceRepairData: [],

      bookingData: [],
      bookingScrollInterval: null, // 添加预约消息滚动定时器
      scrollInterval: null,
      scrollSpeed: 2000, // 滚动间隔时间(毫秒)
      // 实时通知相关
      notifications: [],
      notificationCount: 0
    }
  },
  mounted() {
    if (this.roleId != 3) {
      deviceApi.getRepairCount().then((res) => {
        this.initBarChart(res.data)
      })

      labSubApi.getWeekCount().then((res) => {
        this.initLineChart(res.data)
      })

      window.addEventListener('resize', this.handleResize)

      this.getSystemMsg()

      this.getBookingProcess(1, 60)
    }

    this.getStatistic()

    this.getBookingProcess(1, 100, null)

    // 初始化自动滚动
    this.$nextTick(() => {
      this.initAutoScroll();
      this.initBookingAutoScroll(); // 初始化预约消息自动滚动
    });

    // 初始化WebSocket连接
    this.initWebSocket();
  },
  beforeDestroy() {
    if (this.lineChart) {
      this.lineChart.dispose()
      this.lineChart = null
    }
    if (this.barChart) {
      this.barChart.dispose()
      this.barChart = null
    }

    // 清除定时器
    if (this.scrollInterval) {
      clearInterval(this.scrollInterval);
      this.scrollInterval = null;
    }

    // 清除预约消息滚动定时器
    if (this.bookingScrollInterval) {
      clearInterval(this.bookingScrollInterval);
      this.bookingScrollInterval = null;
    }

    // 断开WebSocket连接
    wsService.close();
  },
  methods: {
    // 初始化WebSocket连接
    initWebSocket() {
      // 添加消息处理器
      wsService.addMessageHandler(this.handleNotification);

      // 连接WebSocket
      wsService.connect();
    },

    // 处理WebSocket通知消息
    handleNotification(data) {
      // 根据消息类型处理不同通知
      switch(data.type) {
        case 'booking':
          this.handleBookingNotification(data);
          break;
        case 'device_repair':
          this.handleDeviceRepairNotification(data);
          break;
        case 'system':
          this.handleSystemNotification(data);
          break;
        case 'operation_log':
          this.handleOperationLogNotification(data);
          break;
        default:
          console.log('收到未知类型通知:', data);
      }

      // 更新通知计数
      this.notificationCount++;

      // 在控制台显示通知（可选）
      console.log('收到通知:', data);
    },

    // 处理预约通知
    handleBookingNotification(data) {
      this.$notify({
        title: '预约通知',
        message: data.content,
        type: 'info',
        duration: 5000
      });

      // 更新预约数据（重新获取）
      this.getBookingProcess(1, 100, null);
    },

    // 处理设备维修通知
    handleDeviceRepairNotification(data) {
      this.$notify({
        title: '设备维修通知',
        message: data.content,
        type: 'warning',
        duration: 5000
      });

      // 更新设备维修统计数据
      deviceApi.getRepairTotal().then((res) => {
        this.deviceRepairTotal = res.data;
      });
    },

    // 处理系统通知
    handleSystemNotification(data) {
      this.$notify({
        title: '系统通知',
        message: data.content,
        type: 'success',
        duration: 5000
      });

      // 重新获取系统消息
      this.getSystemMsg();
    },

    // 处理操作日志通知
    handleOperationLogNotification(data) {
      this.$notify({
        title: '操作日志',
        message: data.content,
        type: 'info',
        duration: 5000
      });
    },

    // 获取日程计划
    getPlan(day) {
      // 确保 bookingData.list 存在且为数组
      if (!this.bookingData || !Array.isArray(this.bookingData.list)) {
        return ''
      }

      // 查找所有匹配日期的预约记录
      return this.bookingData.list.filter((item) => item.bookingDate === day)
    },
    // 获取预约信息
    getBookingProcess(pIndex, pSize, bookingStatus) {
      labBookingApi.getPage(pIndex, pSize, { bookingStatus }).then((res) => {
        this.bookingData = res.data
      })
    },
    // 获取系统消息
    getSystemMsg() {
      logApi.getPage(1, 6, {}).then((res) => {
        this.activities = res.data.list
      })
    },
    // 获取统计数据
    getStatistic() {
      labSubApi.getTotal().then((res) => {
        this.labSubTotal = res.data
      })

      labSubApi.getFreeTotal().then((res) => {
        this.labSubFreeTotal = res.data
      })

      if (this.roleId != 3) {
        deviceApi.getTotal().then((res) => {
          this.deviceTotal = res.data
        })

        deviceApi.getRepairTotal().then((res) => {
          this.deviceRepairTotal = res.data
        })
      }
    },
    // 页面跳转
    navTo(path) {
      this.$router.push(path)
    },

    initLineChart(data) {
      this.lineChart = echarts.init(this.$refs.lineChart)
      const lineOptions = {
        title: {
          text: '近一周实验室预约率',
          textStyle: {
            fontSize: 14,
            fontWeight: 'normal'
          }
        },
        tooltip: {
          trigger: 'axis'
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        toolbox: {
          show: true,
          feature: {
            dataZoom: {
              yAxisIndex: 'none'
            },
            dataView: { readOnly: false },
            magicType: { type: ['bar', 'line'] },
            restore: {},
            saveAsImage: {}
          }
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
        },
        yAxis: {},
        series: [
          {
            name: '预约数',
            type: 'line',
            data: data,
            smooth: true
          }
        ]
      }
      this.lineChart.setOption(lineOptions)
    },
    initBarChart(data) {
      this.barChart = echarts.init(this.$refs.barChart)
      const barOptions = {
        title: {
          text: '近一周设备报修率',
          textStyle: {
            fontSize: 14,
            fontWeight: 'normal'
          }
        },
        tooltip: {
          trigger: 'axis'
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        toolbox: {
          show: true,
          feature: {
            dataZoom: {
              yAxisIndex: 'none'
            },
            dataView: { readOnly: false },
            magicType: { type: ['line', 'bar'] },
            restore: {},
            saveAsImage: {}
          }
        },
        xAxis: {
          type: 'category',
          data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
        },
        yAxis: {},
        series: [
          {
            name: '报修数',
            type: 'bar',
            data: data,
            barWidth: '60%'
          }
        ]
      }
      this.barChart.setOption(barOptions)
    },
    // 调整图表大小
    handleResize() {
      if (this.lineChart) {
        this.lineChart.resize()
      }

      if (this.barChart) {
        this.barChart.resize()
      }
    },

    // 初始化自动滚动
    initAutoScroll() {
      this.$nextTick(() => {
        const timelineWrapper = this.$refs.timelineWrapper;
        if (!timelineWrapper) return;

        // 只有当内容超过容器高度时才启动自动滚动
        if (timelineWrapper.scrollHeight > timelineWrapper.clientHeight) {
          this.startAutoScroll();
        }
      });
    },

    // 开始自动滚动
    startAutoScroll() {
      const timelineWrapper = this.$refs.timelineWrapper;
      if (!timelineWrapper) return;

      // 清除已存在的定时器
      if (this.scrollInterval) {
        clearInterval(this.scrollInterval);
      }

      // 设置新的定时器
      this.scrollInterval = setInterval(() => {
        // 如果已经滚动到底部，则回到顶部
        if (timelineWrapper.scrollTop + timelineWrapper.clientHeight >= timelineWrapper.scrollHeight - 5) {
          timelineWrapper.scrollTo({ top: 0, behavior: 'smooth' });
        } else {
          // 否则向下滚动一项的高度
          const firstItem = timelineWrapper.querySelector('.el-timeline-item');
          if (firstItem) {
            timelineWrapper.scrollBy({
              top: firstItem.offsetHeight + 20,
              behavior: 'smooth'
            });
          }
        }
      }, this.scrollSpeed);

      // 鼠标悬停时暂停滚动
      timelineWrapper.addEventListener('mouseenter', () => {
        if (this.scrollInterval) {
          clearInterval(this.scrollInterval);
          this.scrollInterval = null;
        }
      });

      // 鼠标离开时恢复滚动
      timelineWrapper.addEventListener('mouseleave', () => {
        if (!this.scrollInterval) {
          this.scrollInterval = setInterval(() => {
            if (timelineWrapper.scrollTop + timelineWrapper.clientHeight >= timelineWrapper.scrollHeight - 5) {
              timelineWrapper.scrollTo({ top: 0, behavior: 'smooth' });
            } else {
              const firstItem = timelineWrapper.querySelector('.el-timeline-item');
              if (firstItem) {
                timelineWrapper.scrollBy({
                  top: firstItem.offsetHeight + 20,
                  behavior: 'smooth'
                });
              }
            }
          }, this.scrollSpeed);
        }
      });
    },

    // 初始化预约消息自动滚动
    initBookingAutoScroll() {
      this.$nextTick(() => {
        const bookingWrapper = this.$refs.bookingWrapper;
        if (!bookingWrapper) return;

        // 只有当内容超过容器高度时才启动自动滚动
        if (bookingWrapper.scrollHeight > bookingWrapper.clientHeight) {
          this.startBookingAutoScroll();
        }
      });
    },

    // 开始预约消息自动滚动
    startBookingAutoScroll() {
      const bookingWrapper = this.$refs.bookingWrapper;
      if (!bookingWrapper) return;

      // 清除已存在的定时器
      if (this.bookingScrollInterval) {
        clearInterval(this.bookingScrollInterval);
      }

      // 设置新的定时器
      this.bookingScrollInterval = setInterval(() => {
        // 如果已经滚动到底部，则回到顶部
        if (bookingWrapper.scrollTop + bookingWrapper.clientHeight >= bookingWrapper.scrollHeight - 5) {
          bookingWrapper.scrollTo({ top: 0, behavior: 'smooth' });
        } else {
          // 否则向下滚动一项的高度
          const firstItem = bookingWrapper.querySelector('.el-table__row');
          if (firstItem) {
            bookingWrapper.scrollBy({
              top: firstItem.offsetHeight,
              behavior: 'smooth'
            });
          }
        }
      }, this.scrollSpeed);

      // 鼠标悬停时暂停滚动
      bookingWrapper.addEventListener('mouseenter', () => {
        if (this.bookingScrollInterval) {
          clearInterval(this.bookingScrollInterval);
          this.bookingScrollInterval = null;
        }
      });

      // 鼠标离开时恢复滚动
      bookingWrapper.addEventListener('mouseleave', () => {
        if (!this.bookingScrollInterval) {
          this.bookingScrollInterval = setInterval(() => {
            if (bookingWrapper.scrollTop + bookingWrapper.clientHeight >= bookingWrapper.scrollHeight - 5) {
              bookingWrapper.scrollTo({ top: 0, behavior: 'smooth' });
            } else {
              const firstItem = bookingWrapper.querySelector('.el-table__row');
              if (firstItem) {
                bookingWrapper.scrollBy({
                  top: firstItem.offsetHeight,
                  behavior: 'smooth'
                });
              }
            }
          }, this.scrollSpeed);
        }
      });
    }
  }
}
</script>

<style lang="scss" scoped>
.dashboard-container {
  padding: 20px;

  ::v-deep .el-card {
    border-radius: 8px;
    margin-bottom: 20px;

    .el-card__header {
      font-weight: 600;
      padding: 15px 20px;
      border-bottom: 1px solid #eee;
    }
  }

  ::v-deep .el-col {
    margin-bottom: 0;
  }
}

// 欢迎卡片样式
.welcome-card {
  height: 140px;

  .user-info {
    display: flex;
    align-items: center;
    height: 100%;

    .user-avatar {
      flex-shrink: 0;
      margin-right: 20px;
    }

    .user-details {
      flex: 1;
      overflow: hidden;

      .greeting {
        font-size: 16px;
        margin: 0 0 10px 0;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;

        span {
          font-weight: bold;
        }
      }

      .motivation {
        color: #909399;
        margin: 0;
        font-size: 14px;
      }
    }
  }
}

// 统计卡片样式
.statistic-card {
  cursor: pointer;
  transition: all 0.3s ease;
  height: 140px;

  &:hover {
    transform: translateY(-4px);
    box-shadow: 0 6px 16px rgba(0, 0, 0, 0.15);
  }

  .statistic-content {
    display: flex;
    height: 100%;
    align-items: center;

    .statistic-icon {
      width: 60px;
      height: 60px;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 15px;
      flex-shrink: 0;

      i {
        font-size: 24px;
        color: white;
      }

      &.bg-blue {
        background: linear-gradient(135deg, #409EFF, #1a73e8);
      }

      &.bg-green {
        background: linear-gradient(135deg, #67C23A, #409814);
      }

      &.bg-purple {
        background: linear-gradient(135deg, #9B59B6, #8E44AD);
      }

      &.bg-red {
        background: linear-gradient(135deg, #F56C6C, #e74c3c);
      }
    }

    .statistic-info {
      .statistic-value {
        font-size: 24px;
        font-weight: bold;
        margin: 0 0 5px 0;
      }

      .statistic-label {
        margin: 0;
        color: #909399;
        font-size: 14px;
      }
    }
  }
}

// 日历卡片样式
.calendar-card {
  ::v-deep .el-calendar {
    .el-calendar__body {
      padding: 10px;
    }

    .el-calendar-table {
      .el-calendar-day {
        height: auto;
      }
    }
  }
}

.calendar-cell {
  height: 80px;
  padding: 4px;
  overflow-y: auto;

  .calendar-date {
    font-size: 12px;
    margin-bottom: 2px;
  }

  .calendar-events {
    .event-item {
      display: flex;
      align-items: center;
      margin-bottom: 2px;
      font-size: 12px;

      .event-tag {
        background-color: #67C23A;
        color: white;
        padding: 1px 3px;
        border-radius: 2px;
        margin-right: 3px;
        font-size: 10px;
      }

      .event-content {
        flex: 1;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
      }
    }

    .event-more {
      font-size: 12px;
      color: #909399;
    }
  }
}

// 消息卡片样式
.message-card {
  .message-header {
    display: flex;
    justify-content: space-between;
    align-items: center;

    .message-title {
      flex: 1;
    }
  }

  .timeline-wrapper {
    height: 215px;
    overflow-y: auto;
    padding-right: 10px;

    // 隐藏滚动条（针对不同浏览器）
    &::-webkit-scrollbar {
      width: 6px;
    }

    &::-webkit-scrollbar-thumb {
      background-color: #c0c4cc;
      border-radius: 3px;
    }

    &::-webkit-scrollbar-track {
      background-color: #f5f7fa;
      border-radius: 3px;
    }
  }

  .booking-wrapper {
    height: 208px;
    overflow-y: auto;
    padding-right: 10px;

    // 隐藏滚动条（针对不同浏览器）
    &::-webkit-scrollbar {
      width: 6px;
    }

    &::-webkit-scrollbar-thumb {
      background-color: #c0c4cc;
      border-radius: 3px;
    }

    &::-webkit-scrollbar-track {
      background-color: #f5f7fa;
      border-radius: 3px;
    }
  }

  .message-table {
    ::v-deep .el-table__row {
      cursor: pointer;
    }
  }

  .system-timeline {
    ::v-deep .el-timeline-item__content {
      margin-bottom: 10px;
    }
  }
}

.timeline-card {
  background-color: #f5f7fa;
  border-radius: 4px;
  padding: 10px;
  font-size: 12px;

  p {
    margin: 0;
  }
}

// 快捷导航卡片样式
.quick-nav-card {
  .nav-grid {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 15px;
  }

  .nav-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 15px 0;
    cursor: pointer;
    border-radius: 6px;
    transition: all 0.3s;

    &:hover {
      background-color: #f5f7fa;
      transform: translateY(-2px);
    }

    .nav-icon {
      width: 40px;
      height: 40px;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-bottom: 8px;
      font-size: 20px;
    }

    .nav-title {
      font-size: 14px;
    }
  }
}

// 图表卡片样式
.chart-card {
  .chart-container {
    width: 100%;
    height: 380px;
  }
}

// 响应式调整
@media screen and (max-width: 768px) {
  .dashboard-container {
    padding: 10px;
  }

  .welcome-card {
    height: auto;
    padding: 15px;

    .user-info {
      flex-direction: column;
      text-align: center;

      .user-avatar {
        margin-right: 0;
        margin-bottom: 10px;
      }
    }
  }

  .statistic-card {
    height: 100px;
  }

  .chart-container {
    height: 300px;
  }

  .nav-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}
</style>
