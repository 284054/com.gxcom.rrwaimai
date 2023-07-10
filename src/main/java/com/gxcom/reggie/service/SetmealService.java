package com.gxcom.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gxcom.reggie.dto.SetmealDto;
import com.gxcom.reggie.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    //新增套餐同时保存保存套餐和菜品的关联关系
    public void saveWitDish(SetmealDto setmealDto);

    public void deleteWithDish(List<Long> ids);

    public SetmealDto getByIdWithFlavor(Long id);

    public void uptedeWithFlavor(SetmealDto setmealDto);
}
