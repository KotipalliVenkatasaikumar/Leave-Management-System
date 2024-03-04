package com.employee.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.commonmodels.entity.Employee;
import com.commonmodels.entity.LeaveRequest;
import com.employee.dto.LoginDTO;
import com.employee.service.EmployeeService;

import lombok.extern.slf4j.Slf4j;

@Slf4j

@RestController
@RequestMapping("/employee")
@CrossOrigin(origins = "http://localhost:4200")
public class EmployeeController {
	@Autowired
	private EmployeeService employeeService;

	@GetMapping
	public ResponseEntity<List<Employee>> getAllEmployee() {
		List<Employee> Employees = employeeService.getAllEmployees();
		return ResponseEntity.ok(Employees);
	}

	@PostMapping
	public ResponseEntity<Employee> saveEmployee(@RequestBody Employee employee) {
		Employee saveEmployee = employeeService.saveEmployee(employee);
		return ResponseEntity.ok(saveEmployee);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Employee> getEmployeeById(@PathVariable Integer id) {
		Employee getEmployeeById = employeeService.getEmployeeById(id);
		return ResponseEntity.ok(getEmployeeById);
	}

	@DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable int id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
	@PutMapping("/{id}")
	public ResponseEntity<Employee> updateEmployee(@PathVariable Integer id, @RequestBody Employee updatedEmployee) {
		Employee updateEmployee = employeeService.updateEmployee(id, updatedEmployee);
		return ResponseEntity.ok(updateEmployee);
	}

	@PostMapping("/login")
	public ResponseEntity<Employee> getLoginUser(@RequestBody LoginDTO loginDTO) {

		log.info("Login details {}", loginDTO.getEmail() + " " + loginDTO.getPassword());

		ResponseEntity<Employee> response = employeeService.getLoginUser(loginDTO);

		
		if (response.getStatusCode().is2xxSuccessful()) {

			return response;
		} else {

			return response;
		}
	}

	@GetMapping("/logout")
	public ResponseEntity<String> logout() {

		return ResponseEntity.ok("Logout");

	}

}
