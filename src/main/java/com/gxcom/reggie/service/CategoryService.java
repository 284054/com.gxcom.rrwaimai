package com.gxcom.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gxcom.reggie.entity.Category;

public interface CategoryService extends IService<Category> {
    public void remove(Long id);
}
