package com.shizy.service.user;

import com.baomidou.mybatisplus.plugins.Page;
import com.shizy.entity.user.UserDto;
import com.shizy.entity.user.UserPo;
import com.shizy.entity.user.UserVo;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author shizy
 * @since 2019-08-19
 */
public interface UserService {

    public static final String cacheKey = UserVo.class.getSimpleName();

    public UserPo queryDetailPo(String id);

    public UserVo queryDetailVo(String id);

    public Page queryList(UserDto dto, Page page);

    public String add(UserPo po);

    public boolean delete(String id);

    public boolean updateById(UserPo po);

}
