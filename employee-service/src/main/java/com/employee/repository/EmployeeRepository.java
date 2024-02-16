package com.employee.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.commonmodels.entity.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

//	Optional<Employee> findById(Long id);

	Employee findByEmailAndPassword(String email, String password);
}
