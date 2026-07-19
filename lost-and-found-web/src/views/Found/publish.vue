<template>
  <div style="max-width:600px;margin:0 auto">
    <h2 style="margin-bottom:20px">{{ isEdit ? '编辑失物招领' : '发布招领信息' }}</h2>
    <el-form :model="form" label-width="130px">
      <el-form-item label="标题" required>
        <el-input v-model="form.title" placeholder="如：刚刚捡到一个黑色钱包" />
      </el-form-item>
      <el-form-item label="描述">
        <el-input v-model="form.description" type="textarea" :rows="4" placeholder="描述你刚才捡到的物品" />
      </el-form-item>
      <el-form-item label="分类">
        <el-select v-model="form.category" style="width:100%">
          <el-option value="电子产品" label="电子产品" />
          <el-option value="证件卡包" label="证件卡包" />
          <el-option value="书籍文具" label="书籍文具" />
          <el-option value="生活用品" label="生活用品" />
          <el-option value="其他" label="其他" />
        </el-select>
      </el-form-item>
      <el-form-item label="捡到地点">
        <el-input v-model="form.location" placeholder="如：食堂一楼" />
      </el-form-item>
      <el-form-item label="捡到时间">
        <el-date-picker v-model="form.foundTime" type="datetime" placeholder="选择时间" style="width:100%" />
      </el-form-item>
      <el-form-item label="联系方式">
        <el-input v-model="form.contactPhone" placeholder="你的手机号" />
      </el-form-item>
      <el-form-item label="图片">
        <el-upload :auto-upload="false" :show-file-list="false" @change="handleUpload">
          <el-button>选择图片</el-button>
        </el-upload>
        <img v-if="preview" :src="preview" style="width:200px;margin-top:10px;border-radius:4px" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submit" :loading="loading">提交</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { addFoundItem, updateFoundItem, getFoundDetail } from '@/api/found'
import { uploadFile } from '@/api/upload'
import { useRouter, useRoute } from 'vue-router'
const router = useRouter()
const route = useRoute()
const isEdit = ref(false)
const loading = ref(false)
const preview = ref('')
const selectedFile = ref(null)
const form = reactive({ title: '', description: '', category: '', location: '', foundTime: '', contactPhone: '' })

function handleUpload(file) {
  selectedFile.value = file.raw
  const reader = new FileReader()
  reader.onload = e => preview.value = e.target.result
  reader.readAsDataURL(file.raw)
}

async function submit() {
  if (!form.title) { ElMessage.warning('请输入标题'); return }
  loading.value = true
  try {
    if (selectedFile.value) {
      const uploadRes = await uploadFile(selectedFile.value)
      form.image = uploadRes.data
    }
    if (isEdit.value) {
      await updateFoundItem(route.params.id, form)
      ElMessage.success('保存成功')
    } else {
      await addFoundItem(form)
      ElMessage.success('发布成功，等待审核')
    }
    router.push('/home')
  } finally { loading.value = false }
}

onMounted(async () => {
  if (route.params.id) {
    isEdit.value = true
    const res = await getFoundDetail(route.params.id)
    Object.assign(form, {
      title: res.data.title || '',
      description: res.data.description || '',
      category: res.data.category || '',
      location: res.data.location || '',
      foundTime: res.data.foundTime || '',
      contactPhone: res.data.contactPhone || '',
      image: res.data.image || '',
    })
    if (res.data.image) { preview.value = res.data.image }
  }
})
</script>
