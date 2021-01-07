package com.sso.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sso.core.model.UserInfo;
import com.sso.mapper.TUserMapper;
import com.sso.service.ITUserService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author suning
 * @since 2020-12-06
 */
@Service
public class TUserServiceImpl extends ServiceImpl<TUserMapper, UserInfo> implements ITUserService {


}
