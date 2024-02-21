package com.leave.DTO;

import lombok.Data;

@Data
public class LeaveBalanceDTO {

	int leave_type_id;
	double leave_balance;
	String leave_type_name;
	String leaveTypeWithBalance;

	public LeaveBalanceDTO(int leave_type_id, double leave_balance, String leave_type_name,
			String leaveTypeWithBalance) {
		this.leave_type_id = leave_type_id;
		this.leave_balance = leave_balance;
		this.leave_type_name = leave_type_name;
		this.leaveTypeWithBalance = leaveTypeWithBalance;
	}
}