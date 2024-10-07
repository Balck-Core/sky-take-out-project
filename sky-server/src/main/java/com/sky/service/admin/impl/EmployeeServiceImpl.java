package com.sky.service.admin.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.EmployeeNameExistException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.admin.EmployeeMapper;
import com.sky.pojo.Employee;
import com.sky.result.PageResult;
import com.sky.service.admin.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Service
public class EmployeeServiceImpl  implements EmployeeService {

    @Resource
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();//

        //1、根据用户名查询数据库中的数据  select * from employee where username = #{username}
        Employee employee = employeeMapper.getByUsername(username); //

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        // TODO 后期需要进行md5加密，然后再进行比对
        password = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }
        //3、返回实体对象
        return employee;
    }

    @Override
    public void save(EmployeeDTO employeeDTO) {
        //判断用户名是否存在
        Employee dbEmployee = employeeMapper.getByUsername(employeeDTO.getUsername());
        if(dbEmployee!=null){
            throw new EmployeeNameExistException("当前昵称太火了(*^_^*)，请换一个吧");
        }


        Employee employee = new Employee();

        //对象属性拷贝
        BeanUtils.copyProperties(employeeDTO, employee);

        //设置账号的状态，默认正常状态，1表示正常，0表示锁定
        employee.setStatus(StatusConstant.ENABLE);

        //设置密码，密码默认为123456

        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));

        //设置当前记录的创建时间和修改时间
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());

        //设置当前记录创建人的id和修改人的id
        employee.setCreateUser(BaseContext.getCurrentId());//后期修改
        employee.setUpdateUser(BaseContext.getCurrentId());

        employeeMapper.insert(employee);//后续步骤定义
    }

    /**
     *
     * 分页查询
     */
    @Override
    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        //从pojo中获取分页查询第1页的配置
        PageHelper.startPage(employeePageQueryDTO.getPage(),employeePageQueryDTO.getPageSize());
        //第1页的数据
        Page page1 =  employeeMapper.pageQuery(employeePageQueryDTO);

        return new PageResult(page1.getTotal(),page1.getResult());
    }

    /*
    * 启用禁用员工账号
    *
    * */
    @Override
    public void EnableDisableEmployees(Integer status,Long id){
        employeeMapper.EnableDisableEmployees(status,id);
    }

    /*
    * 根据id查询(回显)
    *
    * */
    public Employee getByid(Long id){
        Employee employee = employeeMapper.getByid(id);
        employee.setPassword("++++");
        return employee;
    }

    /*编辑员工信息
    *
    * */
    public void update(Employee employee){
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(BaseContext.getCurrentId());
        employeeMapper.update(employee);
    }
}
