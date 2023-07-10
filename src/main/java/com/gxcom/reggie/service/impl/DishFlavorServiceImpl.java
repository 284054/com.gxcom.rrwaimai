package com.gxcom.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gxcom.reggie.entity.DishFlavor;
import com.gxcom.reggie.mapper.DishFlavorMapper;
import com.gxcom.reggie.service.DishFlavorSerevice;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorSerevice {
}
