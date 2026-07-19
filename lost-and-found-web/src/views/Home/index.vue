<template>
  <div class="home">
    <div class="hero">
      <div class="hero-inner">
        <div class="hero-icon">
          <svg width="56" height="56" viewBox="0 0 56 56" fill="none" xmlns="http://www.w3.org/2000/svg">
            <rect x="4" y="4" width="48" height="48" rx="12" fill="#2563EB" fill-opacity="0.1"/>
            <path d="M28 16C23.6 16 20 19.6 20 24C20 28.4 23.6 32 28 32C32.4 32 36 28.4 36 24C36 19.6 32.4 16 28 16ZM28 18C31.3 18 34 20.7 34 24C34 27.3 31.3 30 28 30C24.7 30 22 27.3 22 24C22 20.7 24.7 18 28 18Z" fill="#2563EB"/>
            <path d="M36 34H20C17.8 34 16 35.8 16 38V40H40V38C40 35.8 38.2 34 36 34Z" fill="#2563EB" fill-opacity="0.4"/>
          </svg>
        </div>
        <h1 class="hero-title">校 园 失 物 招 领</h1>
        <p class="hero-subtitle">Lost &amp; Found on Campus<br/>帮助每一件物品找到主人</p>
        <div class="search-bar">
          <div class="search-wrapper">
            <svg class="search-icon" width="20" height="20" viewBox="0 0 20 20" fill="none">
              <path d="M9 3a6 6 0 100 12A6 6 0 009 3zM16 16l-3.26-3.26" stroke="#9CA3AF" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
            <input v-model="keyword" placeholder="搜索失物、证件、校园卡..." @keyup.enter="search" class="hero-search-input" />
          </div>
          <button class="hero-search-btn" @click="search">搜索</button>
        </div>
        <div class="quick-links">
          <div class="ql-pill" :class="{ raised: !!hoverBtn }" :style="btnPillStyle"></div>
          <button ref="btnLost" class="hero-btn" :class="{ active: hoverBtn === 'lost' || (!hoverBtn && currentBtn === 'lost') }"
             @mouseenter="hoverBtn = 'lost'" @mouseleave="hoverBtn = null"
             @click="currentBtn = 'lost'; $router.push('/home/lost')">浏览失物</button>
          <button ref="btnFound" class="hero-btn" :class="{ active: hoverBtn === 'found' || (!hoverBtn && currentBtn === 'found') }"
             @mouseenter="hoverBtn = 'found'" @mouseleave="hoverBtn = null"
             @click="currentBtn = 'found'; $router.push('/home/found')">浏览招领</button>
          <button ref="btnPublish" v-if="userStore.token" class="hero-btn" :class="{ active: hoverBtn === 'publish' || (!hoverBtn && currentBtn === 'publish') }"
             @mouseenter="hoverBtn = 'publish'" @mouseleave="hoverBtn = null"
             @click="currentBtn = 'publish'; showPublishDialog = true">发布信息</button>
        </div>

        <!-- Publish dialog -->
        <el-dialog v-model="showPublishDialog" width="380px" :close-on-click-modal="true" top="32vh" class="publish-dialog">
          <div class="publish-options">
            <p style="font-size:13px;color:#6B7280;margin: 0 0 16px 0;text-align:center">选择发布类型</p>
            <button class="publish-option-btn" @click="goPublish('lost')">
              <span class="publish-option-icon">
                <svg width="24" height="24" viewBox="0 0 24 24" fill="none">
                  <path d="M12 4a5 5 0 00-5 5c0 2.8 1.5 4.7 3 6.2a8 8 0 002 1.6V19h-2v2h4v-2h-2v-2.2a8 8 0 002-1.6c1.5-1.5 3-3.4 3-6.2a5 5 0 00-5-5z" fill="#2563EB"/>
                </svg>
              </span>
              <span class="publish-option-text">我丢掉了东西</span>
            </button>
            <button class="publish-option-btn" @click="goPublish('found')">
              <span class="publish-option-icon">
                <svg width="24" height="24" viewBox="0 0 24 24" fill="none">
                  <path d="M12 2C8.7 2 6 4.7 6 8c0 3.3 2 5.5 3.5 7a10 10 0 002 1.5V20H10v2h4v-2h-1.5v-3.5a10 10 0 002-1.5C16 13.5 18 11.3 18 8c0-3.3-2.7-6-6-6zm0 2c2.2 0 4 1.8 4 4s-1.8 4-4 4-4-1.8-4-4 1.8-4 4-4z" fill="#2563EB" fill-opacity="0.4"/>
                  <path d="M14 8a2 2 0 11-4 0 2 2 0 014 0z" fill="#2563EB"/>
                </svg>
              </span>
              <span class="publish-option-text">我捡到了东西</span>
            </button>
          </div>
        </el-dialog>

      </div>
    </div>
    <div class="tab-header">
        <div class="tab-list" ref="tabList">
          <button
            class="tab-item"
            :class="{ active: activeTab === 'lost' }"
            @click="activeTab = 'lost'"
            @mouseenter="hoverTab = 'lost'"
            @mouseleave="hoverTab = null"
            ref="lostTab"
          >最新宝贝回家</button>
          <button
            class="tab-item"
            :class="{ active: activeTab === 'found' }"
            @click="activeTab = 'found'"
            @mouseenter="hoverTab = 'found'"
            @mouseleave="hoverTab = null"
            ref="foundTab"
          >最新失物招领</button>
        </div>
        <div class="tab-indicator" :style="indicatorStyle"></div>
    </div>
      <div class="tab-content" v-show="activeTab === 'lost'">
        <div v-if="lostItems.length === 0" style="text-align:center;padding:40px;color:#999">暂无失物信息</div>
        <el-row :gutter="20">
          <el-col :span="8" v-for="item in lostItems" :key="item.id" style="margin-bottom:20px">
            <el-card :body-style="{ padding: '15px' }" shadow="hover" @click="$router.push('/home/lost/' + item.id)" style="cursor:pointer">
              <img v-if="item.image" :src="item.image" style="width:100%;height:150px;object-fit:cover;border-radius:4px" />
              <div v-else style="width:100%;height:150px;background:#f0f0f0;display:flex;align-items:center;justify-content:center;color:#ccc;border-radius:4px">暂无图片</div>
              <h4 style="margin:10px 0 5px">{{ item.title }}</h4>
              <p style="color:#666;font-size:12px">{{ item.location }} · {{ item.createTime }}</p>
              <p style="color:#999;font-size:12px;margin-top:4px">发布者：<router-link :to="'/home/user/' + item.publisherId" style="color:#409eff;text-decoration:none">{{ item.publisherNickname || "用户"+item.publisherId }}</router-link></p>
            </el-card>
          </el-col>
        </el-row>
      </div>
      <div class="tab-content" v-show="activeTab === 'found'">
        <div v-if="foundItems.length === 0" style="text-align:center;padding:40px;color:#999">暂无招领信息</div>
        <el-row :gutter="20">
          <el-col :span="8" v-for="item in foundItems" :key="item.id" style="margin-bottom:20px">
            <el-card :body-style="{ padding: '15px' }" shadow="hover" @click="$router.push('/home/found/' + item.id)" style="cursor:pointer">
              <img v-if="item.image" :src="item.image" style="width:100%;height:150px;object-fit:cover;border-radius:4px" />
              <div v-else style="width:100%;height:150px;background:#f0f0f0;display:flex;align-items:center;justify-content:center;color:#ccc;border-radius:4px">暂无图片</div>
              <h4 style="margin:10px 0 5px">{{ item.title }}</h4>
              <p style="color:#666;font-size:12px">{{ item.location }} · {{ item.createTime }}</p>
              <p style="color:#999;font-size:12px;margin-top:4px">发布者：<router-link :to="'/home/user/' + item.publisherId" style="color:#409eff;text-decoration:none">{{ item.publisherNickname || "用户"+item.publisherId }}</router-link></p>
            </el-card>
          </el-col>
        </el-row>
      </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, nextTick, watch } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { getLostList } from '@/api/lost'
import { getFoundList } from '@/api/found'
const router = useRouter()
const userStore = useUserStore()
const keyword = ref('')
const activeTab = ref('lost')
const hoverTab = ref(null)
const lostTab = ref(null)
const foundTab = ref(null)
const lostItems = ref([])
const foundItems = ref([])
const tabReady = ref(false)

function getIndicatorStyle() {
  if (!tabReady.value) return { opacity: 0, left: '0px', width: '0px' }
  const target = hoverTab.value || activeTab.value
  const el = target === 'lost' ? lostTab.value : foundTab.value
  if (!el) return { opacity: 0, left: '0px', width: '0px' }
  const btnWidth = el.offsetWidth
  const indWidth = btnWidth * 0.6
  return {
    left: (el.offsetLeft + (btnWidth - indWidth) / 2) + 'px',
    width: indWidth + 'px',
    opacity: 1
  }
}

const indicatorStyle = ref({ opacity: 0 })

function updateIndicator() {
  indicatorStyle.value = getIndicatorStyle()
}

watch([activeTab, hoverTab, tabReady], () => {
  nextTick(updateIndicator)
})

function search() {
  if (keyword.value) {
    router.push({ path: '/home/lost', query: { keyword: keyword.value } })
  }
}

const showPublishDialog = ref(false)
const btnLost = ref(null)
const btnFound = ref(null)
const btnPublish = ref(null)
const hoverBtn = ref(null)
const btnReady = ref(false)

const currentBtn = ref('lost')
const btnPillStyle = ref({})

watch([hoverBtn, currentBtn, btnReady], () => {
  nextTick(updateBtnPill)
})

function updateBtnPill() {
  const target = hoverBtn.value || currentBtn.value
  const el = target === 'lost' ? btnLost.value : target === 'found' ? btnFound.value : btnPublish.value
  if (!el) return
  btnPillStyle.value = {
    left: el.offsetLeft + 'px',
    width: el.offsetWidth + 'px',
  }
}

function goPublish(type) {
  showPublishDialog.value = false
  if (type === 'lost') {
    router.push('/home/publish')
  } else {
    router.push('/home/publish-found')
  }
}

onMounted(async () => {
  const [lostRes, foundRes] = await Promise.all([
    getLostList({ page: 1, size: 6 }),
    getFoundList({ page: 1, size: 6 })
  ])
  lostItems.value = lostRes.data?.records || []
  foundItems.value = foundRes.data?.records || []
  await nextTick()
  tabReady.value = true
  btnReady.value = true
})
</script>

<style scoped>
.hero {
  position: relative;
  margin-bottom: 48px;
  padding: 80px 120px 72px;
  display: flex;
  align-items: center;
  justify-content: center;
  background:
    radial-gradient(ellipse at 50% 0%, rgba(37,99,235,0.04) 0%, transparent 60%),
    linear-gradient(180deg, #ffffff 0%, #f7f8fc 100%);
  border-bottom: 1px solid #f0f2f5;
}
.hero-inner {
  width: 100%;
  max-width: 960px;
  text-align: center;
}
.hero-icon {
  margin-bottom: 20px;
  display: flex;
  justify-content: center;
}
.hero-icon svg {
  display: block;
}
.hero-title {
  font-size: 58px;
  font-weight: 600;
  color: #111827;
  line-height: 1.05;
  letter-spacing: -0.03em;
  margin: 0 0 24px 0;
}
.hero-subtitle {
  font-size: 20px;
  color: #6B7280;
  line-height: 1.65;
  margin: 0 0 48px 0;
}
.hero-subtitle br {
  display: none;
}
.search-bar {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  width: 100%;
  max-width: 900px;
  margin: 0 auto 36px;
}
.search-wrapper {
  position: relative;
  width: 100%;
  max-width: 720px;
}
.search-icon {
  position: absolute;
  left: 20px;
  top: 50%;
  transform: translateY(-50%);
  pointer-events: none;
  z-index: 1;
}
.hero-search-input {
  width: 100%;
  height: 60px;
  padding: 0 20px 0 52px;
  font-size: 16px;
  color: #111827;
  background: #ffffff;
  border: 1px solid #e8ecf2;
  border-radius: 16px;
  outline: none;
  transition: all .25s ease;
  box-sizing: border-box;
}
.hero-search-input::placeholder {
  color: #9CA3AF;
}
.hero-search-input:focus {
  border-color: #2563EB;
  box-shadow: 0 0 0 4px rgba(37,99,235,0.12);
}
.hero-search-input:hover:not(:focus) {
  border-color: #d1d5db;
}
.hero-search-btn {
  height: 60px;
  padding: 0 32px;
  font-size: 16px;
  font-weight: 500;
  color: #fff;
  background: #2563EB;
  border: none;
  border-radius: 16px;
  cursor: pointer;
  transition: all .25s ease;
  white-space: nowrap;
}
.hero-search-btn:hover {
  background: #1D4ED8;
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(37,99,235,0.2);
}
.quick-links {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
}
.hero-btn {
  position: relative;
  z-index: 1;
  height: 48px;
  padding: 0 24px;
  font-size: 15px;
  font-weight: 500;
  border-radius: 12px;
  border: 1px solid transparent;
  cursor: pointer;
  transition: color .25s ease;
  white-space: nowrap;
  background: transparent;
  color: #6B7280;
}
.hero-btn.active {
  color: #fff;
}
.ql-pill {
  position: absolute;
  height: 48px;
  background: #2563EB;
  border-radius: 12px;
  transition: left 0.8s cubic-bezier(0.22, 1, 0.36, 1), width 0.8s cubic-bezier(0.22, 1, 0.36, 1), box-shadow 0.3s ease, transform 0.3s ease;
  z-index: 0;
  pointer-events: none;
}
.ql-pill.raised {
  box-shadow: 0 8px 24px rgba(37,99,235,0.25), 0 2px 6px rgba(37,99,235,0.15);
  transform: translateY(-1px);
}
.home { background: #f7f8fc; min-height: 100vh; }
.publish-dialog :deep(.el-dialog__header) { display: none; }
.publish-dialog :deep(.el-dialog__body) { padding: 24px; }
.publish-options { display: flex; flex-direction: column; gap: 12px; }
.publish-option-btn {
  display: flex;
  align-items: center;
  gap: 14px;
  width: 100%;
  padding: 14px 20px;
  border: 1px solid #e8ecf2;
  border-radius: 12px;
  background: #fff;
  cursor: pointer;
  transition: all .25s ease;
  text-align: left;
  font-size: 15px;
  font-weight: 500;
  color: #111827;
}
.publish-option-btn:hover {
  border-color: #2563EB;
  background: #f8faff;
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(37,99,235,0.1);
}
.publish-option-icon { flex-shrink: 0; display: flex; }
.publish-option-text { color: #111827; }
.content-tabs { margin-top: 32px; }
.tab-header {
  position: relative;
  margin-bottom: 28px;
}
.tab-list {
  display: flex;
  gap: 0;
  position: relative;
}
.tab-item {
  padding: 12px 24px;
  font-size: 15px;
  font-weight: 500;
  color: #6B7280;
  background: none;
  border: none;
  cursor: pointer;
  transition: color .25s ease;
  position: relative;
  z-index: 1;
}
.tab-item:hover {
  color: #111827;
}
.tab-item.active {
  color: #111827;
}
.tab-indicator {
  position: absolute;
  bottom: 0;
  height: 3px;
  background: #2563EB;
  border-radius: 2px;
  transition: left 0.45s cubic-bezier(0.4, 0, 0.2, 1), width 0.45s cubic-bezier(0.4, 0, 0.2, 1);
}
.tab-content {
  animation: fadeIn .3s ease;
}
@keyframes fadeIn {
  from { opacity: 0; transform: translateY(8px); }
  to { opacity: 1; transform: translateY(0); }
}
@media (max-width: 900px) {
  .hero { padding: 48px 24px 40px; flex-direction: column; }
  .hero-title { font-size: 36px; }
  .hero-subtitle { font-size: 17px; margin-bottom: 36px; }
  .search-wrapper { width: 100%; }
  .search-bar { flex-direction: column; width: 100%; }
  .hero-search-btn { width: 100%; }
  .quick-links { flex-wrap: wrap; gap: 10px; }
  .hero-btn { flex: 1; min-width: 0; padding: 0 16px; font-size: 14px; }
  .ql-pill { display: none; }
}
</style>