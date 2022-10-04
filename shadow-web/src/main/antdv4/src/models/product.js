import { notification, message } from 'antd';
import {
  create,
  queryProductList,
  deleteProduct,
  packUp,
  updateProduct
} from '@/services/product';

function isEmpty(obj) {
  return obj === undefined || obj === '';
}
const ProductModel = {
  namespace: 'product',
  state: {
    list: [],
    createModalVisible: false,
    editModalVisible: false,
    record: {},
  },
  effects: {
    // 获取产品列表
    *fetch({ payload }, { call, put }) {
      const response = yield call(queryProductList);
      console.log("获取产品列表",response)
      if (response.msgCode === 0) {
        yield put({
          type: 'queryList',
          payload: response.data,
        });
      } else {
        notification.error({
          message: '获取产品列表信息失败',
          description: response.msg,
        });
      }
    },
    // 新增产品信息
    *create({ payload, callback }, { call }) {
      const response = yield call(create, payload);
      console.log("新增产品信息：",response)
      if (response.msgCode === 0) {
        message.success('新增产品信息成功');
        if (callback) callback();
      } else if (response.msgCode === 1) {
        message.error(response.msg);
      } else {
        notification.error({
          message: '新增产品信息失败',
          description: response.msg,
        });
      }
    },
    *delete({ payload, callback }, { call }) {
      const response = yield call(deleteProduct, payload);
      console.log("删除产品信息：",response);
      if (response.msgCode === 0) {
        message.success('删除成功');
        if (callback) callback();
      } else {
        notification.error({
          message: '删除产品信息失败',
          description: response.msg,
        });
      }
    },
    *update({ payload, callback }, { call }) {
      const response = yield call(updateProduct, payload);
      console.log("更新产品信息：",response);
      if (response.msgCode === 0) {
        message.success('修改成功');
        if (callback) callback();
      } else {
        notification.error({
          message: '修改产品信息失败',
          description: response.msg,
        });
      }
    },
    *pack({ payload, callback }, { call }) {
      const response = yield call(packUp, payload);
      console.log("打包：",response);
      if (response.msgCode === 0) {
        message.success('成功');
        if (callback) callback();
      } else {
        notification.error({
          message: '失败',
          description: response.msg,
        });
      }
    },
  },
  reducers: {
    queryList(state, action) {
      return {
        ...state,
        list: action.payload,
      };
    },
    changeCreateModal(state, { payload }) {
      return {
        ...state,
        createModalVisible: payload,
      }
    },
    changeEditModalVisible(state, { payload }) {
      return {
        ...state,
        editModalVisible: payload,
      }
    },
    saveRecord(state, action) {
      return {
        ...state,
        record: action.payload,
      }
    },
  },
};
export default ProductModel;
