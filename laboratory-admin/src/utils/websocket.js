import { getToken } from '@/utils/auth'

class WebSocketService {
  constructor() {
    this.ws = null
    this.isConnect = false
    this.url = process.env.VUE_APP_BASE_API.replace('http', 'ws') + '/ws/notifications'
    this.reconnectTimer = null
    this.reconnectCount = 0
    this.maxReconnectCount = 5
    this.reconnectInterval = 5000
    this.messageHandlers = []
  }

  // 连接WebSocket
  connect() {
    if (this.isConnect) return

    const token = getToken()
    if (!token) {
      console.error('用户未登录，无法连接WebSocket')
      return
    }

    try {
      this.ws = new WebSocket(`${this.url}?token=${token}`)

      this.ws.onopen = () => {
        console.log('WebSocket连接已建立')
        this.isConnect = true
        this.reconnectCount = 0 // 重置重连计数
      }

      this.ws.onmessage = (event) => {
        try {
          const data = JSON.parse(event.data)
          this.handleMessage(data)
        } catch (error) {
          console.error('解析WebSocket消息失败:', error)
        }
      }

      this.ws.onclose = () => {
        console.log('WebSocket连接已关闭')
        this.isConnect = false
        this.reconnect()
      }

      this.ws.onerror = (error) => {
        console.error('WebSocket连接发生错误:', error)
        this.isConnect = false
        this.reconnect()
      }
    } catch (error) {
      console.error('创建WebSocket连接失败:', error)
      this.reconnect()
    }
  }

  // 处理接收到的消息
  handleMessage(data) {
    // 触发所有消息处理器
    this.messageHandlers.forEach(handler => {
      handler(data)
    })
  }

  // 添加消息处理器
  addMessageHandler(handler) {
    if (typeof handler === 'function') {
      this.messageHandlers.push(handler)
    }
  }

  // 移除消息处理器
  removeMessageHandler(handler) {
    const index = this.messageHandlers.indexOf(handler)
    if (index > -1) {
      this.messageHandlers.splice(index, 1)
    }
  }

  // 重连
  reconnect() {
    if (this.reconnectCount < this.maxReconnectCount && !this.isConnect) {
      this.reconnectCount++
      console.log(`WebSocket重连中... (${this.reconnectCount}/${this.maxReconnectCount})`)
      
      this.reconnectTimer = setTimeout(() => {
        this.connect()
      }, this.reconnectInterval)
    } else if (this.reconnectCount >= this.maxReconnectCount) {
      console.error('WebSocket重连次数已达上限')
    }
  }

  // 关闭连接
  close() {
    if (this.reconnectTimer) {
      clearTimeout(this.reconnectTimer)
      this.reconnectTimer = null
    }
    
    if (this.ws) {
      this.ws.close()
      this.ws = null
    }
    
    this.isConnect = false
    console.log('WebSocket连接已关闭')
  }

  // 检查连接状态
  isConnected() {
    return this.isConnect && this.ws && this.ws.readyState === WebSocket.OPEN
  }
}

// 创建单例
const wsService = new WebSocketService()
export default wsService