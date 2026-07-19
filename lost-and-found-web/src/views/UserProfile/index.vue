<template>
  <div v-if="user" class="profile-page">
    <el-button text @click="$router.back()" style="margin-bottom:15px">&lt; 返回</el-button>
    <el-card>
      <template #header>用户主页</template>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="用户名">{{ user.username || "-" }}</el-descriptions-item>
        <el-descriptions-item label="角色">
          <el-tag v-if="user.role === 'admin'" type="danger" size="small">管理员</el-tag>
          <el-tag v-else size="small">用户</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="昵称">{{ user.nickname || "-" }}</el-descriptions-item>
        <el-descriptions-item label="注册时间">{{ user.createTime || "-" }}</el-descriptions-item>
      </el-descriptions>
    </el-card>
    <el-card style="margin-top:20px">
      <template #header>该用户发布的帖子</template>
      <el-tabs v-model="activeTab">
        <el-tab-pane label="宝贝回家" name="lost">
          <div v-if="lostItems.length === 0" style="text-align:center;padding:20px;color:#999">暂无发布</div>
          <el-card v-for="item in lostItems" :key="item.id" style="margin-bottom:10px;cursor:pointer" @click="$router.push('/home/lost/' + item.id)">
            <strong>{{ item.title }}</strong>
            <p style="color:#666;font-size:12px;margin-top:5px">{{ item.createTime }}</p>
          </el-card>
        </el-tab-pane>
        <el-tab-pane label="失物招领" name="found">
          <div v-if="foundItems.length === 0" style="text-align:center;padding:20px;color:#999">暂无发布</div>
          <el-card v-for="item in foundItems" :key="item.id" style="margin-bottom:10px;cursor:pointer" @click="$router.push('/home/found/' + item.id)">
            <strong>{{ item.title }}</strong>
            <p style="color:#666;font-size:12px;margin-top:5px">{{ item.createTime }}</p>
          </el-card>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getPublicUser } from '@/api/user'
import { getLostList } from '@/api/lost'
import { getFoundList } from '@/api/found'
const route = useRoute()
const user = ref(null)
const lostItems = ref([])
const foundItems = ref([])
const activeTab = ref('lost')
const userId = route.params.id

onMounted(async () => {
  const res = await getPublicUser(userId)
  user.value = res.data
  const [lost, found] = await Promise.all([
    getLostList({ page: 1, size: 100, publisherId: userId }),
    getFoundList({ page: 1, size: 100, publisherId: userId })
  ])
  lostItems.value = lost.data?.records || []
  foundItems.value = found.data?.records || []
})
</script>

<style scoped>
.profile-page { max-width: 800px; margin: 0 auto; }
</style>