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

import com.commonmodels.entity.Holiday;
import com.leave.service.HolidayService;

@RestController
@RequestMapping("/holidays")
@CrossOrigin(origins = "http://localhost:4200")
public class HolidayController {

	private final HolidayService holidayService;

	@Autowired
	public HolidayController(HolidayService holidayService) {
		this.holidayService = holidayService;
	}

	@GetMapping("/{year}")
	public List<Holiday> getHolidaysForYear(@PathVariable int year) {
		return holidayService.getHolidaysForYear(year);
	}

	@GetMapping
	public ResponseEntity<List<Holiday>> getAllHolidays() {
		List<Holiday> holidays = holidayService.getAllHolidays();
		return ResponseEntity.ok(holidays);

	}
	
	@PostMapping
	public ResponseEntity<Holiday> saveHoliday(@RequestBody Holiday holiday) {
		Holiday saveholiday = holidayService.saveHoliday(holiday);
		return ResponseEntity.ok(saveholiday);
	}
	

	@PutMapping("/{id}")
	public ResponseEntity<Holiday> updateHoliday(@PathVariable int id, @RequestBody Holiday updatedHoliday) {
		Holiday updatedHolidaycal = holidayService.updateHoliday(id, updatedHoliday);
		return ResponseEntity.ok(updatedHolidaycal);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteHoliday(@PathVariable int id) {
		holidayService.deleteHoliday(id);
		return ResponseEntity.noContent().build();
	}
	
	



}
