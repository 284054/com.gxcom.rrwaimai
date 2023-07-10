package com.gxcom.reggie.dto;


import com.gxcom.reggie.entity.SetmealDish;
import com.gxcom.reggie.entity.Setmeal;
import lombok.Data;


import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
