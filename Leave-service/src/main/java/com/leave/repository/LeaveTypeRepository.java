package com.leave.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.commonmodels.entity.LeaveType;


@Repository
public interface LeaveTypeRepository extends JpaRepository<LeaveType, Integer> {

	

}
