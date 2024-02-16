package com.leave.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.commonmodels.entity.LeaveBalance;

@Repository
public interface LeaveBalanceRepository extends JpaRepository<LeaveBalance, Integer> {
	
	
	@Query("SELECT lr FROM LeaveBalance lr WHERE lr.employee.employeeId = :employeeId")
	List<LeaveBalance> findByEmployeeId(int employeeId);

}
