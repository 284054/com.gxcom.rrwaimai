package com.gxcom.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gxcom.reggie.dto.OrdersDto;
import com.gxcom.reggie.entity.Orders;

import java.util.List;

public interface OrdersService extends IService<Orders> {
     public void submit(Orders orders);

//     void removeWithDish(List<Long> ids);

}
