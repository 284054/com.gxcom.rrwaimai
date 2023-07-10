package com.gxcom.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gxcom.reggie.entity.AddressBook;
import com.gxcom.reggie.mapper.AddressBookMapper;
import com.gxcom.reggie.service.AddressBookSerice;
import org.springframework.stereotype.Service;

@Service
public class AddressBookSericeImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookSerice {
}
