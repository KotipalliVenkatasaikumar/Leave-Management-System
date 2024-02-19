package com.leave.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.commonmodels.entity.Holiday;
import com.commonmodels.entity.LeaveType;

@Service
public interface HolidayService {

	public List<Holiday> getHolidaysForYear(int year);

	public List<Holiday> getAllHolidays();

	public Holiday saveHoliday(Holiday holiday);

	public Holiday updateHoliday(int id, Holiday updatedHoliday);

	public void deleteHoliday(int id);

}
