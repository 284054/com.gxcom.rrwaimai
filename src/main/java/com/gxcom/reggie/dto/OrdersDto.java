package com.gxcom.reggie.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.gxcom.reggie.entity.OrderDetail;
import com.gxcom.reggie.entity.Orders;
import lombok.Data;
import java.util.List;

@Data
public class OrdersDto extends Orders {

    private String userName;

    private String phone;

    private String address;

    private String consignee;

    private List<OrderDetail> orderDetails;

    private int sumNum;



}
