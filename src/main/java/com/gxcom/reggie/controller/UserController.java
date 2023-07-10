package com.gxcom.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gxcom.reggie.common.R;
import com.gxcom.reggie.entity.User;
import com.gxcom.reggie.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;
//    @PostMapping("/sendMsg")
//    public R<String> sendMsg(@RequestBody User user, HttpSession session){
//        //获取手机号
//        String phone = user.getPhone();
//        if (StringUtils.isNotEmpty(phone)){
//            //生成随机验证码
//            String code = ValidateCodeUtils.generateValidateCode(4).toString();
//            log.info("code: {}",code);
//            //调用阿里云的apl
////            SMSUtils.sendMessage("菩提阁","SMS_460805598",phone,code);
//            //转存验证码用于比对
//            session.setAttribute(phone,code);
//            return R.success("手机验证码发送成功");
//        }
//
//        return R.error("短信发送失败");
//    }
    @PostMapping("/login")
    public R<User> login(@RequestBody User user,HttpServletRequest request){
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone,user.getPhone());
        User one = userService.getOne(queryWrapper);
        if (one == null ){
            return R.error("登录失败");
        }
        request.getSession().setAttribute("user",one.getId());

        return R.success(one);

//        log.info(map.toString());
//        //获取手机号
//        String phone = map.get("phone").toString();
//        //获取验证码
//        String code = map.get("code").toString();
//        //从session中获取保存的验证码
//        Object codeIdSerssion = session.getAttribute(phone);
//        //比对
//        if (codeIdSerssion != null && codeIdSerssion.equals(code)){
//            LambdaQueryWrapper<User> queryWrapper= new LambdaQueryWrapper<>();
//            queryWrapper.eq(User::getPhone, phone);
//            User user = userService.getOne(queryWrapper);
//            if (user == null){
//                user = new User();
//                user.setPhone(phone);
//                user.setStatus(1);
//                userService.save(user);
//            }
//            session.setAttribute("user",user.getId());
//            return R.success(user);
//
//        }
//        return R.error("登录失败");
    }

    /**
     * 移动端用户退出
     * @param request
     * @return
     */
    @PostMapping("/loginout")
    public R<String> loginout(HttpServletRequest request){

        request.getSession().removeAttribute("user");
        return R.success("退出成功");
    }
}
