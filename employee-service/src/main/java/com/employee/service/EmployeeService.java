package com.employee.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.commonmodels.entity.*;
import com.employee.dto.LoginDTO;
@Service
public interface EmployeeService {

	List<Employee> getAllEmployees();

	Employee getEmployeeById(Integer employeeId);

	Employee saveEmployee(Employee employee);

	void deleteEmployee(Integer employeeId);

	Employee updateEmployee(Integer id, Employee updatedEmployee);

	ResponseEntity<Employee> getLoginUser(LoginDTO loginDTO);

}
