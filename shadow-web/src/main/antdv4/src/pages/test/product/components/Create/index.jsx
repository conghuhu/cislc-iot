import React, { PureComponent } from 'react';
import { Input, Modal, Form, Upload, Button, Icon, Checkbox, Col, Row, Select } from 'antd';
import { connect } from 'dva';

const FormItem = Form.Item;
const { Option } = Select;
const { TextArea } = Input;
@connect(({ product,devices }) => ({
  product,
  devices,
  loading: product.fetch,
}))
class Create extends PureComponent {
  state = {
    fileList: [],
  };

  onHandle = e => {
    e.preventDefault();
    const { form, handleModalVisible, dispatch, callBackRefresh } = this.props;
    form.validateFields((err, values) => {
      console.log('表单提交的原数据:', { ...values });
      if (!err) {
        const formData = new FormData();
        formData.append('name', values.name);
        formData.append('protocolList', values.protocolList);
        formData.append('serverList', values.serverList);
        formData.append('description', values.description);
        formData.append('encryption', values.encryption);
        formData.append('operateSystem', values.operateSystem);
        //formData.append('xmlFile', this.state.fileList[0]);
        formData.append('deviceId',values.deviceId);
        dispatch({
          type: 'product/create',
          payload: formData,
          callback: () => {
            callBackRefresh();
            handleModalVisible(false);
          },
        });
      }
    });
  };

  render() {
    const { modalVisible, handleModalVisible,devices:{list} } = this.props;
    const { fileList } = this.state;
    console.log(`新增产品显示项目`,list);
    const { form } = this.props;

    const uploadProps = {
      onRemove: (file) => {
        this.setState({
          fileList: [...file],
        });
      },
      beforeUpload: (file) => {
        this.setState({
          fileList: [file],
        })
      },
      fileList,
      // action: 'http://localhost:8080/admin/test/file',
    };
    const children = [
      <Option key="service1">service1</Option>,
      <Option key="service2">service2</Option>,
      <Option key="service3">service3</Option>,
      <Option key="service4">service4</Option>,
    ];
    return (
      <Modal
        destroyOnClose
        title="创建产品"
        visible={modalVisible}
        onOk={this.onHandle}
        onCancel={() => handleModalVisible(false)}
      >
        <FormItem labelCol={{ span: 5 }} wrapperCol={{ span: 15 }} label="产品名称">
          {form.getFieldDecorator('name', {
            rules: [
              {
                required: true,
                message: '请输入产品的名称',
              },
            ],
          })(<Input placeholder="请输入"/>)}
        </FormItem>
        {/*<FormItem labelCol={{ span: 6 }} wrapperCol={{ span: 17 }} label="产品定义文件">
          {form.getFieldDecorator('file', {
            rules: [{ required: true }],
          })(<Upload {...uploadProps}>
            <Button>
              <Icon type="upload"/> 选择产品xml文档
            </Button>
          </Upload>)}
        </FormItem>*/}
        <FormItem labelCol={{ span: 5 }} wrapperCol={{ span: 15, align: 'middle' }} label="物模型">
          {form.getFieldDecorator('deviceId', {
            rules: [{ required: true }],
          })(
            <Select
              style={{ width: '100%' }}
              placeholder="Please select"
            >
              {list.map(d => (
                <Option key={d.id}>{d.deviceName}</Option>
              ))}
            </Select>,
          )}
        </FormItem>
        <FormItem labelCol={{ span: 5 }} wrapperCol={{ span: 15, align: 'middle' }} label="产品协议">
          {form.getFieldDecorator('protocolList', {
            rules: [{ required: true }],
          })(
            <Checkbox.Group style={{ width: '100%' }} onChange={null}>
              <Row>
                <Col span={8}>
                  <Checkbox value="mqtt">MQTT</Checkbox>
                </Col>
                <Col span={8}>
                  <Checkbox value="coap">COAP</Checkbox>
                </Col>
              </Row>
            </Checkbox.Group>,
          )}
        </FormItem>
        <FormItem labelCol={{ span: 5 }} wrapperCol={{ span: 15, align: 'middle' }} label="操作系统">
          {form.getFieldDecorator('operateSystem', {
            rules: [{ required: true }],
          })(
            <Select
              style={{ width: '100%' }}
              placeholder="Please select"
            >
              <Option key="Linux">Linux</Option>
              <Option key="Android">Android</Option>
            </Select>,
          )}
        </FormItem>
        <FormItem labelCol={{ span: 5 }} wrapperCol={{ span: 15, align: 'middle' }} label="加密方式">
          {form.getFieldDecorator('encryption', {
            rules: [{ required: true }],
          })(
            <Select
              style={{ width: '100%' }}
              placeholder="Please select"
            >
              <Option key="none">无加密</Option>
              <Option key="rsa">RSA</Option>
              <Option key="aes">AES</Option>
            </Select>,
          )}
        </FormItem>
        <FormItem labelCol={{ span: 5 }} wrapperCol={{ span: 15 }} label="产品描述">
          {form.getFieldDecorator('description')(<TextArea rows={4} />)}
        </FormItem>
      </Modal>
    );
  }
}

export default Form.create()(Create);
