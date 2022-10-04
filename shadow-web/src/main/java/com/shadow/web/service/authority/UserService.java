package com.shadow.web.service.authority;

import com.shadow.web.mapper.security.UserMapper;
import com.shadow.web.model.result.Result;
import com.shadow.web.model.security.User;
import com.shadow.web.model.security.UserExample;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class UserService {

    @Resource
    UserMapper mapper;

    /**
     * 根据用户登录名查询用户
     *
     * @param loginName 用户登陆账号
     * @return 用户user对象
     */
    public Result<User> findByUserName(String loginName) {
        UserExample example = new UserExample();
        example.createCriteria().andDeletedEqualTo(0).andUsernameEqualTo(loginName);
        try {
            Result<List<User>> findRet = findByExample(example);
            if (!findRet.success()) {
                log.error("findByUserName failed: {}", findRet.msg());
                return Result.returnError("findByUserName failed:" + findRet.msg());
            }
            List<User> list = findRet.value();
            if (null == list || list.isEmpty()) {
                log.error("findByUserName failed: can not find User by LoginName= {}", loginName);
                return Result.returnError("根据用户登录名[" + loginName + "]查不到用户");
            }
            return Result.returnSuccess(list.get(0));
        } catch (Exception e) {
            log.error("findByUserName error: {}", e);
            return Result.returnError("findByUserName error:" + e);
        }
    }

    public Result<Integer> countByUserName(String loginName) {
        UserExample example = new UserExample();
        example.createCriteria().andDeletedEqualTo(0).andUsernameEqualTo(loginName);
        return countByExample(example);
    }

    /**
     * @param userId 用户ID
     * @return 获取用户密码
     * @auther: wangzhendong
     * @date: 2018/11/14 15:23
     */
    public Result<String> findUserPwdByUserId(Integer userId) {
        Result<User> findRet = findById(userId);
        if (!findRet.success()) {
            log.error("findUserPwdByUserId failed: {}", findRet.msg());
            return Result.returnError("findUserPwdByUserId failed:" + findRet.msg());
        }
        User user = findRet.value();
        String password = user.getPassword();
        if (null == password) {
            log.error("findUserPwdByUserId failed: can not find password by userId= {}", userId);
            return Result.returnError("根据用户ID=" + userId + "查不到密码");
        }
        return Result.returnSuccess(password);
    }

    /**
     * @param userId   用户ID
     * @param password 明文密码
     * @return 修改用户密码
     * @auther wangzhendong
     * @date 2018/12/21 18:19
     */
    public Result<Boolean> changeUserPwd(Integer userId, String password) {
        /* step1: 明文密码转密文*/
        Result<String> encryptRet = encryptPassword(password);
        if (!encryptRet.success()) {
            log.error("changeUserPwd failed: {}", encryptRet.msg());
            return Result.returnError("修改密码失败:" + encryptRet.msg());
        }
        String encryptPwd = encryptRet.value();
        /* step2: 更新*/
        Result<User> findRet = findById(userId);
        if (!findRet.success()) {
            log.error("changeUserPwd failed: {}" , findRet.msg());
            return Result.returnError("修改密码失败:" + findRet.msg());
        }
        User user = findRet.value();
        user.setPassword(encryptPwd);
        Result<Boolean> updateRet = update(user);
        if (!updateRet.success()) {
            log.error("changeUserPwd failed: {}" , updateRet.msg());
            return Result.returnError("修改密码失败:" + updateRet.msg());
        }
        return Result.returnSuccess();
    }

    /**
     * @param password 需要加密的明文
     * @return BCrypt 加密
     * @auther wangzhendong
     * @date 2018/12/21 18:21
     */
    public Result<String> encryptPassword(String password) {
        try {
            return Result.returnSuccess(BCrypt.hashpw(password, BCrypt.gensalt()));
        } catch (Exception e) {
            log.error("encryptPassword error: {}", e);
            return Result.returnError("encryptPassword error");
        }
    }


    /**
     * @return 校验用户密码是否正确
     * @param userId 用户ID
     * @param password 用户密码 明文
     * @auther wangzhendong
     * @date 2018/12/22 16:46
     */
    public Result<Boolean> checkUserPwd(Integer userId, String password){
        /** step1: 根据用户ID查询密码*/
        Result<String> queryRet = findUserPwdByUserId(userId);
        if(!queryRet.success()) {
            log.error("checkUserPwd failed:  {}" , queryRet.msg());
            return Result.returnError("checkUserPwd failed: " + queryRet.msg());
        }
        String userPwd = queryRet.value();
        /** step2: 检查密码是否匹配*/
        Result<Boolean> checkRet = checkPwd(password, userPwd);
        if(!checkRet.success()) {
            log.error("checkUserPwd failed: {}" , checkRet.msg());
            return Result.returnError("checkUserPwd failed: " + checkRet.msg());
        }
        /** step3: 返回比较结果*/
        return Result.returnSuccess(checkRet.value());
    }

    /**
     * 检查密码是否匹配
     * @param password  明文密码
     * @param pwdInDb   数据库里面经过BCrypt加密后的密码
     * @return
     */
    private Result<Boolean> checkPwd(String password, String pwdInDb){
        try {
            return Result.returnSuccess(BCrypt.checkpw(password, pwdInDb));
        }catch(Exception e) {
            log.error("checkPwd error: {}" , e);
            return Result.returnError("checkPwd error:" + e);
        }
    }

    public Result<List<User>> findByExample(UserExample example) {
        try {
            return Result.returnSuccess(mapper.selectByExample(example));
        }catch(Exception e) {
            log.error("findByExample error:" + e);
            return Result.returnError("findByExample error:" + e);
        }
    }

    public Result<User> findById(Integer id) {
        try {
            return Result.returnSuccess(mapper.selectByPrimaryKey(id));
        }catch(Exception e) {
            log.error("findById error:" + e);
            return Result.returnError("findById error");
        }
    }

    public Result<Integer> countByExample(UserExample example){
        try {
            return Result.returnSuccess(mapper.countByExample(example));
        }catch(Exception e) {
            log.error("countByExample error:" + e);
            return Result.returnError("countByExample error:" + e);
        }
    }

    public Result<Integer> create(User record) {
        try {
            int ret = mapper.insertSelective(record);
            if(-1 == ret) {
                log.error("create failed: insert return -1");
                return Result.returnError("create failed: insert return -1");
            }
            return Result.returnSuccess(record.getId());
        }catch(Exception e) {
            log.error("create error:" + e);
            return Result.returnError("create error:" + e);
        }
    }

    public Result<Boolean> update(User record) {
        try {
            int ret = mapper.updateByPrimaryKeySelective(record);
            if(-1 == ret) {
                log.error("update failed: update return -1");
                return Result.returnError("update failed: update return -1");
            }
            return Result.returnSuccess();
        }catch(Exception e) {
            log.error("update error:" + e);
            return Result.returnError("update error:" + e);
        }
    }

    public Result<Boolean> delete(Integer id) {
        try {
            int ret = mapper.deleteByPrimaryKey(id);
            if(-1 == ret) {
                log.error("delete failed: delete return -1");
                return Result.returnError("delete failed: delete return -1");
            }
            return Result.returnSuccess();
        }catch(Exception e) {
            log.error("delete error:" + e);
            return Result.returnError("delete error:" + e);
        }
    }

}
