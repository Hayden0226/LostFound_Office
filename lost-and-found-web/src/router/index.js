import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'Login',
    component: () => import('@/views/Login/index.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/Register/index.vue')
  },
  {
    path: '/home',
    component: () => import('@/layout/Layout.vue'),
    meta: { requiresAuth: true },
    children: [
      { path: '', name: 'Home', component: () => import('@/views/Home/index.vue') },
      { path: 'lost', name: 'Lost', component: () => import('@/views/Lost/index.vue') },
      { path: 'lost/:id', name: 'LostDetail', component: () => import('@/views/Lost/detail.vue') },
      { path: 'found', name: 'Found', component: () => import('@/views/Found/index.vue') },
      { path: 'found/:id', name: 'FoundDetail', component: () => import('@/views/Found/detail.vue') },
      { path: 'publish/:id', name: 'EditLost', component: () => import('@/views/Lost/publish.vue'), meta: { requiresAuth: true } },
      { path: 'publish', name: 'Publish', component: () => import('@/views/Lost/publish.vue'), meta: { requiresAuth: true } },
      { path: 'publish-found/:id', name: 'EditFound', component: () => import('@/views/Found/publish.vue'), meta: { requiresAuth: true } },
      { path: 'publish-found', name: 'PublishFound', component: () => import('@/views/Found/publish.vue'), meta: { requiresAuth: true } },
      { path: 'profile', name: 'Profile', component: () => import('@/views/Profile/index.vue'), meta: { requiresAuth: true } },
      { path: 'user/:id', name: 'UserProfile', component: () => import('@/views/UserProfile/index.vue') },
    ]
  },
  {
    path: '/admin',
    component: () => import('@/layout/AdminLayout.vue'),
    meta: { requiresAuth: true, role: 'admin' },
    children: [
      { path: '', name: 'AdminDashboard', component: () => import('@/views/Admin/dashboard.vue') },
      { path: 'users', name: 'AdminUsers', component: () => import('@/views/Admin/users.vue') },
      { path: 'items', name: 'AdminItems', component: () => import('@/views/Admin/items.vue') },
      { path: 'review', name: 'AdminReview', component: () => import('@/views/Admin/review.vue') },
    ]
  },
]
const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach(async (to, from, next) => {
  const token = sessionStorage.getItem('token')
  
  if (to.meta.requiresAuth && !token) {
    next('/')
    return
  }
  
  if (to.meta.role === 'admin') {
    try {
      const { useUserStore } = await import('@/stores/user')
      const userStore = useUserStore()
      if (!userStore.userInfo) {
        await userStore.getInfo()
      }
      if (userStore.userInfo?.role !== 'admin') {
        next('/home')
        return
      }
    } catch (e) {
      next('/')
      return
    }
  }
  
  next()
})

export default router