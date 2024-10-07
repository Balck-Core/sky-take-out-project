package com.sky.controller.admin;

import com.github.pagehelper.PageHelper;
import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.pojo.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.admin.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Api(tags = "员工管理")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;
    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation("员工登录")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);
        Employee employee = employeeService.login(employeeLoginDTO);
        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);
        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();
        return Result.success(employeeLoginVO);
    }


    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    @ApiOperation("员工退出")
    public Result<String> logout() {
        return Result.success();
    }


    /*
    * 新增员工
    *
    *
    * */
    @PostMapping
    @ApiOperation("新增员工")
    public Result save(@RequestBody EmployeeDTO employeeDTO){
        employeeService.save(employeeDTO);
        return Result.success();
    }

    /*
    * 分页查询员工
    *
    *
    * */
    @GetMapping("/page")
    @ApiOperation("员工列表分页查询")
    public Result page(EmployeePageQueryDTO employeePageQueryDTO){
        PageResult pageResult = employeeService.pageQuery(employeePageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 穷用禁用员工账号
     *
     */
    @PostMapping("/status/{status}")
    @ApiOperation("启用禁用员工账号")
    public Result EnableDisableEmployees(@PathVariable Integer status,Long id){
        employeeService.EnableDisableEmployees(status,id);
        return Result.success();
    }

    /*
    * 根据id查询(编辑用的回显)
    *
    * */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询(编辑用的回显)")
    public Result<Employee> getByid(@PathVariable Long id) {
        Employee employee = employeeService.getByid(id);
        return Result.success(employee);
    }

    /*
    * 编辑员工信息
    *
    * */
    @PutMapping
    @ApiOperation("编辑1员工信息")
    public Result update(@RequestBody Employee employee){
        employeeService.update(employee);
        return Result.success();
    }
}
