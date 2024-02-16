package com.leave.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.commonmodels.entity.LeaveBalance;
import com.commonmodels.entity.LeaveRequest;

@Service
public interface LeaveBalanceService {

	LeaveBalance saveLeaveBalance(LeaveBalance leaveBalance);

	LeaveBalance getLeaveBalanceById(Integer id);

	List<LeaveBalance> getAllLeaveBalance();

	List<LeaveBalance> getLeaveBalanceByEmployeeId(Integer employeeId);

	 void updateLeaveBalanceForApprovedRequest(LeaveRequest leaveRequest, List<LeaveBalance> balances,
			int numberOfDays) ;



	
	
	

}
