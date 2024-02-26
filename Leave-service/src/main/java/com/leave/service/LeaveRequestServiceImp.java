package com.leave.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.commonmodels.entity.Holiday;
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

	@Autowired
	private HolidayService holidayService;

	@Autowired
	private EmailSenderService emailSenderService;

//	@Override
//	public LeaveRequest saveLeaveRequest(LeaveRequest leaveRequest) {
//		leaveRequest.setStatus("Pending");
//
//		// Retrieve existing leave balances for the employee
//		List<LeaveBalance> balances = balanceService
//				.getLeaveBalanceByEmployeeId(leaveRequest.getEmployeeId().getEmployeeId());
//
//		if (balances.isEmpty()) {
//
//			List<LeaveType> leaveTypes = leaveTypeService.getAllLeaveTypes();
//
//			for (LeaveType leaveType : leaveTypes) {
//
//				LeaveBalance leaveBalance = new LeaveBalance();
//
//				leaveBalance.setEmployee(leaveRequest.getEmployeeId());
//				leaveBalance.setLeaveBalance(leaveType.getDefaultLeaves());
//				leaveBalance.setLeaveType(leaveType);
//				leaveBalance.setYear(Year.now().getValue());
//
//				balances.add(leaveBalance);
//			}
//
//			balanceRepository.saveAll(balances);
//		}
//
//		int numberOfBusinessDays = calculateNumberOfBusinessDays(leaveRequest.getStartDate(),
//				leaveRequest.getEndDate());
//
//		balanceService.updateLeaveBalanceForApprovedRequest(leaveRequest, balances, numberOfBusinessDays);
//
//		return leaveRequestRepository.save(leaveRequest);
//	}

//	@Override
//	public LeaveRequest saveLeaveRequest(LeaveRequest leaveRequest) {
//		leaveRequest.setStatus("Pending");
//
//		// Retrieve existing leave balances for the employee
//		List<LeaveBalance> balances = balanceService
//				.getLeaveBalanceByEmployeeId(leaveRequest.getEmployeeId().getEmployeeId());
//
//		if (balances.isEmpty()) {
//
//			List<LeaveType> leaveTypes = leaveTypeService.getAllLeaveTypes();
//
//			for (LeaveType leaveType : leaveTypes) {
//
//				LeaveBalance leaveBalance = new LeaveBalance();
//
//				leaveBalance.setEmployee(leaveRequest.getEmployeeId());
//				leaveBalance.setLeaveBalance(leaveType.getDefaultLeaves());
//				leaveBalance.setLeaveType(leaveType);
//				leaveBalance.setYear(Year.now().getValue());
//
//				balances.add(leaveBalance);
//			}
//
//			balanceRepository.saveAll(balances);
//		}
//
//		int numberOfBusinessDays = calculateNumberOfBusinessDays(leaveRequest.getStartDate(),
//				leaveRequest.getEndDate());
//
//		// Validate leave balance
//		balanceService.validateLeaveBalanceForRequest(leaveRequest, balances, numberOfBusinessDays);
//
//		// If validation passes, update leave balance
//		balanceService.updateLeaveBalanceForApprovedRequest(leaveRequest, balances, numberOfBusinessDays);
//
//		return leaveRequestRepository.save(leaveRequest);
//	}
//	
//	
//	public int calculateNumberOfBusinessDays(LocalDate startDate, LocalDate endDate) {
//		int businessDays = 0;
//		LocalDate date = startDate;
//		while (!date.isAfter(endDate)) {
//			if (date.getDayOfWeek() != DayOfWeek.SATURDAY && date.getDayOfWeek() != DayOfWeek.SUNDAY) {
//				businessDays++;
//			}
//			date = date.plusDays(1);
//		}
//		return businessDays;
//	}
//	

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

		// Get holidays for the year of the leave request
		int year = leaveRequest.getStartDate().getYear();
		List<Holiday> holidays = holidayService.getHolidaysForYear(year);

		// Calculate number of business days excluding weekends and holidays
		int numberOfBusinessDays = calculateNumberOfBusinessDays(leaveRequest.getStartDate(), leaveRequest.getEndDate(),
				holidays);
		leaveRequest.setNumberOfDays(numberOfBusinessDays);

		// Validate leave balance
		balanceService.validateLeaveBalanceForRequest(leaveRequest, balances, numberOfBusinessDays);

		// If validation passes, update leave balance
		balanceService.updateLeaveBalanceForApprovedRequest(leaveRequest, balances, numberOfBusinessDays);

		LeaveRequest leRequest = leaveRequestRepository.save(leaveRequest);

		String emailBody = "Dear " + leaveRequest.getEmployeeId().getEmployeeName() + ",\n\n"
				+ "Your leave request has been submitted successfully for the following period:\n" + "Start Date: "
				+ leaveRequest.getStartDate() + "\n" + "End Date: " + leaveRequest.getEndDate() + "\n" + "Reason: "
				+ leaveRequest.getReason() + "\n\n" + "Thank you for your submission.\n\n" + "Best regards,\n"
				+ "Leave Management Team \n" + "CoreNuts Technologies";

		emailSenderService.sendSimpleEmail(leaveRequest.getEmployeeId().getEmail(), "Leave Request - Notification",
				emailBody);
		return leRequest;
	}

	public int calculateNumberOfBusinessDays(LocalDate startDate, LocalDate endDate, List<Holiday> holidays) {

		int businessDays = 0;
		LocalDate date = startDate;
		while (!date.isAfter(endDate)) {
			if (date.getDayOfWeek() != DayOfWeek.SATURDAY && date.getDayOfWeek() != DayOfWeek.SUNDAY
					&& !isHoliday(date, holidays)) {
				businessDays++;
			}
			date = date.plusDays(1);
		}
		return businessDays;
	}

	private boolean isHoliday(LocalDate date, List<Holiday> holidays) {
		for (Holiday holiday : holidays) {
			if (date.equals(holiday.getDate())) {
				return true;
			}
		}
		return false;
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

		int numberOfDays = calculateNumberOfBusinessDays(updatedLeaveRequest.getStartDate(),
				updatedLeaveRequest.getEndDate(),
				holidayService.getHolidaysForYear(updatedLeaveRequest.getStartDate().getYear()));

		balanceService.updateLeaveBalanceForApprovedRequest(existingLeaveRequest, balances, numberOfDays);

		LeaveRequest leRequest = leaveRequestRepository.save(existingLeaveRequest);

		if (updatedLeaveRequest.getStatus().equalsIgnoreCase("Approved")) {
			emailSenderService.sendSimpleEmail(existingLeaveRequest.getEmployeeId().getEmail(),
					"Leave Request Approved - Notification",
					"Dear " + existingLeaveRequest.getEmployeeId().getEmployeeName() + ",\n\n"
							+ "CoreNuts Technologies is pleased to inform you that your leave request has been approved for the following period:\n"
							+ "Start Date: " + existingLeaveRequest.getStartDate() + "\n" + "End Date: "
							+ existingLeaveRequest.getEndDate() + "\n" + "Number of days: "
							+ existingLeaveRequest.getNumberOfDays() + "\n\n"
							+ "Thank you for your patience and cooperation.\n\n" + "Best regards,\n"
							+ "Leave Management Team");

		} else if (updatedLeaveRequest.getStatus().equalsIgnoreCase("Rejected")) {
			emailSenderService.sendSimpleEmail(existingLeaveRequest.getEmployeeId().getEmail(),
					"Leave Request Rejected - Notification",
					"Dear " + existingLeaveRequest.getEmployeeId().getEmployeeName() + ",\n\n"
							+ "CoreNuts Technologies regrets to inform you that your leave request has been rejected for the following period:\n"
							+ "Start Date: " + existingLeaveRequest.getStartDate() + "\n" + "End Date: "
							+ existingLeaveRequest.getEndDate() + "\n" + "Number of days: "
							+ existingLeaveRequest.getNumberOfDays() + "\n" + "Reason for rejection: "
							+ existingLeaveRequest.getDescription() + "\n\n" + "Thank you for your understanding.\n\n"
							+ "Best regards,\n" + "Leave Management Team");

		}
		return leRequest;
	}

	@Override
	public List<LeaveRequest> getLeaveRequestsByEmployeeId(int employeeId) {
		return leaveRequestRepository.findByEmployeeId(employeeId);
	}

}
