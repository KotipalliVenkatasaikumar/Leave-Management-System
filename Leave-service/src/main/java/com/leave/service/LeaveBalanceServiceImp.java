package com.leave.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.commonmodels.entity.LeaveBalance;
import com.commonmodels.entity.LeaveRequest;
import com.leave.repository.LeaveBalanceRepository;

@Service
public class LeaveBalanceServiceImp implements LeaveBalanceService {

	@Autowired
	private LeaveBalanceRepository leaveBalanceRepository;

	@Override
	public LeaveBalance saveLeaveBalance(LeaveBalance leaveBalance) {

		return leaveBalanceRepository.save(leaveBalance);
	}

	@Override
	public LeaveBalance getLeaveBalanceById(Integer id) {
		// TODO Auto-generated method stub
		return leaveBalanceRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("id not found"));
	}

	@Override
	public List<LeaveBalance> getAllLeaveBalance() {

		return leaveBalanceRepository.findAll();
	}

	@Override
	public List<LeaveBalance> getLeaveBalanceByEmployeeId(Integer employeeId) {

		return leaveBalanceRepository.findByEmployeeId(employeeId);
	}

	@Override
	public void updateLeaveBalanceForApprovedRequest(LeaveRequest leaveRequest, List<LeaveBalance> balances,
			int numberOfDays) {

		for (LeaveBalance balance : balances) {
			if (balance.getLeaveType().getLeaveTypeId() == leaveRequest.getLeaveTypeId().getLeaveTypeId()) {

				if (leaveRequest.getStatus().equalsIgnoreCase("Pending")) {
					balance.setLeaveBalance(balance.getLeaveBalance() - numberOfDays);
				}
				
				
				else if (leaveRequest.getStatus().equalsIgnoreCase("Rejected")) {
					balance.setLeaveBalance(balance.getLeaveBalance() + numberOfDays);
				}

				break;
			}
		}

		leaveBalanceRepository.saveAll(balances);
	}

}
