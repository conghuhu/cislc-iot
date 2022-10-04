import { queryCurrent, query as queryUsers } from '../services/user';
import { notification } from 'antd';

const UserModel = {
  namespace: 'user',
  state: {
    currentUser: {},
  },
  effects: {
    //获取当前用户信息
    *fetchCurrent({ callback }, { call, put }) {
      const response = yield call(queryCurrent);
      if (response.msgCode === 0) {
        let data = response.data;
        yield put({
          type: 'saveCurrentUser',
          payload: data,
        });
        if (callback) {
          callback(data);
        }
      } else {
        notification.error({
          message: '获取当前用户基础信息失败',
          description: response.msg,
        });
      }
    },
    *fetch(_, { call, put }) {
      const response = yield call(queryUsers);
      yield put({
        type: 'save',
        payload: response,
      });
    },
  },
  reducers: {
    saveCurrentUser(state, action) {
      return {
        ...state,
        currentUser: action.payload || {}
      };
    },

    changeNotifyCount(
      state = {
        currentUser: {},
      },
      action,
    ) {
      return {
        ...state,
        currentUser: {
          ...state.currentUser,
          notifyCount: action.payload.totalCount,
          unreadCount: action.payload.unreadCount,
        },
      };
    },
  },
};
export default UserModel;
