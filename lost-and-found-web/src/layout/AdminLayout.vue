<template>
  <div class="admin-shell">
    <aside class="sidebar">
      <div class="brand">
        <span class="brand-text">Lost &amp; Found</span>
        <span class="brand-badge">管理后台</span>
      </div>
      <nav class="nav">
        <a href="/admin" class="nav-item" :class="{ active: $route.path === '/admin' }" @click.prevent="$router.push('/admin')">
          <svg class="nav-icon" width="16" height="16" viewBox="0 0 16 16" fill="none"><rect x="1.5" y="1.5" width="5.5" height="5.5" rx="1" stroke="currentColor" stroke-width="1.5"/><rect x="9" y="1.5" width="5.5" height="5.5" rx="1" stroke="currentColor" stroke-width="1.5"/><rect x="1.5" y="9" width="5.5" height="5.5" rx="1" stroke="currentColor" stroke-width="1.5"/><rect x="9" y="9" width="5.5" height="5.5" rx="1" stroke="currentColor" stroke-width="1.5"/></svg>
          <span>数据概览</span>
        </a>
        <a href="/admin/users" class="nav-item" :class="{ active: $route.path === '/admin/users' }" @click.prevent="$router.push('/admin/users')">
          <svg class="nav-icon" width="16" height="16" viewBox="0 0 16 16" fill="none"><circle cx="6" cy="4.5" r="2.5" stroke="currentColor" stroke-width="1.5"/><path d="M1 14c0-2.5 2-4.5 5-4.5s5 2 5 4.5" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/><circle cx="11.5" cy="5.5" r="2" stroke="currentColor" stroke-width="1.3"/><path d="M15 14c0-2.2-1.6-3.8-4-3.8" stroke="currentColor" stroke-width="1.3" stroke-linecap="round"/></svg>
          <span>用户管理</span>
        </a>
        <a href="/admin/items" class="nav-item" :class="{ active: $route.path === '/admin/items' }" @click.prevent="$router.push('/admin/items')">
          <svg class="nav-icon" width="16" height="16" viewBox="0 0 16 16" fill="none"><path d="M3 2h10l1.5 4H1.5L3 2z" stroke="currentColor" stroke-width="1.3"/><path d="M1.5 6v7a1 1 0 001 1h11a1 1 0 001-1V6" stroke="currentColor" stroke-width="1.3"/></svg>
          <span>帖子管理</span>
        </a>
        <a href="/admin/review" class="nav-item" :class="{ active: $route.path === '/admin/review' }" @click.prevent="$router.push('/admin/review')">
          <svg class="nav-icon" width="16" height="16" viewBox="0 0 16 16" fill="none"><path d="M8 1.5L9.5 5.5h4l-3.2 2.5 1.2 4L8 9l-3.5 3 1.2-4L2.5 5.5h4L8 1.5z" stroke="currentColor" stroke-width="1.3" stroke-linejoin="round"/></svg>
          <span>审核中心</span>
        </a>
      </nav>
    </aside>
    <main class="main">
      <header class="topbar">
        <div class="topbar-left">
          <span class="topbar-bread">管理后台</span>
        </div>
        <div class="topbar-right">
          <button class="topbar-btn" @click="$router.push('/home')">返回首页</button>
          <button class="topbar-btn topbar-btn--danger" @click="userStore.logout()">退出</button>
        </div>
      </header>
      <div class="content">
        <router-view />
      </div>
    </main>
  </div>
</template>

<script setup>
import { useUserStore } from '@/stores/user'
const userStore = useUserStore()
</script>

<style scoped>
.admin-shell {
  display: flex;
  min-height: 100vh;
  background: #f7f8fc;
}
.sidebar {
  width: 260px;
  background: #fff;
  border-right: 1px solid #e8ecf2;
  display: flex;
  flex-direction: column;
  flex-shrink: 0;
}
.brand {
  padding: 24px 20px 20px;
  display: flex;
  align-items: baseline;
  gap: 8px;
}
.brand-text {
  font-size: 18px;
  font-weight: 600;
  color: #111827;
  letter-spacing: -0.02em;
  text-decoration: none;
}
.brand-badge {
  font-size: 11px;
  font-weight: 450;
  color: #9ca3af;
  letter-spacing: 0.03em;
}
.nav {
  padding: 0 12px;
  display: flex;
  flex-direction: column;
  gap: 2px;
}
.nav-item {
  display: flex;
  align-items: center;
  gap: 30px;
  padding: 14px 16px;
  border-radius: 6px;
  font-size: 15px;
  font-weight: 450;
  color: #6B7280;
  text-decoration: none;
  transition: all 0.15s ease;
  cursor: pointer;
  line-height: 1.5;
}
.nav-item:hover {
  background: #f3f4f6;
  color: #111827;
}
.nav-item.active {
  background: #eff6ff;
  color: #2563EB;
  font-weight: 500;
}
.nav-icon {
  flex-shrink: 0;
}
.main {
  flex: 1;
  display: flex;
  flex-direction: column;
  min-width: 0;
}
.topbar {
  height: 64px;
  background: #fff;
  border-bottom: 1px solid #e8ecf2;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 32px;
  flex-shrink: 0;
}
.topbar-left {
  display: flex;
  align-items: center;
}

.topbar-right {
  display: flex;
  align-items: center;
  gap: 8px;
}
.topbar-btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  height: 40px;
  padding: 0 20px;
  font-size: 15px;
  font-weight: 500;
  border: none;
  border-radius: 0;
  background: transparent;
  color: #6B7280;
  cursor: pointer;
  transition: all 0.15s ease;
  line-height: 1;
  outline: none;
  letter-spacing: 0.01em;
}
.topbar-btn:hover {
  background: transparent;
  color: #111827;
  transform: translateY(-1px);
}
.topbar-btn--danger {
  color: #ef4444;
}
.topbar-btn--danger:hover {
  background: transparent;
  color: #dc2626;
  transform: translateY(-1px);
}
.content {
  flex: 1;
  padding: 28px;
  overflow-y: auto;
}
</style>
