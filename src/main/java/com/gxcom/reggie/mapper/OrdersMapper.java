package com.gxcom.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gxcom.reggie.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {
}
