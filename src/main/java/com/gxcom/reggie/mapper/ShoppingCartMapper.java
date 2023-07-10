package com.gxcom.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gxcom.reggie.entity.Setmeal;
import com.gxcom.reggie.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart>  {
}
