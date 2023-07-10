package com.gxcom.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gxcom.reggie.common.CustomException;
import com.gxcom.reggie.entity.Category;
import com.gxcom.reggie.entity.Dish;
import com.gxcom.reggie.entity.Setmeal;
import com.gxcom.reggie.mapper.CategoryMapper;
import com.gxcom.reggie.service.CategoryService;
import com.gxcom.reggie.service.DishService;
import com.gxcom.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    /**
     * 根据id进行删除分类，删除之前进行判断
     * @param id
     */
    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;
    @Override
    public void remove(Long id) {
        /**
         * 查询Dish实体里面对应的表
         */
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper= new LambdaQueryWrapper<>();
        //根据分类id进查询
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        int count1 = dishService.count(dishLambdaQueryWrapper);
        //查询当前分类是否已经关联了菜品,如果已经关联就抛出一个业务异常
        if (count1 > 0){
            //已经关联，抛出业务异常
           throw new CustomException("当前这个分类下关联了菜品，不能删除");
        }
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper= new LambdaQueryWrapper<>();
        //根据分类id进查询
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        int count2 = setmealService.count(setmealLambdaQueryWrapper);
        //查询当前分类是否已经关联了套餐,如果已经关联就抛出一个业务异常
        if (count2 > 0){
            //已经关联，抛出业务异常
            throw new CustomException("当前这个分类下关联了套餐，不能删除");
        }
        super.removeById(id);

    }
}
