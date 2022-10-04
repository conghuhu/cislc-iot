import requestTest from '../utils/requestTest';

// 获取产品列表
export async function queryProductList() {
  return requestTest('/admin/product/list', {
    method: 'POST',
  });
}
// 新增产品信息
export async function create(params) {
  return requestTest('/admin/product/create', {
    method: 'POST',
    body: params,
  });
}
// 删除产品信息
export async function deleteProduct(params) {
  return requestTest('/admin/product/delete', {
    method: 'POST',
    body: params,
  });
}
// 更新产品信息
export async function updateProduct(params) {
  return requestTest('/admin/product/update', {
    method: 'POST',
    body: params,
  });
}
// 打包
export async function packUp(params) {
  return requestTest('/admin/product/packUp', {
    method: 'POST',
    body: params,
  });
}


