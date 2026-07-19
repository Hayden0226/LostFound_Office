<template>
  <div>
    <el-card style="margin-bottom:20px">
      <template #header>账户信息</template>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="用户名">{{ userStore.userInfo?.username || "-" }}</el-descriptions-item>
        <el-descriptions-item label="角色">
          <el-tag v-if="userStore.userInfo?.role === 'admin'" type="danger" size="small">管理员</el-tag>
          <el-tag v-else size="small">用户</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="昵称">{{ userStore.userInfo?.nickname || "-" }}</el-descriptions-item>
        <el-descriptions-item label="注册时间">{{ userStore.userInfo?.createTime || "-" }}</el-descriptions-item>
      </el-descriptions>
    </el-card>

    <el-card style="margin-bottom:20px">
      <template #header>修改资料</template>
      <el-form :model="userInfo" label-width="80px">
        <el-form-item label="昵称">
          <el-input v-model="userInfo.nickname" />
        </el-form-item>
        <el-form-item label="手机">
          <el-input v-model="userInfo.phone" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="userInfo.email" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="updateProfile">保存修改</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-tabs v-model="activeTab">
      <el-tab-pane label="我的发布" name="my">
        <div v-if="myItems.length === 0" style="text-align:center;padding:20px;color:#999">暂无发布</div>
        <el-card v-for="item in myItems" :key="item.id" style="margin-bottom:10px;cursor:pointer" @click="router.push('/home/' + item._type + '/' + item.id)">
          <div style="display:flex;justify-content:space-between;align-items:center">
            <div>
              <strong>{{ item.title }}</strong>
              <el-tag v-if="item.category" size="small" type="info" style="margin-left:10px">{{ item.category }}</el-tag>
              <el-tag v-if="item.status === 0" size="small" type="warning" style="margin-left:10px">待审核</el-tag>
              <el-tag v-else-if="item.status === 1" size="small" type="success" style="margin-left:10px">已发布</el-tag>
              <el-tag v-else size="small" style="margin-left:10px">已结束</el-tag>
              <p style="color:#666;font-size:12px;margin-top:5px">{{ item.createTime }}</p>
            </div>
            <el-button size="small" text @click.stop="editMyItem(item)">编辑</el-button>
            <el-button size="small" text @click.stop="deleteMyItem(item)">删除</el-button>
          </div>
        </el-card>
        <el-pagination v-if="myTotal > 0" v-model:current-page="myPage" :page-size="10" :total="myTotal" layout="prev,pager,next" @current-change="loadMyItems" style="text-align:center;margin-top:10px" />
      </el-tab-pane>
      <el-tab-pane label="我的认领" name="claims">
        <div v-if="myClaims.length === 0" style="text-align:center;padding:20px;color:#999">暂无认领</div>
        <el-card v-for="claim in myClaims" :key="claim.id" style="margin-bottom:10px;cursor:pointer" @click="router.push('/home/' + claim.itemType + '/' + claim.itemId)">
          <div style="display:flex;justify-content:space-between;align-items:center">
            <div>
              <strong>{{ claim.itemTitle }}</strong>
              <el-tag v-if="claim.itemType === 'lost'" size="small" type="primary" style="margin-left:8px">宝贝回家</el-tag>
              <el-tag v-else size="small" type="warning" style="margin-left:8px">失物招领</el-tag>
              <el-tag v-if="claim.status === 0" size="small" type="warning" style="margin-left:8px">待审核</el-tag>
              <el-tag v-else-if="claim.status === 1" size="small" type="success" style="margin-left:8px">已通过</el-tag>
              <el-tag v-else-if="claim.status === 2" size="small" type="danger" style="margin-left:8px">已拒绝</el-tag>
              <p style="color:#666;font-size:12px;margin-top:5px">{{ claim.createTime }}</p>
              <p v-if="claim.reason" style="color:#999;font-size:12px">认领理由：{{ claim.reason }}</p>
            </div>
          </div>
        </el-card>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import { getMyLostItems, deleteLostItem } from '@/api/lost'
import { getMyFoundItems, deleteFoundItem } from '@/api/found'
import { updateUser } from '@/api/user'
import { getMyClaims } from '@/api/claim'
const userStore = useUserStore()
const router = useRouter()
const activeTab = ref('my')
const userInfo = reactive({ ...userStore.userInfo } || {})
const myItems = ref([])
const myTotal = ref(0)
const myPage = ref(1)
const myClaims = ref([])

async function updateProfile() {
  await updateUser(userInfo)
  ElMessage.success('保存成功')
  await userStore.getInfo()
}
async function loadMyItems() {
  const [lostRes, foundRes] = await Promise.all([
    getMyLostItems({ page: myPage.value, size: 10 }),
    getMyFoundItems({ page: myPage.value, size: 10 })
  ])
  const lostWithType = (lostRes.data.records || []).map(item => ({ ...item, _type: 'lost' }))
  const foundWithType = (foundRes.data.records || []).map(item => ({ ...item, _type: 'found' }))
  myItems.value = [...lostWithType, ...foundWithType]
  myTotal.value = (lostRes.data.total || 0) + (foundRes.data.total || 0)
}
function editMyItem(item) {
  const path = item._type === 'lost' ? '/home/publish/' + item.id : '/home/publish-found/' + item.id
  router.push(path)
}
async function deleteMyItem(item) {
  if (item._type === 'lost') {
    await deleteLostItem(item.id)
  } else {
    await deleteFoundItem(item.id)
  }
  ElMessage.success('已删除')
  loadMyItems()
}
async function loadMyClaims() {
  const res = await getMyClaims()
  myClaims.value = res.data || []
}
onMounted(() => { Object.assign(userInfo, userStore.userInfo); loadMyItems(); loadMyClaims() })
</script>
