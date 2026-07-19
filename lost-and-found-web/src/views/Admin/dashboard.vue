<template>
  <div>
    <div style="margin-bottom:15px;display:flex;justify-content:space-between;align-items:center">
      <h3>数据概览</h3>
      <el-button size="small" @click="loadData">刷新数据</el-button>
    </div>
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card>
          <h3>{{ stats.userCount }}</h3>
          <p style="color:#666">用户总数</p>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <h3>{{ stats.lostCount }}</h3>
          <p style="color:#666">宝贝回家帖子</p>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <h3>{{ stats.foundCount }}</h3>
          <p style="color:#666">失物招领帖子</p>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getStats } from '@/api/admin'

const stats = ref({ userCount: 0, lostCount: 0, foundCount: 0 })

async function loadData() {
  const res = await getStats()
  stats.value = {
    userCount: res.data.userCount || 0,
    lostCount: res.data.lostCount || 0,
    foundCount: res.data.foundCount || 0
  }
}
onMounted(loadData)
</script>

<style scoped>
h3 { font-size: 28px; margin: 0 0 8px; }
.el-card { cursor: pointer; transition: transform .25s, box-shadow .25s; }
.el-card:hover { transform: translateY(-3px); box-shadow: 0 4px 14px rgba(0,0,0,.06); }
</style>
