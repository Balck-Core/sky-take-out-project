package com.sky.mapper.admin;


import com.github.pagehelper.Page;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.pojo.Employee;
import org.apache.ibatis.annotations.*;


public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     *
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);


    /**
     * 插入员工数据
     *
     * @parm employee
     */

    @Insert("insert into employee (name,username,password,phone,sex,id_number,create_time,update_time,create_user,update_user,status)" +
            "values " +
            "(#{name},#{username},#{password},#{phone},#{sex},#{idNumber},#{createTime},#{updateTime},#{createUser},#{updateUser},#{status})")
    void insert(Employee employee);

    /**
     * 分页查询
     */
    Page pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 禁用启用员工帐号状态
     */
    @Update("update employee set  status=#{status} where id = #{id}")
    void EnableDisableEmployees(@Param("status") int status, @Param("id") long id);

    /*
     * 根据id查询员工(回显)
     *
     * */
    @Select("select * from employee where id = #{id}")
    Employee getByid(Long id);

    /*
     * 编辑员工信息
     *
     * */
    void update(Employee employee);
}
