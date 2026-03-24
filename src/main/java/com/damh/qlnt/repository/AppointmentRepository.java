package com.damh.qlnt.repository;

import com.damh.qlnt.entity.Appointment;
import com.damh.qlnt.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByTenant(User tenant);
    List<Appointment> findByOwner(User owner);
}
