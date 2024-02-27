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

import lombok.extern.slf4j.Slf4j;

@Slf4j
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

	@Override
	public LeaveRequest saveLeaveRequest(LeaveRequest leaveRequest) {
		leaveRequest.setStatus("Pending");
		log.info("leave request {}", leaveRequest);
		// Retrieve existing leave balances for the employee
		List<LeaveBalance> balances = balanceService
				.getLeaveBalanceByEmployeeId(leaveRequest.getEmployee().getEmployeeId());

		if (balances.isEmpty()) {
			List<LeaveType> leaveTypes = leaveTypeService.getAllLeaveTypes();

			for (LeaveType leaveType : leaveTypes) {
				LeaveBalance leaveBalance = new LeaveBalance();
				leaveBalance.setEmployee(leaveRequest.getEmployee());
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

		return leaveRequestRepository.save(leaveRequest);
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
		existingLeaveRequest.setEmployee(updatedLeaveRequest.getEmployee());
		existingLeaveRequest.setStatus(updatedLeaveRequest.getStatus());
		existingLeaveRequest.setDescription(updatedLeaveRequest.getDescription());

		List<LeaveBalance> balances = balanceService
				.getLeaveBalanceByEmployeeId(updatedLeaveRequest.getEmployee().getEmployeeId());

		int numberOfDays = calculateNumberOfBusinessDays(updatedLeaveRequest.getStartDate(),
				updatedLeaveRequest.getEndDate(),
				holidayService.getHolidaysForYear(updatedLeaveRequest.getStartDate().getYear()));

		balanceService.updateLeaveBalanceForApprovedRequest(existingLeaveRequest, balances, numberOfDays);

		
		return leaveRequestRepository.save(existingLeaveRequest);

	}

	@Override
	public List<LeaveRequest> getLeaveRequestsByEmployeeId(int employeeId) {
		return leaveRequestRepository.findByEmployeeId(employeeId);
	}

}
