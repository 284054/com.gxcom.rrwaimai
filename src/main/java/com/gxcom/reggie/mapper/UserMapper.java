package com.gxcom.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gxcom.reggie.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
