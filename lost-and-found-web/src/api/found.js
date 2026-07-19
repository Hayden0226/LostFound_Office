import request from '@/utils/axios'

export function getFoundList(params) {
  return request.get('/found/list', { params })
}

export function getFoundDetail(id) {
  return request.get('/found/' + id)
}

export function addFoundItem(data) {
  return request.post('/found', data)
}

export function updateFoundItem(id, data) {
  return request.put('/found/' + id, data)
}

export function deleteFoundItem(id) {
  return request.delete('/found/' + id)
}

export function getMyFoundItems(params) {
  return request.get('/found/my', { params })
}
