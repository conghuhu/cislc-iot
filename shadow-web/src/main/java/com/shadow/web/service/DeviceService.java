package com.shadow.web.service;

import com.shadow.web.mapper.device.DeviceMapper;
import com.shadow.web.mapper.device.DevicePropChildMapper;
import com.shadow.web.mapper.device.DevicePropMapper;
import com.shadow.web.model.device.*;
import com.shadow.web.model.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
 * @Auther: 10413
 * @Date: 2020/9/27 14:34
 * @Description:
 */
@Service
public class DeviceService {

    @Resource
    private DeviceMapper deviceMapper;
    @Resource
    private DevicePropMapper devicePropMapper;
    @Resource
    private DevicePropChildMapper devicePropChildMapper;

    //查
    public Result<List<Device>> selectDevice() {
        DeviceExample example = new DeviceExample();
        return Result.returnSuccess(deviceMapper.selectByExample(example));
    }

    //删
    @Transactional(rollbackFor = Exception.class)
    public Result deleteDevice(Integer id) {
        int ret = deviceMapper.deleteByPrimaryKey(id);
        if (ret == -1) {
            return Result.returnError("删除产品失败");
        }
        return Result.returnSuccess();
    }

    //增
    @Transactional(rollbackFor = Exception.class)
    public Result insertDevice(Device device) {
        int ret = deviceMapper.insertSelective(device);
        if (ret == -1) {
            return Result.returnError("新增产品失败");
        }
        return Result.returnSuccess();
    }

    //查属性
    public Result<List<DeviceProp>> selectDeviceProp(int id) {
        DevicePropExample example = new DevicePropExample();
        example.createCriteria().andDeviceIdEqualTo(id).andDeletedEqualTo(0);
        return Result.returnSuccess(devicePropMapper.selectByExample(example));
    }

    /**
     * 查子属性
     * @param propId 属性id
     * @return 子属性
     */
    public Result<List<DevicePropChild>> selectDevicePropChild(int propId) {
        DevicePropChildExample example = new DevicePropChildExample();
        example.createCriteria().andPropIdEqualTo(propId).andDeletedEqualTo(0);
        return Result.returnSuccess(devicePropChildMapper.selectByExample(example));
    }

    //删属性
    @Transactional(rollbackFor = Exception.class)
    public Result deleteDeviceProp(Integer id) {
        int ret = devicePropMapper.deleteByPrimaryKey(id);
        if (ret == -1) {
            return Result.returnError("删除产品失败");
        }
        return Result.returnSuccess();
    }
    //增属性
    @Transactional(rollbackFor = Exception.class)
    public Result<Integer> insertDeviceProp(DeviceProp prop) {
        int ret = devicePropMapper.insertSelective(prop);
        if (ret == -1) {
            return Result.returnError("新增物模型属性失败");
        }
        return Result.returnSuccess(prop.getId());
    }

    //增
    public Result insertDevicePropChild(DevicePropChild prop){
        int ret = devicePropChildMapper.insertSelective(prop);
        if (ret == -1) {
            return Result.returnError("新增物模型属性子类失败");
        }
        return Result.returnSuccess();
    }

}
