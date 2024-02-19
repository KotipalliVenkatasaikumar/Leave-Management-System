package com.leave.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.commonmodels.entity.Holiday;

@Repository
public interface HolidayRepository extends JpaRepository<Holiday, Integer> {
	
	List<Holiday> findByDateBetween(LocalDate startDate, LocalDate endDate);

}
