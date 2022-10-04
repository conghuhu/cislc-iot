import React, {useState, useEffect, useRef, PureComponent} from 'react';
import {Button, Modal, Table} from "antd";
import PropType from "@/pages/test/devices/Create/PropType";
import {connect} from "dva";
import Create from "@/pages/test/devices/Create/DevicePropCreate";

@connect(({devices, loading}) => ({
  devices,
  loading: loading.effects['devices/fetch'],
}))
export default class VirtualTable extends PureComponent {

  handleCreateModalVisible(data) {
    const { dispatch } = this.props;
    dispatch({
      type: 'devices/changePropCreateModal',
      payload: data,
    });
  }

  handleModalVisible(visible){
    const{dispatch} = this.props;
    dispatch({
      type: 'devices/changePropCreateModal',
      payload: visible,
    });
  }

  deleteRecord = record => {
    const {dispatch,devices:{propChildList}} = this.props;
    Modal.confirm({
      title: '确认删除',
      okText: '确认',
      cancelText: '取消',
      onOk: () => {
        const nowList = propChildList.filter(data =>{
          return data.propChildName !== record.propChildName;
        });
        dispatch({
          type: 'devices/queryPropChildList',
          payload: nowList,
        });
        this.handleModalVisible(false);
      },
    });
  };

  render() {
    const {devices: {propCreateVisible,propChildList}} = this.props;
    const structColumns = [
      {title: '参数名称', dataIndex: 'propChildTag', width: 150},
      {title: '参数类型', dataIndex: 'construction', width: 100},
      {title: '操作', dataIndex: 'option', width: 50,render: (text, record) => (
          <span>
          <a type="dashed" onClick={() => this.deleteRecord(record)}>
                删除
          </a>
        </span>
        )},
    ];
    return (
      <>
        <Table
          columns={structColumns}
          dataSource={propChildList}
          pagination={false}
        />
        <Button
          style={{
            width: '100%',
            marginTop: 16,
            marginBottom: 8,
          }}
          type="dashed"
          onClick={() => this.handleCreateModalVisible(true)}
          icon="plus"
        >
          新增
        </Button>
        <PropType
          visible={propCreateVisible}
          handleModalVisible={this.handleCreateModalVisible}
          callBackRefresh={this.callBackRefresh}
        />
      </>
    )
  }
}
