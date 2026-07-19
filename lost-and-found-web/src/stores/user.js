import { defineStore } from 'pinia'
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import request from '@/utils/axios'
import router from '@/router'

export const useUserStore = defineStore('user', () => {
  const token = ref(sessionStorage.getItem('token') || '')
  const userInfo = ref(null)

  function setToken(newToken) {
    token.value = newToken
    sessionStorage.setItem('token', newToken)
  }

  async function login(loginData) {
    // Clear old token first to avoid stale state
    sessionStorage.removeItem('token')
    token.value = ''
    userInfo.value = null
    const res = await request.post('/user/login', loginData)
    setToken(res.data)
    await getInfo()
    ElMessage.success('登录成功')
    router.push('/home')
  }

  async function register(registerData) {
    await request.post('/user/register', registerData)
    ElMessage.success('注册成功')
    router.push('/')
  }

  async function getInfo() {
    const res = await request.get('/user/info')
    userInfo.value = res.data
  }

  function logout() {
    token.value = ''
    userInfo.value = null
    sessionStorage.removeItem('token')
    router.push('/')
  }

  return { token, userInfo, setToken, login, register, getInfo, logout }
})
