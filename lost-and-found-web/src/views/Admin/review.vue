<template>
  <div>
    <h3 style="margin-bottom:15px">审核中心</h3>
    <el-tabs v-model="activeTab">
      <el-tab-pane label="宝贝回家审核" name="lost">
        <el-table :data="allLost" stripe>
          <el-table-column label="标题" min-width="150">
            <template #default="{ row }"><a style="color:#409eff;cursor:pointer;text-decoration:none" @click="router.push('/home/lost/' + row.id)">{{ row.title }}</a></template>
          </el-table-column>
          <el-table-column label="发布人" width="120">
            <template #default="{ row }"><router-link :to="'/home/user/' + row.publisherId" style="color:#409eff;text-decoration:none">{{ row.publisherNickname || row.publisherId }}</router-link></template>
          </el-table-column>
          <el-table-column prop="createTime" label="发布时间" width="150" />
          <el-table-column prop="status" label="状态" width="90">
            <template #default="{ row }">
              <el-tag v-if="row.status === 0" type="warning">待审核</el-tag>
              <el-tag v-else-if="row.status === 1" type="success">已通过</el-tag>
              <el-tag v-else type="info">已拒绝</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="180">
            <template #default="{ row }">
              <el-button v-if="row.status === 0" size="small" type="success" @click.stop="approveLost(row.id)">通过</el-button>
              <el-button v-if="row.status === 0" size="small" type="danger" @click.stop="rejectLost(row.id)">拒绝</el-button>
              <span v-else style="color:#999">已处理</span>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
      <el-tab-pane label="失物招领审核" name="found">
        <el-table :data="allFound" stripe>
          <el-table-column label="标题" min-width="150">
            <template #default="{ row }"><a style="color:#409eff;cursor:pointer;text-decoration:none" @click="router.push('/home/found/' + row.id)">{{ row.title }}</a></template>
          </el-table-column>
          <el-table-column label="发布人" width="120">
            <template #default="{ row }"><router-link :to="'/home/user/' + row.publisherId" style="color:#409eff;text-decoration:none">{{ row.publisherNickname || row.publisherId }}</router-link></template>
          </el-table-column>
          <el-table-column prop="createTime" label="发布时间" width="150" />
          <el-table-column prop="status" label="状态" width="90">
            <template #default="{ row }">
              <el-tag v-if="row.status === 0" type="warning">待审核</el-tag>
              <el-tag v-else-if="row.status === 1" type="success">已通过</el-tag>
              <el-tag v-else type="info">已拒绝</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="180">
            <template #default="{ row }">
              <el-button v-if="row.status === 0" size="small" type="success" @click.stop="approveFound(row.id)">通过</el-button>
              <el-button v-if="row.status === 0" size="small" type="danger" @click.stop="rejectFound(row.id)">拒绝</el-button>
              <span v-else style="color:#999">已处理</span>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getLostItems, getFoundItems, reviewLostItem, reviewFoundItem } from '@/api/admin'
const router = useRouter()
const activeTab = ref('lost')
const allLost = ref([])
const allFound = ref([])

async function loadData() {
  const [lost, found] = await Promise.all([
    getLostItems({ page: 1, size: 100 }),
    getFoundItems({ page: 1, size: 100 })
  ])
  allLost.value = lost.data.records || []
  allFound.value = found.data.records || []
}
async function approveLost(id) { await reviewLostItem(id, 1); ElMessage.success('已通过'); loadData() }
async function rejectLost(id) { await reviewLostItem(id, 3); ElMessage.success('已拒绝'); loadData() }
async function approveFound(id) { await reviewFoundItem(id, 1); ElMessage.success('已通过'); loadData() }
async function rejectFound(id) { await reviewFoundItem(id, 3); ElMessage.success('已拒绝'); loadData() }
onMounted(loadData)
</script>
