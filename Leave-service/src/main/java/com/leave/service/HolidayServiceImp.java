package com.leave.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.commonmodels.entity.Holiday;
import com.leave.repository.HolidayRepository;

@Service
public class HolidayServiceImp implements HolidayService {

	private final HolidayRepository holidayRepository;

	@Autowired
	public HolidayServiceImp(HolidayRepository holidayRepository) {
		this.holidayRepository = holidayRepository;
	}

	@Override
	public List<Holiday> getHolidaysForYear(int year) {
		LocalDate startDate = LocalDate.of(year, 1, 1);
		LocalDate endDate = LocalDate.of(year, 12, 31);
		return holidayRepository.findByDateBetween(startDate, endDate);
	}

	@Override
	public List<Holiday> getAllHolidays() {
		// TODO Auto-generated method stub
		return holidayRepository.findAll();
	}

	@Override
	public Holiday saveHoliday(Holiday holiday) {
		// TODO Auto-generated method stub
		return holidayRepository.save(holiday);
	}

	@Override
	public Holiday updateHoliday(int id, Holiday updatedHoliday) {
		
		Optional<Holiday> optionalExistingHoliday = holidayRepository.findById(id);

		if (optionalExistingHoliday.isPresent()) {
			Holiday existingHoliday = optionalExistingHoliday.get();

			if (updatedHoliday.getName() != null) {
				existingHoliday.setDate(updatedHoliday.getDate());
				existingHoliday.setName(updatedHoliday.getName());
			}

			return holidayRepository.save(existingHoliday);
		} else {

			return null;
		}
	}

	@Override
	public void deleteHoliday(int id) {
		holidayRepository.deleteById(id);
		
	}

}
