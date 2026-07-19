<template>
  <div>
    <h2 style="margin-bottom:20px">浏览宝贝回家</h2>
    <el-row :gutter="20">
      <el-col :span="6">
        <el-input v-model="keyword" placeholder="搜索宝贝回家" clearable @change="search" />
      </el-col>
      <el-col :span="4">
        <el-select v-model="category" placeholder="宝贝类型" clearable @change="search" style="width:100%">
          <el-option value="" label="所有" />
          <el-option value="电子产品" label="电子产品" />
          <el-option value="证件卡包" label="证件卡包" />
          <el-option value="书籍文具" label="书籍文具" />
          <el-option value="生活用品" label="生活用品" />
          <el-option value="其他" label="其他" />
        </el-select>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top:20px">
      <el-col :span="8" v-for="item in list" :key="item.id" style="margin-bottom:20px">
        <el-card shadow="hover" @click="$router.push('/home/lost/' + item.id)" style="cursor:pointer">
          <img v-if="item.image" :src="item.image" style="width:100%;height:180px;object-fit:cover;border-radius:4px" />
          <div v-else style="width:100%;height:180px;background:#f0f0f0;display:flex;align-items:center;justify-content:center;color:#ccc">暂无失物信息无图片</div>
          <h4 style="margin:10px 0 5px">{{ item.title }}</h4>
          <el-tag size="small" v-if="item.category">{{ item.category }}</el-tag>
          <p style="color:#666;font-size:12px;margin-top:5px">{{ item.location }} 路 {{ item.createTime }}</p>
          <p style="color:#999;font-size:12px;margin-top:2px">发布者：<router-link :to="'/home/user/' + item.publisherId" style="color:#409eff;text-decoration:none">{{ item.publisherNickname || "用户"+item.publisherId }}</router-link></p>
        </el-card>
      </el-col>
    </el-row>

    <el-pagination
      v-if="total > 0"
      v-model:current-page="page"
      :page-size="size"
      :total="total"
      layout="prev, pager, next"
      @current-change="loadData"
      style="text-align:center;margin-top:20px"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getLostList } from '@/api/lost'
const route = useRoute()
const list = ref([])
const total = ref(0)
const page = ref(1)
const size = ref(12)
const keyword = ref(route.query.keyword || '')
const category = ref('')

async function loadData() {
  const res = await getLostList({ page: page.value, size: size.value, keyword: keyword.value, category: category.value })
  list.value = res.data.records
  total.value = res.data.total
}
function search() { page.value = 1; loadData() }
onMounted(loadData)
</script>
