import { Avatar, Button, Card, Icon, List, Typography, Modal, Input, Row, Col } from 'antd';
import React, { Component } from 'react';
import { PageHeaderWrapper } from '@ant-design/pro-layout';
import { connect } from 'dva';
import styles from './style.less';
import Create from '@/pages/test/product/components/Create';
import Edit from '@/pages/test/product/components/Edit';
import image from './img/vending_machine.png';

const { Paragraph } = Typography;
const ListItem = List.Item;

@connect(({ product, loading,devices }) => ({
  product,
  devices,
  loading: loading.effects['product/fetch'],
}))
class ProductList extends Component {
  componentDidMount() {
    const { dispatch } = this.props;
    dispatch({
      type: 'product/fetch',
    });
    dispatch({
      type: 'devices/fetch',
    });
  }

  // 修改产品新增框显示状态
  handleCreateModalVisible = visible => {
    const { dispatch } = this.props;
    dispatch({
      type: 'product/changeCreateModal',
      payload: visible,
    });
  };

  // 修改产品编辑框显示状态
  handleEditModalVisible = visible => {
    const { dispatch } = this.props;
    dispatch({
      type: 'product/changeEditModalVisible',
      payload: visible,
    });
  };

  // 回调 刷新
  callBackRefresh = () => {
    const { dispatch } = this.props;
    // console.log('执行回调')
    dispatch({
      type: 'product/fetch',
    });
  };

  // 打包
  handlePackage = item => {
    Modal.confirm({
      title: '确认打包',
      okText: '确认',
      cancelText: '取消',
      onOk: () => {
        document.getElementById(`product${item.id}`).click();
      },
    });
  };

  // 修改事件
  handleEdit = item => {
    const { dispatch } = this.props;
    dispatch({
      type: 'product/saveRecord',
      payload: item,
    });
    this.handleEditModalVisible(true);
  };

  // 删除事件
  handleDelete = item => {
    const { dispatch } = this.props;
    Modal.confirm({
      title: '确认删除产品信息',
      okText: '确认',
      cancelText: '取消',
      onOk: () => {
        dispatch({
          type: 'product/delete',
          payload: item.id,
          callback: () => {
            this.callBackRefresh()
          },
        })
      },
    });
  };

  //搜索事件
  handleFormSubmit = ()=>{
    const { dispatch } = this.props;
    dispatch({
      type: 'product/fetch',
    });
  };

  // 标题操作图标
  extraCardTitle = item => (
      [
        <Icon type="edit" key="edit" onClick={() => {
          this.handleEdit(item)
        }}/>,
        <Icon type="download" key="download" onClick={() => this.handlePackage(item)}/>,
        <Icon type="delete" key="delete" onClick={() => this.handleDelete(item)}/>,
      ]
    );

  render() {
    const {
      product: { list, createModalVisible, editModalVisible },
      loading,
    } = this.props;
    console.log("ssssssssssssssss",list)
    const content = (
      <div className={styles.pageHeaderContent}>
        <p>
          <h3>Type-A</h3>
        </p>
        <p>
          测试用户定义产品的结构，生成对应的xsd文件，用户根据xsd文件生成xml对象的描述文档，后台对应生成java源代码可编译加入spring ioc容器中。
        </p>
        <div className={styles.contentLink}>
          <Row gutter={[24, 8]}>
            <Col span={4}>
              <Button type="primary" onClick={() => this.handleCreateModalVisible(true)}>
                创建产品
              </Button>
            </Col>
            <Col span={20}>
              <Input.Search
                placeholder="产品名称"
                enterButton="搜索"
                onSearch={this.handleFormSubmit}
                style={{ maxWidth: 522, width: '100%' }}
              />
            </Col>
          </Row>
        </div>
      </div>
    );
    const extraContent = (
      <div className={styles.extraImg}>
        <img
          alt="右边的图片"
          src="https://gw.alipayobjects.com/zos/rmsportal/RzwpdLnhmvDJToTdfDPe.png"
        />
      </div>
    );

    return (
      <PageHeaderWrapper content={content} extraContent={extraContent}>
        <div className={styles.cardList}>
          <List
            rowKey="id"
            loading={loading}
            grid={{ gutter: 24, lg: 3, md: 2, sm: 1, xs: 1 }}
            dataSource={list}
            renderItem={item => (
              <ListItem key={item.id}>
                <Card
                  className={styles.card}
                  actions={this.extraCardTitle(item)}
                >
                  <Card.Meta
                    avatar={<Avatar size={84} shape="square" src={image}/>}
                    title={<a>{item.name}</a>}
                    description={
                      <Paragraph ellipsis={2}>
                        <table>
                          <tbody>
                          <tr>
                            <td>物模型: {item.deviceName}</td>
                          </tr>
                          <tr>
                            <td>产品协议: {item.protocolList === ''?'无':item.protocolList}</td>
                          </tr>
                          <tr>
                            <td>打包平台: {item.operateSystem}</td>
                          </tr>
                          <tr>
                            <td>加密方式: {item.encryption === 'none'? '无加密' : item.encryption  === 'rsa' ? '对称加密aes' : '非对称加密rsa'}</td>
                          </tr>
                          <tr>
                            <td>
                              <div className={styles.line} title={item.description}>产品描述: {item.description}
                              </div>
                            </td>
                          </tr>
                          </tbody>
                        </table>
                        <a id={`product${item.id}`} href={`/admin/product/package/download?id=${item.id}`} download={`${item.name}.zip`} />
                      </Paragraph>
                    }/>
                </Card>
              </ListItem>
            )}
          />
        </div>
        <Create
          modalVisible={createModalVisible}
          handleModalVisible={this.handleCreateModalVisible}
          callBackRefresh={this.callBackRefresh}
        />
        <Edit
          modalVisible={editModalVisible}
          handleModalVisible={this.handleEditModalVisible}
          callBackRefresh={this.callBackRefresh}
        />
      </PageHeaderWrapper>
    );
  }
}

export default ProductList;
