<template>
  <div class="register-page">
    <el-card class="register-card">
      <h2 style="text-align:center;margin-bottom:20px">注册</h2>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="0">
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="用户名" prefix-icon="User" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" type="password" placeholder="密码" prefix-icon="Lock" show-password />
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <el-input v-model="form.confirmPassword" type="password" placeholder="确认密码" prefix-icon="Lock" show-password />
        </el-form-item>
        <el-form-item prop="nickname">
          <el-input v-model="form.nickname" placeholder="昵称（可选）" prefix-icon="Edit" />
        </el-form-item>
        <el-form-item prop="phone">
          <el-input v-model="form.phone" placeholder="手机号（可选）" prefix-icon="Phone" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" style="width:100%" @click="handleRegister" :loading="loading">注册</el-button>
        </el-form-item>
        <div style="text-align:center">
          已有账号？<router-link to="/">立即登录</router-link>
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

const form = reactive({ username: '', password: '', confirmPassword: '', nickname: '', phone: '' })
const validateConfirm = (rule, value, callback) => {
  if (value !== form.password) callback(new Error('两次密码不一致'))
  else callback()
}
const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }, { min: 6, message: '至少6位', trigger: 'blur' }],
  confirmPassword: [{ required: true, message: '请确认密码', trigger: 'blur' }, { validator: validateConfirm, trigger: 'blur' }]
}

async function handleRegister() {
  await formRef.value.validate()
  loading.value = true
  try {
    await userStore.register({ username: form.username, password: form.password, nickname: form.nickname, phone: form.phone })
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.register-page { display: flex; justify-content: center; align-items: center; min-height: 100vh; background: #f5f7fa; }
.register-card { width: 400px; }
</style>
