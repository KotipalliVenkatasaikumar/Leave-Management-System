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
import com.commonmodels.entity.LeaveType;
import com.leave.service.LeaveTypeService;

@RestController
@RequestMapping("/leave")
@CrossOrigin(origins = "http://localhost:4200")
public class LeaveTypeController {
	@Autowired
	private LeaveTypeService leaveTypeService;

	@PostMapping
	public ResponseEntity<LeaveType> saveLeaveType(@RequestBody LeaveType leaveType) {
		LeaveType saveLeaveType = leaveTypeService.saveLeaveType(leaveType);
		return ResponseEntity.ok(saveLeaveType);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteLeaveType(@PathVariable int id) {
		leaveTypeService.deleteLeaveType(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/{id}")
	public ResponseEntity<LeaveType> getLeaveTypeById(@PathVariable int id) {
		LeaveType leaveType = leaveTypeService.getLeaveTypeById(id);
		return ResponseEntity.ok(leaveType);
	}

	@GetMapping
	public ResponseEntity<List<LeaveType>> getAllLeaveType() {
		List<LeaveType> leaveTypes = leaveTypeService.getAllLeaveTypes();
		return ResponseEntity.ok(leaveTypes);
	}

	@PutMapping("/{id}")
	public ResponseEntity<LeaveType> updateLeaveType(@PathVariable int id, @RequestBody LeaveType updatedLeavetype) {
		LeaveType updatedLeaveType = leaveTypeService.updateLeaveType(id, updatedLeavetype);
		return ResponseEntity.ok(updatedLeaveType);
	}

}
