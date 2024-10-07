package com.sky.service.common;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

import java.util.List;

/**
 * @Description SetmealService
 * @Author CoreryBlack
 * @Date 2024-10-05
 */
public interface SetmealService {

    /**
     * 新增套餐，同时需要保存套餐和菜品的关联关系
     *
     */
    void saveWithDish(SetmealDTO setmealDTO);

    /**
     * 分页查询
     * @param setmealPageQueryDTO
     *
     */
    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 批量删除套餐
     *
     */
    void deleteBatch(List<Long> ids);

    /**
     * 根据id查询套餐和关联的菜品数据
     */
    SetmealVO getByIdWithDish(Long id);

    /**
     * 修改套餐
     */
    void update(SetmealDTO setmealDTO);

    /**
     * 套餐起售、停售
     */
    void startOrStop(Integer status, Long id);
}