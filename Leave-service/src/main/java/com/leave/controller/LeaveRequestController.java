package com.leave.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.commonmodels.entity.LeaveRequest;
import com.leave.service.LeaveRequestService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/leaverequest")
@CrossOrigin(origins = "http://localhost:4200")
public class LeaveRequestController {
	@Autowired
	private LeaveRequestService leaveRequestService;

	@PostMapping("/save")
	public ResponseEntity<LeaveRequest> saveLeaveRequest(@RequestBody LeaveRequest leaveRequest) {

		LeaveRequest savedLeaveRequest = leaveRequestService.saveLeaveRequest(leaveRequest);

		return ResponseEntity.ok(savedLeaveRequest);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteLeaveRequest(@PathVariable int id) {
		leaveRequestService.deleteLeaveRequest(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping
	public ResponseEntity<List<LeaveRequest>> getAllLeaveRequests() {
		List<LeaveRequest> leaveRequests = leaveRequestService.getAllLeaveRequest();
		return ResponseEntity.ok(leaveRequests);
	}

	@GetMapping("/{id}")
	public ResponseEntity<LeaveRequest> getLeaveRequestById(@PathVariable int id) {
		LeaveRequest leaveRequest = leaveRequestService.getLeaveRequestById(id);
		return ResponseEntity.ok(leaveRequest);
	}

	@PutMapping("/{id}")
	public ResponseEntity<LeaveRequest> updateLeaveRequest(@PathVariable int id,
			@RequestBody LeaveRequest updatedLeaveRequest) {
		LeaveRequest updatedRequest = leaveRequestService.updateLeaveRequest(id, updatedLeaveRequest);
//		System.err.println(updatedRequest);
		System.err.println(id + "" + updatedLeaveRequest);
		return ResponseEntity.ok(updatedRequest);
	}

	@GetMapping("/employee/{employeeId}")
	public ResponseEntity<List<LeaveRequest>> getLeaveRequestsByEmployeeId(@PathVariable Integer employeeId) {
		List<LeaveRequest> leaveRequests = leaveRequestService.getLeaveRequestsByEmployeeId(employeeId);
		return ResponseEntity.ok(leaveRequests);
	}

}
