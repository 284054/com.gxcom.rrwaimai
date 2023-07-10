package com.gxcom.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gxcom.reggie.common.R;
import com.gxcom.reggie.entity.Category;
import com.gxcom.reggie.entity.Dish;
import com.gxcom.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类的管理
 */
@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 新增菜品分类
     * @param category
     * @return
     */
@PostMapping
    public R<String> save(@RequestBody Category category){
        log.info("category:{}",category);
        categoryService.save(category);
        return R.success("新增分类成功");

    }
   @GetMapping("/page")
        public R<Page> page(int page, int pageSize){
        //分页构造器
        Page<Category> pageInfo= new Page<>(page,pageSize);
        //条件构造器
       LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //添加一个排序条件
        queryWrapper.orderByAsc(Category::getSort);
        //进行分页查询
        categoryService.page(pageInfo,queryWrapper);
       return R.success(pageInfo);
    }

    /**
     *
     * 根据id删除分类
     * @param id
     * @return
     */
    @DeleteMapping
      public R<String> detele(Long id){
       log.info("删除分类 ，id{}",id);
//       categoryService.removeById(ids);
       categoryService.remove(id);
       return R.success("删除分类成功！");
    }

    /**
     * 将传达过来的数据封装成Category
     * 返回一个普通的字符串类型String
     * @param category
     * @return
     */
    @PutMapping
    public R<String> uptate(@RequestBody Category category){
        log.info("修改分类信息：{}",category);
        categoryService.updateById(category);
        return R.success("修改分类成功");
    }

    /**
     * 根据条件查询分类数据
     * @param category
     * @return
     */
    @GetMapping("/list")
    public R<List<Category>> liis(Category category){
        //条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //添加条件
        queryWrapper.eq(category.getType() !=null,Category::getType,category.getType());
        //添加排序条件
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);


        List<Category> list = categoryService.list(queryWrapper);
        return R.success(list);
    }
    /**
     * 修改菜品状态(启售，停售)
     * @param status
     * @param ids
     * @return
     */

}

