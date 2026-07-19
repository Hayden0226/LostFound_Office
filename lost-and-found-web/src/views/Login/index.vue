<template>
  <div class="login-page">
    <el-card class="login-card">
      <h2 style="text-align:center;margin-bottom:20px">登录</h2>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="0">
        <el-form-item prop="username">
          <el-input ref="usernameRef" v-model="form.username" placeholder="用户名" prefix-icon="User" @keydown.down="focusPassword" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" type="password" placeholder="密码" prefix-icon="Lock" show-password @keyup.enter="handleLogin"  />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" style="width:100%" @click="handleLogin" :loading="loading">登录</el-button>
        </el-form-item>
        <div style="text-align:center">
          还没有账号？<router-link to="/register">立即注册</router-link>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useUserStore } from '@/stores/user'
const userStore = useUserStore()
const formRef = ref(null)
const loading = ref(false)
const usernameRef = ref(null)
const passwordRef = ref(null)

const form = reactive({ username: '', password: '' })
const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

function focusPassword() { passwordRef.value.focus() }
function focusUsername() { usernameRef.value.focus() }

async function handleLogin() {
  await formRef.value.validate()
  loading.value = true
  try {
    await userStore.login(form)
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-page { display: flex; justify-content: center; align-items: center; min-height: 100vh; background: #f5f7fa; }
.login-card { width: 400px; }
</style>
