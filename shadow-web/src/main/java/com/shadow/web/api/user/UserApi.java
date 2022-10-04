package com.shadow.web.api.user;

import com.alibaba.fastjson.JSONObject;
import com.shadow.web.model.result.ApiResult;
import com.shadow.web.model.result.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Auther: wangzhendong
 * @Date: 2019/11/26 16:29
 * @Description:
 */
@Controller
@RequestMapping("/admin/user")
@Slf4j
public class UserApi {

    /**
     * 获取用户信息
     * @return
     */
    @GetMapping("/info")
    public ApiResult getCurUserInfo() {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(1);
        userInfo.setLoginName("admin");
        return ApiResult.returnSuccess(JSONObject.toJSONString(userInfo));
    }

    /*
    *     @ApiOperation(value="获取用户基础信息", notes="获取当前用户的基本信息")
    @GetMapping("/info")
    public ApiResult getCurUserInfo() {
        Result<UserInfo> ret = userInfoService.findCurUserInfo();
        if(!ret.success()) {
            log.error("getUserInfoForAvatar failed: {}",  ret.msg());
            return ApiResult.returnError(1,ret.msg());
        }
        return ApiResult.returnSuccess(JSONObject.toJSONString(ret.value()));
    }
    * */
}
