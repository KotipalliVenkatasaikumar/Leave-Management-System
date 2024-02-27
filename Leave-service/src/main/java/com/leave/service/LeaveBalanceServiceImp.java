package com.leave.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.commonmodels.entity.LeaveBalance;
import com.commonmodels.entity.LeaveRequest;
import com.commonmodels.entity.LeaveType;
import com.leave.DTO.LeaveBalanceDTO;
import com.leave.exceptions.InsufficientLeaveBalanceException;
import com.leave.repository.LeaveBalanceRepository;

@Service
public class LeaveBalanceServiceImp implements LeaveBalanceService {

	@Autowired
	private LeaveBalanceRepository leaveBalanceRepository;
	@Autowired
	private EmailSenderService emailSenderService;

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

	public void validateLeaveBalanceForRequest(LeaveRequest leaveRequest, List<LeaveBalance> balances,
			int numberOfBusinessDays) {

		LeaveType requestedLeaveType = leaveRequest.getLeaveTypeId();

		LeaveBalance leaveBalance = balances.stream()
				.filter(balance -> balance.getLeaveType().equals(requestedLeaveType)).findFirst().orElse(null);

		if (leaveBalance == null) {
			throw new InsufficientLeaveBalanceException("Leave balance not found for the requested leave type");
		}

		// Check if the leave balance is sufficient for the requested number of days
		if (leaveBalance.getLeaveBalance() < numberOfBusinessDays) {
			throw new InsufficientLeaveBalanceException("Insufficient leave balance for the requested leave type");
		}
	}

	@Override
	public void updateLeaveBalanceForApprovedRequest(LeaveRequest leaveRequest, List<LeaveBalance> balances,
			int numberOfDays) {

		for (LeaveBalance balance : balances) {
			if (balance.getLeaveType().getLeaveTypeId() == leaveRequest.getLeaveTypeId().getLeaveTypeId()) {
				if (leaveRequest.getStatus().equalsIgnoreCase("Pending")) {
					balance.setLeaveBalance(balance.getLeaveBalance() - numberOfDays);

					// Send email notification for pending request
					String emailBody = "Dear " + leaveRequest.getEmployee().getEmployeeName() + ",\n\n"
							+ "Your leave request has been submitted successfully for the following period:\n"
							+ "Start Date: " + leaveRequest.getStartDate() + "\n" + "End Date: "
							+ leaveRequest.getEndDate() + "\n" + "Reason: " + leaveRequest.getReason() + "\n\n"
							+ "Thank you for your submission.\n\n" + "Best regards,\n" + "Leave Management Team \n"
							+ "CoreNuts Technologies";

					emailSenderService.sendSimpleEmail(leaveRequest.getEmployee().getEmail(),
							"Leave Request - Notification", emailBody);
				} else if (leaveRequest.getStatus().equalsIgnoreCase("Approved")) {
					// Send email notification for approved request
					String emailBody = "Dear " + leaveRequest.getEmployee().getEmployeeName() + ",\n\n"
							+ "CoreNuts Technologies is pleased to inform you that your leave request has been approved for the following period:\n"
							+ "Start Date: " + leaveRequest.getStartDate() + "\n" + "End Date: "
							+ leaveRequest.getEndDate() + "\n" + "Number of days: " + leaveRequest.getNumberOfDays()
							+ "\n\n" + "Thank you for your patience and cooperation.\n\n" + "Best regards,\n"
							+ "Leave Management Team";

					emailSenderService.sendSimpleEmail(leaveRequest.getEmployee().getEmail(),
							"Leave Request Approved - Notification", emailBody);

				} else if (leaveRequest.getStatus().equalsIgnoreCase("Rejected")) {

					balance.setLeaveBalance(balance.getLeaveBalance() + numberOfDays);

					// Send email notification for rejected request
					String emailBody = "Dear " + leaveRequest.getEmployee().getEmployeeName() + ",\n\n"
							+ "CoreNuts Technologies regrets to inform you that your leave request has been rejected for the following period:\n"
							+ "Start Date: " + leaveRequest.getStartDate() + "\n" + "End Date: "
							+ leaveRequest.getEndDate() + "\n" + "Number of days: " + leaveRequest.getNumberOfDays()
							+ "\n" + "Reason for rejection: " + leaveRequest.getDescription() + "\n\n"
							+ "Thank you for your understanding.\n\n" + "Best regards,\n" + "Leave Management Team";

					emailSenderService.sendSimpleEmail(leaveRequest.getEmployee().getEmail(),
							"Leave Request Rejected - Notification", emailBody);
				}

				// Assuming only one leave type should match, so exit loop
				break;
			}
		}
		leaveBalanceRepository.saveAll(balances);
	}

	@Override
	public List<LeaveBalanceDTO> getLeaveBalanceDtoByEmployeeId(Integer employeeId) {
		return leaveBalanceRepository.findLeaveBalanceByEmployeeId(employeeId);
	}

}
