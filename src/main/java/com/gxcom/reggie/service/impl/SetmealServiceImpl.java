package com.gxcom.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gxcom.reggie.common.CustomException;
import com.gxcom.reggie.dto.DishDto;
import com.gxcom.reggie.dto.SetmealDto;
import com.gxcom.reggie.entity.Dish;
import com.gxcom.reggie.entity.DishFlavor;
import com.gxcom.reggie.entity.Setmeal;
import com.gxcom.reggie.entity.SetmealDish;
import com.gxcom.reggie.mapper.SetmealMapper;
import com.gxcom.reggie.service.SetmeaIDshService;
import com.gxcom.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmeaIDshService setmeaIDshService;
    /**
     * 新增套餐
     *     @Transactional 事务注解要么全成功要么全部失败
     * @param setmealDto
     */
    @Override
    @Transactional
    public void saveWitDish(SetmealDto setmealDto) {
        //保存基本信息，操作setmeal ， 执行insert操作
        this.save(setmealDto);
        //保存套餐和套餐关联的信息，操作setmeal_dish表，执行insert操作
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item)->{
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        setmeaIDshService.saveBatch(setmealDishes);

    }

    /**
     * 删除套餐
     * @param ids
     */
    @Override
    @Transactional
    public void deleteWithDish(List<Long> ids) {
        //select count(*) from Setmeal where id in (1,2,3) and status = 1
        //查询套餐状态，确定是否可用删除
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId,ids);
        queryWrapper.eq(Setmeal::getStatus,1);
        int count = this.count(queryWrapper);
        if (count > 0){
            throw new CustomException("套餐正在销售中，无法删除，如要删除请停止销售");
        }
        //如果可以删除，先删除套餐表中的数据---Setmeal
        this.removeByIds(ids);

        //delete from setmeal_dish where dish_id in (1,2,3)
        LambdaQueryWrapper<SetmealDish> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.in(SetmealDish::getSetmealId,ids);
        //删除关系表中的数据----setmeal_dish
        setmeaIDshService.remove(queryWrapper1);
    }

    /**
     * 数据回显
     * @param id
     * @return
     */
    @Override
    @Transactional
    public SetmealDto getByIdWithFlavor(Long id) {
        //查询菜品的基本信息，从dish表查询
        Setmeal setmeal = this.getById(id);

        //查询菜品对应的口味信息，从dish_flavors表查询
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal,setmealDto);

        //查询套餐菜品信息
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,setmeal.getId());
//        List<SetmealDish> flavors = setmeaIDshService.list(queryWrapper);
        List<SetmealDish> list = setmeaIDshService.list(queryWrapper);
        setmealDto.setSetmealDishes(list);
        return setmealDto;


    }

    /**
     * 修改套餐
     * @param setmealDto
     */
    @Override
    @Transactional
    public void uptedeWithFlavor(SetmealDto setmealDto) {
        //更新setmeal表基本信息
        this.updateById(setmealDto);

        //更新setmeal_dish表信息delete操作
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, setmealDto.getId());
        setmeaIDshService.remove(queryWrapper);

        //更新setmeal_dish表信息insert操作
        List<SetmealDish> SetmealDishes = setmealDto.getSetmealDishes();

        SetmealDishes = SetmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        setmeaIDshService.saveBatch(SetmealDishes);

    }

}
