package com.shizy.service.user.impl;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.shizy.entity.user.UserDto;
import com.shizy.entity.user.UserPo;
import com.shizy.entity.user.UserVo;
import com.shizy.mapper.user.UserMapper;
import com.shizy.service.user.UserService;
import com.shizy.utils.auth.IdUtil;
import com.shizy.utils.bean.BeanUtil;
import com.shizy.utils.query.QueryUtil;
import com.shizy.utils.redis.CacheUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author shizy
 * @since 2019-08-19
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CacheUtil<String, String, UserVo> cacheUtil;

    /***********************************************/

    @Override
    public UserPo queryDetailPo(String id) {
        if (StringUtils.isBlank(id)) {
            return null;
        }
        return userMapper.selectById(id);
    }

    @Override
//    @Cacheable(value = cacheKey, key = "#id")
    public UserVo queryDetailVo(String id) {
        if (StringUtils.isBlank(id)) {
            return null;
        }

        UserVo userVo = null;

        userVo = cacheUtil.getHash(cacheKey, id);
        if (userVo != null) {
            return userVo;
        }

        UserPo userPo = userMapper.selectById(id);
        if (userPo == null) {
            return null;
        }

        userVo = BeanUtil.copyParam2Entity(userPo, new UserVo());
        cacheUtil.putHash(cacheKey, id, userVo);
        return userVo;
    }

    @Override
    public Page queryList(UserDto dto, Page page) {
        Wrapper wrapper = QueryUtil.getEntityCondition(dto, new UserPo());

        if (dto.getNameAndAccount() != null && wrapper != null) {
            String param = dto.getNameAndAccount();
            wrapper.andNew()
                    .like("user_account", param)
                    .or()
                    .like("user_name", param);
        }

        List<UserPo> listPo = userMapper.selectPage(page, wrapper);

        List<UserVo> listVo = BeanUtil.copyParam2EntityList(listPo, new UserVo());
        page.setRecords(listPo);

        return page;
    }

    /***********************************************/

    /**
     * 若使用@Cacheable做缓存，返回值必须为要缓存的数据，且不方便设置过期时间、缓存格式，缓存在代码中的位置不可控，为了方便还是自己缓存好些
     * <p>
     * 缓存清理见 ClearRedisCache.java
     */

    @Override
    public String add(UserPo po) {

        String id = IdUtil.genUUID();
        po.setUserId(id);

        int result = userMapper.insert(po);
        if (result > 0) {
            cacheUtil.putHash(cacheKey, po.getUserId(), BeanUtil.copyParam2Entity(po, new UserVo()));
            return id;
        }
        return null;
    }

    @Override
    public boolean delete(String id) {

        if(StringUtils.isBlank(id)){
            return false;
        }

        int result = userMapper.deleteById(id);
        if (result > 0) {
            cacheUtil.deleteHash(cacheKey, id);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateById(UserPo po) {
        if(StringUtils.isBlank(po.getUserId())){
            return false;
        }

        int result = userMapper.updateById(po);

        if (result > 0) {
            cacheUtil.putHash(cacheKey, po.getUserId(), BeanUtil.copyParam2Entity(po, new UserVo()));
            return true;
        }
        return false;
    }

    /***********************************************/


}












