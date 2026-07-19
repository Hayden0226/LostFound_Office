<template>
  <div>
    <h3 style="margin-bottom:15px">帖子管理</h3>
    <el-tabs v-model="activeTab">
      <el-tab-pane label="宝贝回家帖子" name="lost">
        <el-table :data="lostItems" stripe>
          <el-table-column prop="id" label="ID" width="60" />
          <el-table-column label="标题" min-width="150">
            <template #default="{ row }"><a style="color:#409eff;cursor:pointer;text-decoration:none" @click="router.push('/home/lost/' + row.id)">{{ row.title }}</a></template>
          </el-table-column>
          <el-table-column label="发布人" width="120">
            <template #default="{ row }"><router-link :to="'/home/user/' + row.publisherId" style="color:#409eff;text-decoration:none">{{ row.publisherNickname || row.publisherId }}</router-link></template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="100">
            <template #default="{ row }">
              <el-tag v-if="row.status === 0" type="warning">待审核</el-tag>
              <el-tag v-else-if="row.status === 1" type="success">已发布</el-tag>
              <el-tag v-else>已结束</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="时间" width="160" />
          <el-table-column label="操作" width="120">
            <template #default="{ row }">
              <el-button size="small" text type="danger" @click.stop="handleDelete('lost', row.id)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
      <el-tab-pane label="失物招领帖子" name="found">
        <el-table :data="foundItems" stripe>
          <el-table-column prop="id" label="ID" width="60" />
          <el-table-column label="标题" min-width="150">
            <template #default="{ row }"><a style="color:#409eff;cursor:pointer;text-decoration:none" @click="router.push('/home/found/' + row.id)">{{ row.title }}</a></template>
          </el-table-column>
          <el-table-column label="发布人" width="120">
            <template #default="{ row }"><router-link :to="'/home/user/' + row.publisherId" style="color:#409eff;text-decoration:none">{{ row.publisherNickname || row.publisherId }}</router-link></template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="100">
            <template #default="{ row }">
              <el-tag v-if="row.status === 0" type="warning">待审核</el-tag>
              <el-tag v-else-if="row.status === 1" type="success">已发布</el-tag>
              <el-tag v-else>已结束</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="时间" width="160" />
          <el-table-column label="操作" width="120">
            <template #default="{ row }">
              <el-button size="small" text type="danger" @click.stop="handleDelete('found', row.id)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getLostItems, getFoundItems, deleteItem } from '@/api/admin'
const router = useRouter()
const route = useRoute()
const activeTab = ref(route.query.tab || 'lost')
watch(() => route.query.tab, v => { if (v) activeTab.value = v })
const lostItems = ref([])
const foundItems = ref([])

async function loadData() {
  const [lost, found] = await Promise.all([
    getLostItems({ page: 1, size: 100 }),
    getFoundItems({ page: 1, size: 100 })
  ])
  lostItems.value = lost.data.records || []
  foundItems.value = found.data.records || []
}
async function handleDelete(type, id) {
  await deleteItem(type, id)
  ElMessage.success('已删除')
  loadData()
}
onMounted(loadData)
</script>
