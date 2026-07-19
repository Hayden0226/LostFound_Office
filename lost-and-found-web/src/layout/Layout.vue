<template>
  <div class="layout">
    <el-header class="header">
      <div class="header-inner">
        <router-link to="/home" class="logo">
<span class="logo-text">Lost &amp; Found</span>
          </router-link>
        <div class="nav">
          <div class="nav-indicator" :style="navIndicatorStyle" />
          <a ref="navHome" href="/home" class="nav-item" :class="{ active: activeNav == 'home' }"
             @mouseenter="hoveredNav = 'home'" @mouseleave="hoveredNav = null"
             @click.prevent="router.push('/home')">首页</a>
          <a ref="navLost" href="/home/lost" class="nav-item" :class="{ active: activeNav == 'lost' }"
             @mouseenter="hoveredNav = 'lost'" @mouseleave="hoveredNav = null"
             @click.prevent="router.push('/home/lost')">宝贝回家</a>
          <a ref="navFound" href="/home/found" class="nav-item" :class="{ active: activeNav == 'found' }"
             @mouseenter="hoveredNav = 'found'" @mouseleave="hoveredNav = null"
             @click.prevent="router.push('/home/found')">失物招领</a>
          <div ref="navUser" class="nav-user-wrap" v-if="userStore.token" @mouseenter="hoveredNav = 'user'" @mouseleave="hoveredNav = null">
          <el-dropdown trigger="click" @visible-change="onDropdownChange">
            <span class="user-btn">{{ userStore.userInfo?.nickname || '用户' }}</span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="$router.push('/home/profile')">个人中心</el-dropdown-item>
                <el-dropdown-item @click="$router.push('/home/publish')">发布宝贝回家</el-dropdown-item>
                <el-dropdown-item @click="$router.push('/home/publish-found')">发布失物招领</el-dropdown-item>
                <el-dropdown-item v-if="userStore.userInfo?.role === 'admin'" @click="$router.push('/admin')">管理后台</el-dropdown-item>
                <el-dropdown-item divided @click="userStore.logout()">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
          </div>
          <template v-else>
            <router-link to="/login" class="nav-item">登录</router-link>
            <router-link to="/register" class="nav-item">注册</router-link>
          </template>
        </div>
      </div>
    </el-header>
    <el-main class="main">
      <router-view />
    </el-main>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, nextTick, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

// Nav indicator
const navHome = ref(null)
const navLost = ref(null)
const navFound = ref(null)
const navUser = ref(null)
const hoveredNav = ref(null)
const dropdownOpen = ref(false)
const navReady = ref(false)
const navIndicatorStyle = ref({ opacity: 0, width: '0px', left: '0px' })

const activeNav = computed(() => {
  const path = route.path
  if (path === '/home') return 'home'
  if (path.startsWith('/home/lost')) return 'lost'
  if (path.startsWith('/home/found')) return 'found'
  return 'home'
})

function getNavIndicatorStyle() {
  if (!navReady.value) return { opacity: 0, width: '0px', left: '0px' }
  const target = (dropdownOpen.value ? 'user' : null) || hoveredNav.value || activeNav.value
  const el = ({ home: navHome.value, lost: navLost.value, found: navFound.value, user: navUser.value })[target]
  if (!el) return { opacity: 0, width: '0px', left: '0px' }
  const btnWidth = el.offsetWidth
  const indWidth = btnWidth * 0.6
  return {
    left: (el.offsetLeft + (btnWidth - indWidth) / 2) + 'px',
    width: indWidth + 'px',
    opacity: 1
  }
}

function onDropdownChange(v) {
  dropdownOpen.value = v
  if (v) hoveredNav.value = 'user'
  else if (hoveredNav.value === 'user') hoveredNav.value = null
  nextTick(updateNavIndicator)
}

function updateNavIndicator() {
  navIndicatorStyle.value = getNavIndicatorStyle()
}

watch([activeNav, hoveredNav, navReady], () => {
  nextTick(updateNavIndicator)
})

onMounted(async () => {
  if (userStore.token) {
    try { await userStore.getInfo() } catch (e) { userStore.logout() }
  }
  await nextTick()
  navReady.value = true
})
</script>

<style scoped>
.layout { min-height: 100vh; }
.header {
  background: rgba(255,255,255,0.92);
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
  border-bottom: 1px solid #f0f2f5;
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 100;
  height: 60px !important;
}
.header-inner {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 24px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 100%;
}
.logo {
  display: flex;
  align-items: center;
  gap: 10px;
  text-decoration: none;
}
.logo-icon {
  flex-shrink: 0;
}
.logo-text {
  font-size: 18px;
  font-weight: 600;
  color: #111827;
  letter-spacing: -0.02em;
}
.nav { display: flex; align-items: center; gap: 6px; position: relative; }
.nav-item {
  color: #6B7280;
  text-decoration: none;
  font-size: 14px;
  font-weight: 500;
  padding: 8px 14px;
  border-radius: 8px;
  transition: all .2s ease;
  position: relative;
  z-index: 1;
}
.nav-item.active {
  color: #111827;
}
.nav-indicator {
  position: absolute;
  bottom: 2px;
  height: 3px;
  background: #2563EB;
  border-radius: 2px;
  transition: left 0.45s cubic-bezier(0.4, 0, 0.2, 1), width 0.45s cubic-bezier(0.4, 0, 0.2, 1);
}
.nav-user-wrap {
  display: flex;
  align-items: center;
}
.nav-item:hover {
  color: #111827;
}
.user-btn {
  cursor: pointer;
  color: #6B7280;
  font-size: 14px;
  font-weight: 500;
  padding: 8px 14px;
  border-radius: 8px;
  transition: all .2s ease;
}
.user-btn:hover {
  color: #111827;
}
.main {
  max-width: 1200px;
  margin: 80px auto 20px;
  padding: 0 24px;
  min-height: calc(100vh - 100px);
}
</style>