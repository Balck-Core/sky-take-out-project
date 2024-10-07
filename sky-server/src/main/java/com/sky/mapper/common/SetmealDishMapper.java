package com.sky.mapper.common;

import com.sky.pojo.SetmealDish;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SetmealDishMapper {
    /**
    	根据菜品id查询数量
    */
    Integer selectCountByDishIds(@Param("ids") List<Integer> ids);

    /**
     * 批量保存套餐和菜品的关联关系
     *
     */
    void insertBatch(List<SetmealDish> setmealDishes);

    /**
     * 根据套餐id删除套餐和菜品的关联关系
     *
     */
    @Delete("delete from setmeal_dish where setmeal_id = #{setmealId}")
    void deleteBySetmealId(Long setmealId);
}