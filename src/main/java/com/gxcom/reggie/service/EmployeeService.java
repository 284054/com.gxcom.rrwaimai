package com.gxcom.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gxcom.reggie.entity.Employee;

import java.util.List;

public interface EmployeeService extends IService<Employee> {
    void removeWithDish(List<Long> ids);
}

