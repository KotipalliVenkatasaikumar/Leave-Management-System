package com.employee.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.commonmodels.entity.Employee;
import com.employee.dto.LoginDTO;
import com.employee.repository.EmployeeRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmployeeServiceImp implements EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;

	public List<Employee> getAllEmployee() {

		return employeeRepository.findAll();
	}

	public Employee saveEmployee(Employee employee) {
		
		Employee emp=employeeRepository.save(employee);
		
		

		return employeeRepository.save(employee);
	}

	public Employee getEmployeeById(Integer id) {

		return employeeRepository.findById(id).orElse(null);
	}

	public void deleteEmployee(Integer id) {
		employeeRepository.deleteById(id);

	}

	@Override
	public Employee updateEmployee(Integer id, Employee updatedEmployee) {

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

		return employeeRepository.findAll();
	}

	@Override
	public ResponseEntity<Employee> getLoginUser(LoginDTO loginDTO) {

		if (loginDTO.getEmail() == null || loginDTO.getPassword() == null) {
			log.info("Bad request {}",loginDTO.getPassword());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST); 
		}


		Employee loggedInEmployee = employeeRepository.findByEmailAndPassword(loginDTO.getEmail(),
				loginDTO.getPassword());

		if (loggedInEmployee != null) {

			return new ResponseEntity<>(loggedInEmployee, HttpStatus.OK);
		} else {

			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

//	private boolean isValidEmail(String email) {
//
//		return email != null && email.matches("[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
//	}

}
