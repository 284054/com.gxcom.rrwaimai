package com.gxcom.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gxcom.reggie.common.CustomException;
import com.gxcom.reggie.common.R;
import com.gxcom.reggie.entity.Employee;
import com.gxcom.reggie.entity.Setmeal;
import com.gxcom.reggie.service.EmployeeService;
import com.gxcom.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;


//员工登录


@Slf4j
@RestController
@RequestMapping("/employee")
public class EmpIoyeeController {

    @Autowired
    private EmployeeService employeeService;
    /**
     * 员工登录
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request,@RequestBody Employee employee){

        //1、将页面提交的密码password进行md5加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //2、根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);
//        3、如果没有查询到则返回登录失败结果
        if (emp == null){
            return  R.error("登录失败");
        }


//        4、密码比对，如果不一致则返回登录失败结果
        if (!emp.getPassword().equals(password)){
            return R.error("登录失败");
        }

//        5、查看员工状态，如果为已禁用状态，则返回员工已禁用结果
        if (emp.getStatus() == 0){
            return R.error("账号已禁用");
        }

//        6、登录成功，将员工id存入Session并返回登录成功结果
         request.getSession().setAttribute("employee",emp.getId());
        return  R.success(emp);

    }

    /**
     * 员工退出
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        //清除Session保存当前员工的id
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");

    }

    /**
     * 添加员工
     * @param request
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee){
        log.info("新增员工，员工信息：{}",employee.toString());
        //设置初始密码为123456，用md5加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

        //获取当前时间
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());

        //获取当前登录的id
//       Long empId = (Long) request.getSession().getAttribute("employee");

//       employee.setCreateUser(empId);
       //最后更新人id
//        employee.setUpdateUser(empId);

        employeeService.save(employee);

        return R.success("新增员工成功");
    }
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize,String name){
        log.info("page = {},pageSize = {},name = {}",page,pageSize,name);
        //构造分页查询
        Page pageInfo = new Page(page,pageSize);
        //构造条件
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();
        //添加条件
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
         //添加排序条件
        queryWrapper.orderByDesc(Employee::getCreateTime);
        //执行查询
        employeeService.page(pageInfo,queryWrapper);

        return R.success(pageInfo);
    }

    /**
     * 根据员工id修改员工信息
     * @param request
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> ubtate(HttpServletRequest request,@RequestBody Employee employee){
        log.info(employee.toString());
//        Long empId = (Long) request.getSession().getAttribute("employee");
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser(empId);
        long id = Thread.currentThread().getId();
        log.info("线程id为： {}",id);


        employeeService.updateById(employee);
        return R.success("员工信息修改成功");

    }

    /**
     * 根据员工id查询员工信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        log.info("根据员工id查询信息...");
        Employee employee1 = employeeService.getById(id);
        if (employee1!=null){
            return R.success(employee1);
        }
        return R.error("没有查询到该员工信息");
    }

    /**
     *
     * 根据id进行删除
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids) {
        log.info("ids:{}", ids);
//        employeeService.removeById(ids);
        employeeService.removeWithDish(ids);
        return R.success("删除成功");
    }

}
