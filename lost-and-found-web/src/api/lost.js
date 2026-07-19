import request from '@/utils/axios'

export function getLostList(params) {
  return request.get('/lost/list', { params })
}

export function getLostDetail(id) {
  return request.get('/lost/' + id)
}

export function addLostItem(data) {
  return request.post('/lost', data)
}

export function updateLostItem(id, data) {
  return request.put('/lost/' + id, data)
}

export function deleteLostItem(id) {
  return request.delete('/lost/' + id)
}

export function getMyLostItems(params) {
  return request.get('/lost/my', { params })
}
