package com.leave.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.commonmodels.entity.LeaveType;

@Service
public interface LeaveTypeService {

	List<LeaveType> getAllLeaveTypes();

	LeaveType saveLeaveType(LeaveType leaveType);

	void deleteLeaveType(int id);

	LeaveType getLeaveTypeById(int id);

	LeaveType updateLeaveType(int id, LeaveType updatedLeaveType);

}
