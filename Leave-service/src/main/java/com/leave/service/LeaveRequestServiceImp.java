package com.leave.service;

import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.commonmodels.entity.LeaveBalance;
import com.commonmodels.entity.LeaveRequest;
import com.commonmodels.entity.LeaveType;
import com.leave.repository.LeaveBalanceRepository;
import com.leave.repository.LeaveRequestRepository;

@Service
public class LeaveRequestServiceImp implements LeaveRequestService {
	@Autowired
	private LeaveRequestRepository leaveRequestRepository;

	@Autowired
	private LeaveBalanceService balanceService;
	@Autowired
	private LeaveTypeService leaveTypeService;

	@Autowired
	private LeaveBalanceRepository balanceRepository;

	@Override
	public LeaveRequest saveLeaveRequest(LeaveRequest leaveRequest) {
		leaveRequest.setStatus("Pending");

		// Retrieve existing leave balances for the employee
		List<LeaveBalance> balances = balanceService
				.getLeaveBalanceByEmployeeId(leaveRequest.getEmployeeId().getEmployeeId());

		if (balances.isEmpty()) {

			List<LeaveType> leaveTypes = leaveTypeService.getAllLeaveTypes();

			for (LeaveType leaveType : leaveTypes) {

				LeaveBalance leaveBalance = new LeaveBalance();

				leaveBalance.setEmployee(leaveRequest.getEmployeeId());
				leaveBalance.setLeaveBalance(leaveType.getDefaultLeaves());
				leaveBalance.setLeaveType(leaveType);
				leaveBalance.setYear(Year.now().getValue());

				balances.add(leaveBalance);
			}

			balanceRepository.saveAll(balances);
		}

		int numberOfDays = calculateNumberOfDays(leaveRequest.getStartDate(), leaveRequest.getEndDate());

		balanceService.updateLeaveBalanceForApprovedRequest(leaveRequest, balances, numberOfDays);

		return leaveRequestRepository.save(leaveRequest);
	}

	@Override
	public void deleteLeaveRequest(Integer id) {
		leaveRequestRepository.deleteById(id);

	}

	@Override
	public List<LeaveRequest> getAllLeaveRequest() {
		List<LeaveRequest> all = leaveRequestRepository.findAll();
		List<LeaveRequest> pendingRequests = new ArrayList<>();
		for (LeaveRequest leaveRequest : all) {
			String status = leaveRequest.getStatus();
			if (!status.equalsIgnoreCase("Approved") && !status.equalsIgnoreCase("Rejected")) {
				pendingRequests.add(leaveRequest);
			}
		}
		return pendingRequests;
	}

	@Override
	public LeaveRequest getLeaveRequestById(Integer id) {
		return leaveRequestRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("id not found"));
	}

	@Override
	public LeaveRequest updateLeaveRequest(Integer id, LeaveRequest updatedLeaveRequest) {
		LeaveRequest existingLeaveRequest = leaveRequestRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Leave request not found"));

		existingLeaveRequest.setStartDate(updatedLeaveRequest.getStartDate());
		existingLeaveRequest.setEndDate(updatedLeaveRequest.getEndDate());
		existingLeaveRequest.setLeaveTypeId(updatedLeaveRequest.getLeaveTypeId());
		existingLeaveRequest.setReason(updatedLeaveRequest.getReason());
		existingLeaveRequest.setManagerId(updatedLeaveRequest.getManagerId());
		existingLeaveRequest.setEmployeeId(updatedLeaveRequest.getEmployeeId());
		existingLeaveRequest.setStatus(updatedLeaveRequest.getStatus());
		existingLeaveRequest.setDescription(updatedLeaveRequest.getDescription());

		List<LeaveBalance> balances = balanceService
				.getLeaveBalanceByEmployeeId(updatedLeaveRequest.getEmployeeId().getEmployeeId());

		int numberOfDays = calculateNumberOfDays(updatedLeaveRequest.getStartDate(), updatedLeaveRequest.getEndDate());

		balanceService.updateLeaveBalanceForApprovedRequest(existingLeaveRequest, balances, numberOfDays);

		return leaveRequestRepository.save(existingLeaveRequest);
	}

	@Override
	public List<LeaveRequest> getLeaveRequestsByEmployeeId(int employeeId) {
		return leaveRequestRepository.findByEmployeeId(employeeId);
	}

	public int calculateNumberOfDays(LocalDate startDate, LocalDate endDate) {
		return (int) java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1;
	}

}
