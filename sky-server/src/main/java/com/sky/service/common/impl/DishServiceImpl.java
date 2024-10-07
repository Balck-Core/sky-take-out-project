package com.sky.service.common.impl;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.common.DishFlavorMapper;
import com.sky.mapper.common.DishMapper;
import com.sky.mapper.common.SetmealDishMapper;
import com.sky.pojo.Dish;
import com.sky.pojo.DishFlavor;
import com.sky.result.PageResult;
import com.sky.service.common.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    /**
     * 新增菜品和对应的口味
     *
     * @param dishDTO
     */
    @Transactional
    public void saveWithFlavor(DishDTO dishDTO) {

        Dish dish = new Dish();

        BeanUtils.copyProperties(dishDTO, dish);

        //向菜品表插入1条数据
        dishMapper.insert(dish);//后绪步骤实现

        //获取insert语句生成的主键值
        Long dishId = dish.getId();

        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && flavors.size() > 0) {
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishId);
            });
            //向口味表插入n条数据
            dishFlavorMapper.insertBatch(flavors);//后绪步骤实现
        }
    }
    /**
     * 菜品分页查询
     *
     */
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);//后绪步骤实现
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Autowired
    private SetmealDishMapper setmealDishMapper;
    /**
     * 菜品批量删除
     *
     */
    @Transactional // 事务
    public void deleteBatch(List<Long> ids) {
        // 转换 Long 列表为 Integer 列表
        List<Integer> integerIds = ids.stream()
                .map(Long::intValue)
                .collect(Collectors.toList());

        // 查询删除的菜品是否有在起售中的
        Integer count = dishMapper.selectCountByIdsAndStatus(integerIds, StatusConstant.ENABLE);
        if (count > 0) {
            throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
        }

        // 查询删除的菜品是否有被套餐关联的
        Integer count2 = setmealDishMapper.selectCountByDishIds(integerIds);
        if (count2 > 0) {
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        // 删除菜品的口味信息
        dishFlavorMapper.deleteByDishIds(integerIds);

        // 删除菜品信息
        dishMapper.deleteByIds(integerIds);
    }

    /**
     * 根据id查询菜品和对应的口味数据
     *
     */
    public DishVO getByIdWithFlavor(Long id) {
        DishVO dishVO =  dishMapper.selectById(id);

        return dishVO;
    }

    /**
     * 根据id修改菜品基本信息和对应的口味信息
     *
     */
    public void updateWithFlavor(DishDTO dishDTO) {
        //        1.修改dish表
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.update(dish);
//        2.修改dish_flavor表(对于关系表的修改操作)
//        a.先删除原有数据
        dishFlavorMapper.deleteByDishId(dish.getId());
//        b.添加新的数据 -- 批量新增
        List<DishFlavor> flavors = dishDTO.getFlavors();
        flavors.forEach(dishFlavor -> {
            dishFlavor.setDishId(dish.getId());
        });
        dishFlavorMapper.batchSave(flavors);
    }

    /**
     * 根据分类id查询菜品
     *
     *
     */
    public List<Dish> list(Long categoryId) {
        Dish dish = Dish.builder()
                .categoryId(categoryId)
                .status(StatusConstant.ENABLE)
                .build();
        return dishMapper.list(dish);
    }
}