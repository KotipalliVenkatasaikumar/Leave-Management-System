package com.leave.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.commonmodels.entity.LeaveRequest;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Integer> {

	@Query("SELECT lr FROM LeaveRequest lr WHERE lr.employee.employeeId = :employeeId")
	List<LeaveRequest> findByEmployeeId(int employeeId);
}
