package com.employee.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.commonmodels.entity.*;
import com.employee.repository.EmployeeRepository;


@Service
public class EmployeeServiceImp implements EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;

	public List<Employee> getAllEmployee() {

		return employeeRepository.findAll();
	}

	public Employee saveEmployee(Employee employee) {

		return employeeRepository.save(employee);
	}

	public Employee getEmployeeById(Long id) {

		return employeeRepository.findById(id).orElse(null);
	}

	public void deleteEmployee(Long id) {
		employeeRepository.deleteById(id);

	}

	@Override
	public Employee updateEmployee(Long id, Employee updatedEmployee) {

		Optional<Employee> optionalExistingEmployee = employeeRepository.findById(id);
		if (optionalExistingEmployee.isPresent()) {
			Employee existingEmployee = optionalExistingEmployee.get();

			existingEmployee.setEmployeeName(updatedEmployee.getEmployeeName());
			existingEmployee.setRole(updatedEmployee.getRole());
			existingEmployee.setGender(updatedEmployee.getGender());
			existingEmployee.setEmail(updatedEmployee.getEmail());
			existingEmployee.setPassword(updatedEmployee.getPassword());

			return employeeRepository.save(existingEmployee);
		}
		return null;
	}

	@Override
	public List<Employee> getAllEmployees() {
		// TODO Auto-generated method stub
		return employeeRepository.findAll();
	}

}
