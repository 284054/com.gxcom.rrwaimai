package com.gxcom.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gxcom.reggie.common.CustomException;
import com.gxcom.reggie.dto.DishDto;
import com.gxcom.reggie.entity.Dish;
import com.gxcom.reggie.entity.DishFlavor;
import com.gxcom.reggie.mapper.DishMapper;
import com.gxcom.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DishServiceImol extends ServiceImpl<DishMapper,Dish> implements DishService {


    @Autowired
     private DishFlavorServiceImpl dishFlavorService;
    /**
     * 新增菜品，同时保存对应的口味数据
     * @param dishDto
     */

    //涉及到多张表的操作需要加入事务控制@Transactional还要在启动项添加事务开启@EnableTransactionManagement
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        //保存菜品的基本信息到菜品表dish
        this.save(dishDto);

        Long dishId = dishDto.getId();//菜品id

        //菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());
        //保存菜品口味数据到菜品口味表dish_flavor
        dishFlavorService.saveBatch(flavors);

    }

    @Override
    @Transactional
    public DishDto getByIdWithFlavor(Long id) {
        //查询菜品的基本信息，从dish表查询
        Dish dish = this.getById(id);

        //查询菜品对应的口味信息，从dish_flavors表查询
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dish.getId());
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(flavors);
        return dishDto;
    }

    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
     this.updateById(dishDto);

     LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
     queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());

     dishFlavorService.remove(queryWrapper);
     List<DishFlavor> flavors = dishDto.getFlavors();

        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
    }

    @Override
    @Transactional
    public void removeWithDish(List<Long> ids) {
        /**
         * 删除套餐，同时需要删除套餐和菜品的关联数据
         * @param ids
         */
            //select count(*) from Dish where id in (1,2,3) and status = 1
            //查询菜品状态，确定是否可用删除
            LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper();
            queryWrapper.in(Dish::getId,ids);
            queryWrapper.eq(Dish::getStatus,1);

            int count = this.count(queryWrapper);
            if(count > 0){
                //如果不能删除，抛出一个业务异常
                throw new CustomException("菜品正在售卖中，不能删除");
            }

            //如果可以删除，先删除菜品表中的数据---Dish
            this.removeByIds(ids);

            //delete from dish_flavor where dish_id in (1,2,3)
            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.in(DishFlavor::getDishId,ids);
            //删除关系表中的数据----dish_flavor
             dishFlavorService.remove(lambdaQueryWrapper);
        }

//    @Override
//    public void statusWithDish(List<Long> ids) {
//        log.info(ids.toString());
//        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper();
//        queryWrapper.in(Dish::getId,ids);
//
//    }



}
