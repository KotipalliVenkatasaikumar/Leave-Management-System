package com.employee.service;

import java.util.List;

import com.commonmodels.entity.*;




public interface EmployeeService {
	
	
	List<Employee> getAllEmployees();

	Employee getEmployeeById(Long employeeId);

	Employee saveEmployee(Employee employee);

	void deleteEmployee(Long employeeId);

	Employee updateEmployee(Long id, Employee updatedEmployee);

	
}
