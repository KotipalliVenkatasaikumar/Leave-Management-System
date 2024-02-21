package com.leave.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.commonmodels.entity.LeaveBalance;
import com.commonmodels.entity.LeaveRequest;
import com.leave.DTO.LeaveBalanceDTO;
import com.leave.service.LeaveBalanceService;

@RestController
@RequestMapping("/leaveBalance")
@CrossOrigin(origins = "http://localhost:4200")
public class LeaveBalanceController {
	@Autowired
	private LeaveBalanceService leaveBalanceService;

	@PostMapping
	public ResponseEntity<LeaveBalance> saveLeaveBalance(@RequestBody LeaveBalance leaveBalance) {
		LeaveBalance saveLeaveBalance = leaveBalanceService.saveLeaveBalance(leaveBalance);
		return ResponseEntity.ok(saveLeaveBalance);
	}

	@GetMapping("/{id}")
	public ResponseEntity<LeaveBalance> getLeaveBalanceById(@PathVariable int id) {
		LeaveBalance leaveBalance = leaveBalanceService.getLeaveBalanceById(id);
		return ResponseEntity.ok(leaveBalance);
	}
	
	@GetMapping
	public ResponseEntity<List<LeaveBalance>> getAllLeaveBalance() {
		List<LeaveBalance> leaveBalance = leaveBalanceService.getAllLeaveBalance();
		return ResponseEntity.ok(leaveBalance);
	}
	
	@GetMapping("/employee/{employeeId}")
	public ResponseEntity<List<LeaveBalance>> getLeaveBalanceByEmployeeId(@PathVariable Integer employeeId) {
		List<LeaveBalance> leaveBalance = leaveBalanceService.getLeaveBalanceByEmployeeId(employeeId);
		return ResponseEntity.ok(leaveBalance);
	}
	
	@GetMapping("/leavebalancebyemployeeid/{employeeId}")
	public ResponseEntity<List<LeaveBalanceDTO>> getLeaveBalanceDtoByEmployeeId(@PathVariable Integer employeeId) {
		List<LeaveBalanceDTO> leaveBalance = leaveBalanceService.getLeaveBalanceDtoByEmployeeId(employeeId);
		return ResponseEntity.ok(leaveBalance);
	}
	
}
