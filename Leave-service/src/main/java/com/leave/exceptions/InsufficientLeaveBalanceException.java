package com.leave.exceptions;

public class InsufficientLeaveBalanceException extends RuntimeException {
	public InsufficientLeaveBalanceException(String msg) {
		super(msg);

	}

}
