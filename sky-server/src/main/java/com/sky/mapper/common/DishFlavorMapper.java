package com.sky.mapper.common;


import com.sky.pojo.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DishFlavorMapper {
    /**
     * 批量插入口味数据
     *
     */
    void insertBatch(List<DishFlavor> flavors);

    /**
     * 根据主键删除菜品口味
     *
     */
    void deleteByDishIds(@Param("ids") List<Integer> ids);

    @Delete("delete from dish_flavor where dish_id=#{id}")
    void deleteByDishId(Long id);

    void batchSave(List<DishFlavor> flavors);
}