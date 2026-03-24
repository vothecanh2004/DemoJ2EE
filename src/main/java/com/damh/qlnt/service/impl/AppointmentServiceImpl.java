package com.damh.qlnt.service.impl;

import com.damh.qlnt.entity.Appointment;
import com.damh.qlnt.entity.AppointmentStatus;
import com.damh.qlnt.entity.User;
import com.damh.qlnt.repository.AppointmentRepository;
import com.damh.qlnt.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;

    @Override
    public Appointment bookAppointment(Appointment appointment) {
        appointment.setStatus(AppointmentStatus.PENDING);
        return appointmentRepository.save(appointment);
    }

    @Override
    public Optional<Appointment> getAppointmentById(Long id) {
        return appointmentRepository.findById(id);
    }

    @Override
    public List<Appointment> getAppointmentsByTenant(User tenant) {
        return appointmentRepository.findByTenant(tenant);
    }

    @Override
    public List<Appointment> getAppointmentsByOwner(User owner) {
        return appointmentRepository.findByOwner(owner);
    }
    
    @Override
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    @Override
    public void approveAppointment(Long id) {
        Appointment apt = appointmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));
        apt.setStatus(AppointmentStatus.APPROVED);
        appointmentRepository.save(apt);
    }

    @Override
    public void rejectAppointment(Long id, String reason) {
        Appointment apt = appointmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));
        apt.setStatus(AppointmentStatus.REJECTED);
        apt.setDisputeNote(reason);
        appointmentRepository.save(apt);
    }

    @Override
    public void completeAppointment(Long id) {
        Appointment apt = appointmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));
        apt.setStatus(AppointmentStatus.COMPLETED);
        appointmentRepository.save(apt);
    }
}
