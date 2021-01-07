package com.sso.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sso.core.model.UserInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author suning
 * @since 2020-12-06
 */
@Mapper
public interface TUserMapper extends BaseMapper<UserInfo> {

}
