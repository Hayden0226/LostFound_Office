import request from '@/utils/axios'

export function getStats() {
  return request.get('/admin/stats')
}

export function getUsers(params) {
  return request.get('/admin/users', { params })
}

export function getLostItems(params) {
  return request.get('/admin/lost-items', { params })
}

export function getFoundItems(params) {
  return request.get('/admin/found-items', { params })
}

export function reviewLostItem(id, status) {
  return request.put('/admin/review/lost/' + id, null, { params: { status } })
}

export function reviewFoundItem(id, status) {
  return request.put('/admin/review/found/' + id, null, { params: { status } })
}

export function reviewClaim(id, status) {
  return request.put('/admin/review/claim/' + id, null, { params: { status } })
}

export function deleteItem(type, id) {
  return request.delete('/admin/item/' + type + '/' + id)
}
