package com.shadow.web.api.authority;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shadow.web.model.authority.JwtUser;
import com.shadow.web.model.result.ApiResult;
import com.shadow.web.model.result.Result;
import com.shadow.web.model.security.User;
import com.shadow.web.service.authority.JwtTokenUtil;
import com.shadow.web.service.authority.PermissionService;
import com.shadow.web.service.authority.RSAService;
import com.shadow.web.service.authority.UserService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class AuthenticationApi {
    @Value("${jwt.header}")
    private String tokenHeader;

    @Value("${jwt.route.authentication.pre-auth}")
    private String superPermissionName;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Resource
    UserDetailsService userDetailsService;

    @Autowired
    UserService userService;

    @Autowired
    PermissionService permissionService;

    @Autowired
    RSAService rsaService;

    /**
     * @return ?????????????????????ID
     * @auther wangzhendong
     * @date 2019/10/21 16:32
     */
    private Integer getCurUserId() {
        JwtUser userDetails = (JwtUser) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        return userDetails.getId();
    }

    /**
     * @param request ????????????????????????
     * @return ???????????????????????????RSA??????????????????
     * @auther wangzhendong
     * @date 2019/10/21 16:34
     */
    @PostMapping("${jwt.route.authentication.pre-auth}")
    public ApiResult preAuth(HttpServletRequest request) {
        HttpSession session = request.getSession();
        /** step1: ?????????????????????*/
        Result<KeyPair> ret = rsaService.generateKeyPair();
        if (!ret.success()) {
            return ApiResult.returnError(1, "preAuth failed:" + ret.msg());
        }
        KeyPair key = ret.value();
        /** step2: ????????????session??????*/
        RSAPrivateKey privateKey = (RSAPrivateKey) key.getPrivate();
        session.setAttribute(RSAService.PrivateKeySessionAttr, privateKey);
        /** step3: ?????????????????????*/
        RSAPublicKey publicKey = (RSAPublicKey) key.getPublic();
        Base64 base64 = new Base64();
        String encoded = base64.encodeToString(publicKey.getEncoded());
        return ApiResult.returnSuccess("{\"encoded\":\"" + encoded + "\"}");
    }

    /**
     * @param input   ?????????????????? username ????????? ????????????
     *                password ??????  ????????????
     * @param request ????????????????????????
     * @return ??????
     * @auther wangzhendong
     * @date 2018/12/21 16:50
     */
    @PostMapping("${jwt.route.authentication.path}")
    public ApiResult login(@RequestBody Map<String, String> input, HttpServletRequest request) {
        /** step1: ????????????*/
        String username = input.get("username");
        String password = input.get("password");
        if (null == username || username.isEmpty()) {
            return ApiResult.returnError(1, "error:input param username is null");
        }
        /** step2: ????????????????????????*/
        Result<String> ret = rsaService.decryptPwd(request, password);
        if (!ret.success()) {
            return ApiResult.returnError(1, "??????????????????:" + ret.msg());
        }
        String realPwd = ret.value();
        /** step3: ???????????????token*/
        try {
            final JwtUser userDetails = (JwtUser) userDetailsService.loadUserByUsername(username);
            String token = jwtTokenUtil.generateToken(userDetails);
            JSONObject data = new JSONObject();
            data.put("token", token);
            data.put("userId", userDetails.getId());
            data.put("currentAuthority", username); //????????????????????? ??????????????????????????????????????????ID
            /** step4: ?????????session???????????????*/
            request.getSession().removeAttribute(RSAService.PrivateKeySessionAttr);

            final Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, realPwd));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return ApiResult.returnSuccess(data.toString());
        } catch (UsernameNotFoundException e) {
            log.error("login error: {}", e);
            return ApiResult.returnError(1, "????????????????????????????????????????????????????????????");
        } catch (DisabledException e) {
            log.error("login error: {}", e);
            return ApiResult.returnError(1, "???????????????");
        } catch (LockedException e) {
            log.error("login error: {}", e);
            return ApiResult.returnError(1, "??????????????????");
        } catch (BadCredentialsException e) {
            log.error("login error:", e);
            return ApiResult.returnError(1, "???????????????????????????????????????");
        }
    }

    /**
     * @param request ??????????????????????????????
     * @return ????????????
     * @auther wangzhendong
     * @date 2018/12/21 17:07
     */
    @PostMapping("${jwt.route.authentication.logout}")
    public ApiResult logout(HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) (SecurityContextHolder
                .getContext().getAuthentication());
        JwtUser userDetails = (JwtUser) (authenticationToken.getPrincipal());

        User user = new User();
        user.setId(userDetails.getId());
        user.setLastLogoutTime(new Timestamp(System.currentTimeMillis()));
        //???token????????????
        Result<Boolean> ret = userService.update(user);
        if (!ret.success()) {
            log.error("logout failed: update user lastLogoutTime failed:" + ret.msg());
            return ApiResult.returnError(1, "????????????: ??????????????????????????????" + ret.msg());
        }
        //??????session
        HttpSession session = request.getSession();
        if (null != session) {
            session.invalidate();
        }

        //??????????????????,security filter????????????context??????
        //SecurityContextHolder.getContext().setAuthentication(null);
        Map<String, Object> result = new HashMap<>();
        result.put("result", "success");
        result.put("message", "????????????");
        return ApiResult.returnSuccess(JSON.toJSONString(result));
    }

    //????????? ??????
    @Data
    private class ChangePasswordCtx {
        private Integer userId;
        private String newPwd;
    }

    /**
     * @return ???????????? ???????????????????????????????????????????????????????????????????????????
     * @param input ????????? oldPassword  newPassword ???????????????
     * @param request ?????????????????????
     * @auther wangzhendong
     * @date 2018/12/22 16:50
     */
    @PostMapping("${jwt.route.authentication.change-pwd}")
    public ApiResult changePassword(@RequestBody Map<String, Object> input, HttpServletRequest request) {
        /* step1 ????????????????????? */
        Result<ChangePasswordCtx> preRet = preChangeOwnPwd(input, request);
        if (!preRet.success()) {
            log.error("changPassword failed:" + preRet.msg());
            return ApiResult.returnError(1, "??????????????????:" + preRet.msg());
        }
        ChangePasswordCtx ctx = preRet.value();
        /* step2 ?????????????????? */
        //??????Rsa?????????????????? ?????????????????? ???????????????????????????
        Result<String> decryptRet = rsaService.decryptPwd(request, ctx.getNewPwd());
        if (!decryptRet.success()) {
            log.error("changePassword failed: " + decryptRet.msg());
            return ApiResult.returnError(1, "??????????????????:" + decryptRet.msg());
        }
        String decryptNewPwd = decryptRet.value();
        //??????????????????
        Result<Boolean> ret = userService.changeUserPwd(ctx.getUserId(), decryptNewPwd);
        if (!ret.success()) {
            log.error("changPassword failed:" + ret.msg());
            return ApiResult.returnError(1, "??????????????????:" + ret.msg());
        }
        return ApiResult.returnSuccess("");
    }
    /**
     * @return: ????????????
     * @auther: wangzhendong
     * @date: 2018/11/10 15:38
     */
    private Result<Boolean> checkUserPwd(Integer userId, String pwd, HttpServletRequest request) {
        /* step1: ???????????? */
        Result<String> decryptRet = rsaService.decryptPwd(request, pwd);
        if (!decryptRet.success()) {
            log.error("checkUserPwd failed: " + decryptRet.msg());
            return Result.returnError("checkUserPwd failed: " + decryptRet.msg());
        }
        String password = decryptRet.value();
        /* step2: ???????????????????????? */
        return userService.checkUserPwd(userId, password);
    }

    /**
     * @return ??????????????????????????????
     * @auther wangzhendong
     * @date 2018/12/22 16:49
     */
    private Result<ChangePasswordCtx> preChangeOwnPwd(Map<String, Object> input, HttpServletRequest request) {
        /* step1 ?????????????????? */
        String oldPwd = (String) input.get("oldPassword");
        String newPwd = (String) input.get("newPassword");
        if (StringUtils.isEmpty(oldPwd)) {
            log.error("preChangeOwnPwd failed: input param oldPassword is null or empty");
            return Result.returnError("input param oldPassword is null or empty");
        }
        if (StringUtils.isEmpty(newPwd)) {
            log.error("preChangeOwnPwd failed: input param newPassword is null or empty");
            return Result.returnError("input param newPassword is null or empty");
        }
        /* step2 ??????????????????ID */
        JwtUser userDetails = (JwtUser) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        Integer userId = userDetails.getId();
        /* step3 ??????????????? */
        Result<Boolean> checkRet = checkUserPwd(userId, oldPwd, request);
        if (!checkRet.success()) {
            log.error("preChangeOwnPwd failed:" + checkRet.msg());
            return Result.returnError("preChangeOwnPwd failed:" + checkRet.msg());
        }
        if (!checkRet.value()) {
            log.error("preChangeOwnPwd failed: input param oldPassword is not correct");
            return Result.returnError("?????????????????????");
        }
        /* step4 ???????????? */
        ChangePasswordCtx ctx = new ChangePasswordCtx();
        ctx.setUserId(userId);
        ctx.setNewPwd(newPwd);
        return Result.returnSuccess(ctx);
    }

    /**
     * ????????????
     * ??????????????????????????????????????????
     *
     * @param input
     * @param response
     */
    @PostMapping(value = "/admin/checkPermission")
    public ApiResult checkPermission(@RequestBody Map<String, String> input, HttpServletResponse response) {
        String authority = input.get("authority");
        if (StringUtils.isEmpty(authority)) {
            return ApiResult.returnError(1, "input param authority is null");
        }
        /** step1: ??????????????????ID*/
        Integer userId = getCurUserId();
        /** step2: ????????????????????????????????????*/
        Result<Boolean> ret = permissionService.checkHasPermission(userId, authority);
        if (!ret.success()) {
            log.error("checkPermission failed:" + ret.msg());
            return ApiResult.returnError(1, "????????????????????????:" + ret.msg());
        }
        //??????????????????????????????????????????????????????????????????status=400
        if (!ret.value()) {
            response.setStatus(400);
        }
        return ApiResult.returnSuccess("");
    }

    /**
     * ???????????????????????????????????????
     * ?????????????????????????????????token???????????????????????????401???
     * ?????????????????????????????????????????????????????????????????????
     */
    @GetMapping(value = "/admin/checkLogin")
    public void checkLogin(HttpServletResponse response) {
        if (null != SecurityContextHolder.getContext().getAuthentication()) {
            return;
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }
    }

    /**
     * ???????????????????????????????????????????????????????????????
     *
     * @param response
     */
    @PostMapping(value = "/admin/authorities")
    public ApiResult getPermission(HttpServletResponse response) {
        /** step1: ??????????????????ID*/
        Integer userId = getCurUserId();
        /** step2: ?????????????????????????????????*/
        Result<List<String>> ret = permissionService.findNamesByUserId(userId);
        if (!ret.success()) {
            log.error("checkPermission failed when getUserAuthority:" + ret.msg());
            return ApiResult.returnError(1, "????????????????????????:" + ret.msg());
        }
        if (null == ret.value()) {
            return ApiResult.returnSuccess("[]");
        }
        return ApiResult.returnSuccess(JSON.toJSONString(ret.value()));
    }

}
