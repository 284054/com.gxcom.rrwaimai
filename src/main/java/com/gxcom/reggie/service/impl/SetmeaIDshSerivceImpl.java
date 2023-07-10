package com.gxcom.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gxcom.reggie.entity.SetmealDish;
import com.gxcom.reggie.mapper.SetmeaIDshMapper;
import com.gxcom.reggie.service.SetmeaIDshService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SetmeaIDshSerivceImpl extends ServiceImpl<SetmeaIDshMapper, SetmealDish> implements SetmeaIDshService {
}
