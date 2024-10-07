package com.sky.mapper.common;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.enumeration.OperationType;
import com.sky.pojo.Dish;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     *
     *
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    /**
     * 插入菜品数据
     *
     */
    void insert(Dish dish);

    /**
     * 菜品分页查询
     *
     */
    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 根据菜品id和状态查询数量
     *
     */
    Integer selectCountByIdsAndStatus(@Param("ids") List<Integer> ids,
                                      @Param("status") Integer status);
    //批量删除菜品
    void deleteByIds(@Param("ids") List<Integer> ids);

    //查询菜品和菜品的口味
    DishVO selectById(Long dishId);

    /**
     * 根据id动态修改菜品数据
     *
     */
    @AutoFill(value = OperationType.UPDATE)
    void update(Dish dish);

    /**
     * 动态条件查询菜品
     *
     *
     */
    List<Dish> list(Dish dish);

    /**
     * 根据套餐id查询菜品
     */
    @Select("select a.* from dish a left join setmeal_dish b on a.id = b.dish_id where b.setmeal_id = #{setmealId}")
    List<Dish> getBySetmealId(Long setmealId);
}