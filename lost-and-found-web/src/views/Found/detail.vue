<template>
  <div v-if="item" class="detail">
    <el-button text @click="$router.back()" style="margin-bottom:15px">&lt; 返回</el-button>
    <el-row :gutter="30">
      <el-col :span="12">
        <img v-if="item.image" :src="item.image" style="width:100%;border-radius:8px" />
        <div v-else style="width:100%;height:300px;background:#f0f0f0;display:flex;align-items:center;justify-content:center;color:#ccc;border-radius:8px">暂无图片</div>
      </el-col>
      <el-col :span="12">
        <h2>{{ item.title }}</h2>
        <el-tag v-if="item.category" style="margin-bottom:8px">{{ item.category }}</el-tag>
        <p style="color:#999;font-size:13px;margin-top:5px">发布者：<router-link :to="'/home/user/' + item.publisherId" style="color:#409eff;text-decoration:none">{{ item.publisherNickname || "用户"+item.publisherId }}</router-link></p>
        <p style="margin:15px 0;color:#666;line-height:1.8">{{ item.description }}</p>
        <p><strong>地点：</strong>{{ item.location }}</p>
        <p><strong>捡到时间：</strong>{{ item.foundTime }}</p>
        <p><strong>联系方式：</strong>{{ item.contactPhone }}</p>
        <p v-if="approvedClaim" style="color:#e6a23c;font-weight:bold"><strong>已认领人：</strong>{{ approvedClaim.claimerNickname || '用户'+approvedClaim.claimerId }} ({{ approvedClaim.claimerPhone }})</p>
        <div style="margin-top:20px">
          <el-button v-if="userStore.token && userStore.userInfo?.id !== item.publisherId" type="primary" @click="showClaim = true">联系认领</el-button>
        </div>
      </el-col>
    </el-row>

    <el-divider />
    <h3>留言 ({{ comments.length }})</h3>
    <div v-if="userStore.token" style="margin:15px 0">
      <el-input v-model="commentContent" placeholder="写下你的留言..." type="textarea" :rows="3" style="max-width:600px" />
      <el-button type="primary" style="margin-top:10px" @click="addComment">发表留言</el-button>
    </div>
    <div v-for="c in comments" :key="c.id" style="padding:10px 0;border-bottom:1px solid #eee">
      <strong>用户{{ c.userId }}</strong><span style="color:#999;margin-left:10px;font-size:12px">{{ c.createTime }}</span>
      <p style="margin-top:5px">{{ c.content }}</p>
    </div>

    <el-dialog v-model="showClaim" title="认领申请" width="500px">
      <el-form :model="claimForm" label-width="80px">
        <el-form-item label="认领理由">
          <el-input v-model="claimForm.reason" type="textarea" :rows="4" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showClaim = false">取消</el-button>
        <el-button type="primary" @click="submitClaim">提交</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import { getFoundDetail } from '@/api/found'
import { getComments, addComment as addCommentApi } from '@/api/comment'
import { addClaim, getApprovedClaim } from '@/api/claim'
const route = useRoute()
const userStore = useUserStore()
const item = ref(null)
const comments = ref([])
const commentContent = ref('')
const showClaim = ref(false)
const claimForm = ref({ reason: '' })
const approvedClaim = ref(null)
const id = route.params.id

async function loadDetail() {
  const res = await getFoundDetail(id)
  item.value = res.data
}
async function loadComments() {
  const res = await getComments(id, 'found')
  comments.value = res.data
}
async function addComment() {
  if (!commentContent.value) return
  await addCommentApi({ itemId: id, itemType: 'found', content: commentContent.value })
  commentContent.value = ''
  loadComments()
}
async function submitClaim() {
  await addClaim({ itemId: id, itemType: 'found', reason: claimForm.value.reason })
  ElMessage.success('认领申请已提交')
  showClaim.value = false
}
async function loadApprovedClaim() {
  const res = await getApprovedClaim(id, 'found')
  approvedClaim.value = res.data
}
onMounted(() => { loadDetail(); loadComments(); loadApprovedClaim() })
</script>
