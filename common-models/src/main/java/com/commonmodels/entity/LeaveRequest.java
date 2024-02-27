package com.commonmodels.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "LeaveRequest")


@NamedQuery(name = "LeaveRequest.findByEmployeeId", query = "SELECT lr FROM LeaveRequest lr WHERE lr.employee.employeeId = :employeeId")
public class LeaveRequest {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "request_id")
	private int requestId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "employee_id")
//	@Column(name="employee_id")
	private Employee employee;

	@Column(name = "start_date")
	private LocalDate startDate;

	@Column(name = "end_date")
	@JsonSerialize(using = InstantSerializer.class)
	private LocalDate endDate;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "leave_type_id")
//	@Column(name = "leave_type_id")
	private LeaveType leaveTypeId;

	@Column(name = "reason")
	private String reason;

//	@ManyToOne(fetch = FetchType.EAGER)
//	@JoinColumn(name = "manager_id")
	@Column(name = "manager_id")
	private Long managerId;

	@Column(name = "status")
	private String status;
	
	@Column(name="description")
	private String description;
	
	private long numberOfDays;

}
