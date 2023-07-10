package com.gxcom.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gxcom.reggie.entity.Employe;

import java.util.List;

public interface EmpIoyeService extends IService<Employe> {
    void removeWithDish(List<Long> ids);
}
