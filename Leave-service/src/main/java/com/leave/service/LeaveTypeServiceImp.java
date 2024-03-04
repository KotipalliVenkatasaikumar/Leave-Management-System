package com.leave.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.commonmodels.entity.LeaveType;
import com.leave.repository.LeaveTypeRepository;

@Service
public class LeaveTypeServiceImp implements LeaveTypeService {
	@Autowired
	private LeaveTypeRepository leaveTypeRepository;

	@Override
	public LeaveType saveLeaveType(LeaveType leaveType) {

		return leaveTypeRepository.save(leaveType);
	}

	@Override
	public void deleteLeaveType(int id) {
		leaveTypeRepository.deleteById(id);

	}

	@Override
	public LeaveType getLeaveTypeById(int id) {

		return leaveTypeRepository.getById(id);
	}

	@Override
	public List<LeaveType> getAllLeaveTypes() {

		return leaveTypeRepository.findAll();
	}

	@Override
	public LeaveType updateLeaveType(int id, LeaveType updatedLeaveType) {
		Optional<LeaveType> optionalExistingLeaveType = leaveTypeRepository.findById(id);

		if (optionalExistingLeaveType.isPresent()) {
			LeaveType existingLeaveType = optionalExistingLeaveType.get();

			if (updatedLeaveType.getLeaveTypeName() != null) {
				existingLeaveType.setLeaveTypeName(updatedLeaveType.getLeaveTypeName());
				existingLeaveType.setDefaultLeaves(updatedLeaveType.getDefaultLeaves());
			}
			

			return leaveTypeRepository.save(existingLeaveType);
		} else {

			return null;
		}
	}

}
