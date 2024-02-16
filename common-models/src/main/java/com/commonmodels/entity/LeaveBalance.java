package com.commonmodels.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "LeaveBalance")
public class LeaveBalance {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "balance_id")
	private int balanceId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "employee_id")
	private Employee employee;

//	@ManyToOne(fetch = FetchType.EAGER)
//	@JoinColumn(name = "leave_type_id")
//	private LeaveType leaveType;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "leave_type_id") // Assuming this is the column name in LeaveBalance table
	private LeaveType leaveType; // This field represents the relationship with LeaveType

	@Column(nullable = false)
	private double leaveBalance;

	@Column(nullable = false)
	private int year;

}
