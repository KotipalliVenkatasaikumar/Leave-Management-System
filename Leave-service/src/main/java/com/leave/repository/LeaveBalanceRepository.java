package com.leave.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.commonmodels.entity.LeaveBalance;
import com.leave.DTO.LeaveBalanceDTO;

@Repository
public interface LeaveBalanceRepository extends JpaRepository<LeaveBalance, Integer> {

	@Query("SELECT lr FROM LeaveBalance lr WHERE lr.employee.employeeId = :employeeId")
	List<LeaveBalance> findByEmployeeId(int employeeId);

	@Query("SELECT new com.leave.DTO.LeaveBalanceDTO(lt.leaveTypeId, lb.leaveBalance, lt.leaveTypeName, concat(lb.leaveBalance, lt.leaveTypeName)) FROM LeaveBalance lb JOIN lb.leaveType lt WHERE lb.employee.employeeId = :employeeId")
	List<LeaveBalanceDTO> findLeaveBalanceByEmployeeId(int employeeId);

}
