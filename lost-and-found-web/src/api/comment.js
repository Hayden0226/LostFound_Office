import request from '@/utils/axios'

export function getComments(itemId, itemType) {
  return request.get('/comment/' + itemId, { params: { itemType } })
}

export function addComment(data) {
  return request.post('/comment', data)
}

export function deleteComment(id) {
  return request.delete('/comment/' + id)
}
