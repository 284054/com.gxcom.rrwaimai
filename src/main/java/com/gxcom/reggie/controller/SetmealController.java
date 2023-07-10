package com.gxcom.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gxcom.reggie.common.R;
import com.gxcom.reggie.dto.DishDto;
import com.gxcom.reggie.dto.SetmealDto;

import com.gxcom.reggie.entity.Category;
import com.gxcom.reggie.entity.Setmeal;
import com.gxcom.reggie.service.CategoryService;
import com.gxcom.reggie.service.SetmeaIDshService;
import com.gxcom.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 套餐管理
 */
@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {
    @Autowired
    private SetmeaIDshService setmeaIDshService;
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private CategoryService categoryService;

   @PostMapping
   public R<String> save(@RequestBody SetmealDto setmealDto){
       log.info("套餐信息：{}",setmealDto);
       setmealService.saveWitDish(setmealDto);
       return R.success("新增套餐成功");
   }

    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        //构造分页查询器对象,页面需要什么数据服务端就要返回什么数据，Setmeal不满足,页面没有显示categoryName（菜品分类）
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        Page<SetmealDto> dishDtoPage = new Page<>();
        //条件构造器
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper();
        //过滤条件
        queryWrapper.like(name != null, Setmeal::getName, name);
        //排序条件，根据时间进行降序排序
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        //执行分页查询
        setmealService.page(pageInfo,queryWrapper);
        //对象拷贝
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");

        List<Setmeal> recrds = pageInfo.getRecords();

        List<SetmealDto> list = recrds.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item,setmealDto);
            //分类id
            Long categoryId = item.getCategoryId();
            //根据分类id查询分类对象
            Category category = categoryService.getById(categoryId);
            if(category != null){
                //分类名称
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());
         dishDtoPage.setRecords(list);

        return R.success(dishDtoPage);
    }

    /**
     * 套餐删除
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        log.info("ids: {}",ids);
        setmealService.deleteWithDish(ids);
        return R.success("套餐删除成功");
    }
    /**
     * 修改菜品状态(启售，停售)
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> sale(@PathVariable int status, String[] ids){
        for(String id: ids){
            Setmeal setmeal = setmealService.getById(id);
            setmeal.setStatus(status);
            setmealService.updateById(setmeal);
        }

        return R.success("修改成功");
    }

    /**
     * 数据回显
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<SetmealDto> get(@PathVariable Long id){
        log.info("id : {}",id);
        SetmealDto setmealDto = setmealService.getByIdWithFlavor(id);
        return R.success(setmealDto);
    }

    /**
     * 套餐修改成功
     * @param setmealDto
     * @return
     */
    @PutMapping
    public R<String> uptede(@RequestBody SetmealDto setmealDto){
        log.info("setmealDto:{}",setmealDto);
        setmealService.uptedeWithFlavor(setmealDto);
        return R.success("套餐修改成功");
    }

    /**
     * 前端套餐查询
     * @param setmeal
     * @return
     */
    @GetMapping("/list")
    public R<List<Setmeal>> list( Setmeal setmeal){
        log.info("setmeal: {}",setmeal);
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        //判断CategoryId(分类id)是否为空
        queryWrapper.eq(setmeal.getCategoryId() != null,Setmeal::getCategoryId,setmeal.getCategoryId());
        //判断getStatus(状态 0:停用 1:启用)是否为空
        queryWrapper.eq(setmeal.getStatus() != null,Setmeal::getStatus,setmeal.getStatus());
        //排序CreateTime创建时间
        queryWrapper.orderByAsc(Setmeal::getCreateTime);
        List<Setmeal> list = setmealService.list(queryWrapper);
        return R.success(list);
    }
}
