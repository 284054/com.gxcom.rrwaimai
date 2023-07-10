package com.gxcom.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gxcom.reggie.common.R;
import com.gxcom.reggie.entity.Employe;
import com.gxcom.reggie.entity.Employee;
import com.gxcom.reggie.service.EmpIoyeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/employe")
public class EmpIoyeController {
    @Autowired
    private EmpIoyeService empIoyeService;

    /**
     * 员工登录
     *
     * @param
     * @return
     */
    @PostMapping("/login")
    public R<Employe> login(HttpServletRequest request, @RequestBody Employe employe) {

        //1、将页面提交的密码password进行md5加密处理
        String password = employe.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //2、根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employe> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(Employe::getUsername, employe.getUsername());
        Employe emp = empIoyeService.getOne(queryWrapper);
//        3、如果没有查询到则返回登录失败结果
        if (emp == null) {
            return R.error("登录失败");
        }


//        4、密码比对，如果不一致则返回登录失败结果
        if (!emp.getPassword().equals(password)) {
            return R.error("登录失败");
        }

//        5、查看员工状态，如果为已禁用状态，则返回员工已禁用结果
        if (emp.getStatus() == 0) {
            return R.error("账号已禁用");
        }

//        6、登录成功，将员工id存入Session并返回登录成功结果
        request.getSession().setAttribute("employee", emp.getId());
        return R.success(emp);

    }

    /**
     * 员工退出
     *
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        //清除Session保存当前员工的id
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");

    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        log.info("page = {},pageSize = {},name = {}", page, pageSize, name);
        //构造分页查询
        Page pageInfo = new Page(page, pageSize);
        //构造条件
        LambdaQueryWrapper<Employe> queryWrapper = new LambdaQueryWrapper();
        //添加条件
        queryWrapper.like(StringUtils.isNotEmpty(name), Employe::getName, name);
        //添加排序条件
        queryWrapper.orderByDesc(Employe::getCreateTime);
        //执行查询
        empIoyeService.page(pageInfo, queryWrapper);

        return R.success(pageInfo);

    }
    /**
     * 添加员工
     * @param request
     * @param employe
     * @return
     */
    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employe employe){
        log.info("新增员工，员工信息：{}",employe.toString());
        //设置初始密码为123456，用md5加密
        employe.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

        empIoyeService.save(employe);

        return R.success("新增员工成功");
    }
    /**
     * 根据员工id修改员工信息
     * @param request
     * @param employe
     * @return
     */
    @PutMapping
    public R<String> ubtate(HttpServletRequest request,@RequestBody Employe employe){
        log.info(employe.toString());
        long id = Thread.currentThread().getId();
        log.info("线程id为： {}",id);


        empIoyeService.updateById(employe);
        return R.success("员工信息修改成功");

    }
    /**
     * 根据员工id查询员工信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employe> getById(@PathVariable Long id){
        log.info("根据员工id查询信息...");
        Employe employee1 = empIoyeService.getById(id);
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
        empIoyeService.removeWithDish(ids);
        return R.success("删除成功");
    }

}
