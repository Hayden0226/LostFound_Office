import request from '@/utils/axios'

export function addClaim(data) {
  return request.post('/claim', data)
}

export function getClaims(itemId, itemType) {
  return request.get('/claim/' + itemId, { params: { itemType } })
}

export function getMyClaims() {
  return request.get('/claim/my')
}

export function getApprovedClaim(itemId, itemType) {
  return request.get('/claim/approved', { params: { itemId, itemType } })
}
