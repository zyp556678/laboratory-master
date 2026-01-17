import Layout from '@/layout'

// 管理员权限
const adminPer = [
  {
    path: '/sys',
    component: Layout,
    redirect: '/sys/user',
    meta: {
      title: '系统管理',
      icon: 'el-icon-setting'
    },
    children: [
      {
        path: 'user',
        name: 'user',
        component: () => import('@/views/sys/user/index'),
        meta: {
          title: '用户管理',
          icon: 'user'
        }
      },
      {
        path: 'log',
        component: () => import('@/views/sys/log/index'),
        meta: {
          title: '日志管理',
          icon: 'el-icon-info'
        }
      }
    ]
  },

  {
    path: '/lab',
    component: Layout,
    redirect: '/lab',
    meta: {
      title: '实验室管理',
      icon: 'el-icon-office-building'
    },
    children: [
      {
        path: '',
        component: () => import('@/views/lab/sub/index'),
        meta: {
          title: '实验室管理',
          icon: 'el-icon-school'
        }
      },
      {
        path: 'booking',
        component: () => import('@/views/lab/booking/index'),
        meta: {
          title: '实验室预约管理',
          icon: 'el-icon-date'
        }
      },
      {
        path: 'open',
        component: () => import('@/views/lab/open/index'),
        meta: {
          title: '实验室开放管理',
          icon: 'el-icon-full-screen'
        }
      },
      {
        path: 'person',
        component: () => import('@/views/lab/person/index'),
        meta: {
          title: '实验室人员管理',
          icon: 'el-icon-s-custom'
        }
      }
    ]
  },

  {
    path: '/course',
    component: Layout,
    meta: {
      title: '课程管理',
      icon: 'form'
    },
    children: [
      {
        path: '',
        component: () => import('@/views/course/index.vue'),
        meta: {
          title: '课程管理',
          icon: 'table'
        }
      },
      {
        path: 'project',
        component: () => import('@/views/course/project/index'),
        meta: {
          title: '课程项目管理',
          icon: 'el-icon-document'
        }
      }
    ]
  },

  {
    path: '/device',
    component: Layout,
    redirect: '',
    meta: {
      title: '设备管理',
      icon: 'el-icon-s-platform'
    },
    children: [
      {
        path: '',
        component: () => import('@/views/device/index.vue'),
        meta: {
          title: '设备管理',
          icon: 'el-icon-monitor'
        }
      },
      {
        path: 'repair',
        component: () => import('@/views/device/repair/index'),
        meta: {
          title: '设备报修管理',
          icon: 'el-icon-document-delete'
        }
      }
    ]
  }
]

// 教师权限
const teacherPer = [
  {
    path: '/lab',
    component: Layout,
    children: [
      {
        path: '',
        component: () => import('@/views/lab/sub/index'),
        meta: {
          title: '实验室查看',
          icon: 'el-icon-school'
        }
      }
    ]
  },
  {
    path: '/open',
    component: Layout,
    children: [
      {
        path: 'open',
        component: () => import('@/views/lab/open/index'),
        meta: {
          title: '实验室开放查看',
          icon: 'el-icon-full-screen'
        }
      }
    ]
  },
  {
    path: '/lab-booking',
    component: Layout,
    children: [
      {
        path: '',
        component: () => import('@/views/lab/booking/index'),
        meta: {
          title: '我的预约',
          icon: 'el-icon-date'
        }
      }
    ]
  },
  {
    path: '/course',
    component: Layout,
    children: [
      {
        path: '',
        component: () => import('@/views/course/index.vue'),
        meta: {
          title: '课程管理',
          icon: 'table'
        }
      }
    ]
  },
  {
    path: '/project',
    component: Layout,
    children: [
      {
        path: '',
        component: () => import('@/views/course/project/index'),
        meta: {
          title: '课程项目管理',
          icon: 'el-icon-document'
        }
      }
    ]
  },

  {
    path: '/device',
    component: Layout,
    children: [
      {
        path: 'repair',
        component: () => import('@/views/device/repair/index'),
        meta: {
          title: '设备报修记录',
          icon: 'el-icon-document-delete'
        }
      }
    ]
  }
]

// 学生权限
const studentPer = [
  {
    path: '/lab',
    component: Layout,
    children: [
      {
        path: '',
        component: () => import('@/views/lab/sub/index'),
        meta: {
          title: '实验室查看',
          icon: 'el-icon-school'
        }
      }
    ]
  },
  {
    path: '/open',
    component: Layout,
    children: [
      {
        path: '',
        component: () => import('@/views/lab/open/index'),
        meta: {
          title: '实验室开放查看',
          icon: 'el-icon-full-screen'
        }
      }
    ]
  },
  {
    path: '/lab-booking',
    component: Layout,
    children: [
      {
        path: '',
        component: () => import('@/views/lab/booking/index'),
        meta: {
          title: '我的预约',
          icon: 'el-icon-date'
        }
      }
    ]
  },
  {
    path: '/course',
    component: Layout,
    children: [
      {
        path: '',
        component: () => import('@/views/course/index.vue'),
        meta: {
          title: '课程查看',
          icon: 'table'
        }
      }
    ]
  },
  {
    path: '/device',
    component: Layout,
    children: [
      {
        path: '',
        component: () => import('@/views/device/repair/edit'),
        meta: {
          title: '设备报修',
          icon: 'el-icon-monitor'
        }
      }
    ]
  }
]

/**
 *根据角色ID获取用户权限
 * @param {Number} roleId 角色ID
 * @returns
 */
export function getPermission(roleId) {
  let menus = []

  if (roleId === 1) {
    menus = adminPer
  } else if (roleId === 2) {
    menus = teacherPer
  } else if (roleId === 3) {
    menus = studentPer
  }

  return menus
}
