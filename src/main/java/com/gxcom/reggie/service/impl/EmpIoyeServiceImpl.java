package com.gxcom.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gxcom.reggie.common.CustomException;
import com.gxcom.reggie.entity.Employe;
import com.gxcom.reggie.entity.Employee;
import com.gxcom.reggie.mapper.EmpIoyeMapper;
import com.gxcom.reggie.service.EmpIoyeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmpIoyeServiceImpl extends ServiceImpl<EmpIoyeMapper, Employe> implements EmpIoyeService {
    @Override
    @Transactional
    public void removeWithDish(List<Long> ids) {
        LambdaQueryWrapper<Employe> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Employe::getId,ids);
        queryWrapper.eq(Employe::getStatus,1);
        int count = this.count(queryWrapper);
        if (count > 0 ){
            throw new CustomException("账号正在使用中，不能删除");
        }else {
            this.removeByIds(ids);}
    }
}