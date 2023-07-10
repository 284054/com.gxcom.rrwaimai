package com.gxcom.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gxcom.reggie.dto.DishDto;
import com.gxcom.reggie.entity.Dish;

import java.util.List;

public interface DishService extends IService<Dish> {

    //新增菜品，同时插入菜品对应的口味数据，需要操作两张表：dish、dish_flavor
    public void saveWithFlavor(DishDto dishDto);
    //根据id才行菜品信息和口味信息
    public DishDto getByIdWithFlavor(Long id);
     //更新菜品信息同时还要更新菜品口味信息
    public void updateWithFlavor(DishDto dishDto);

    public void removeWithDish(List<Long> ids);

//    public void statusWithDish(List<Long> ids);

//    public void statusWithDish(List<Long> ids);

}
