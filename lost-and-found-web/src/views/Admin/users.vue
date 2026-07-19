<template>
  <div>
    <h3 style="margin-bottom:15px">用户管理</h3>
    <el-table :data="users" stripe>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="username" label="用户名" />
      <el-table-column prop="nickname" label="昵称" />
      <el-table-column prop="role" label="角色" width="100">
        <template #default="{ row }">
          <el-tag :type="row.role === 'admin' ? 'danger' : 'info'">{{ row.role }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="注册时间" />
    </el-table>
    <el-pagination v-if="total > 0" v-model:current-page="page" :page-size="10" :total="total" layout="prev,pager,next" @current-change="loadData" style="text-align:center;margin-top:15px" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getUsers } from '@/api/admin'
const users = ref([])
const total = ref(0)
const page = ref(1)
async function loadData() {
  const res = await getUsers({ page: page.value, size: 10 })
  users.value = res.data.records
  total.value = res.data.total
}
onMounted(loadData)
</script>
