package com.damh.qlnt.service;

import com.damh.qlnt.entity.Appointment;
import com.damh.qlnt.entity.User;
import java.util.List;
import java.util.Optional;

public interface AppointmentService {
    Appointment bookAppointment(Appointment appointment);
    Optional<Appointment> getAppointmentById(Long id);
    List<Appointment> getAppointmentsByTenant(User tenant);
    List<Appointment> getAppointmentsByOwner(User owner);
    List<Appointment> getAllAppointments();
    
    // Status transitions
    void approveAppointment(Long id);
    void rejectAppointment(Long id, String reason);
    void completeAppointment(Long id);
    void cancelByAdmin(Long id);
}
