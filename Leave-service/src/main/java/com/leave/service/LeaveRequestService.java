package com.leave.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.commonmodels.entity.LeaveRequest;

@Service
public interface LeaveRequestService {

	LeaveRequest saveLeaveRequest(LeaveRequest leaveRequest);

	void deleteLeaveRequest(Integer id);

	List<LeaveRequest> getAllLeaveRequest();

	LeaveRequest getLeaveRequestById(Integer id);

	LeaveRequest updateLeaveRequest(Integer id, LeaveRequest updatedLeaveRequest);

	List<LeaveRequest> getLeaveRequestsByEmployeeId(int employeeId);

	
	

}
