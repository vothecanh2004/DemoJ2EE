package com.damh.qlnt.service.impl;

import com.damh.qlnt.entity.Contract;
import com.damh.qlnt.entity.ContractStatus;
import com.damh.qlnt.entity.User;
import com.damh.qlnt.repository.ContractRepository;
import com.damh.qlnt.service.ContractService;
import com.damh.qlnt.entity.RoomStatus;
import com.damh.qlnt.entity.Room;
import com.damh.qlnt.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ContractServiceImpl implements ContractService {

    private final ContractRepository contractRepository;
    private final RoomRepository roomRepository;

    @Override
    public Contract createContract(Contract contract) {
        contract.setStatus(ContractStatus.ACTIVE);
        Contract saved = contractRepository.save(contract);
        
        // Update Room Status
        Room room = saved.getRoom();
        room.setStatus(RoomStatus.RENTED);
        roomRepository.save(room);
        
        return saved;
    }

    @Override
    public Optional<Contract> getContractById(Long id) {
        return contractRepository.findById(id);
    }

    @Override
    public List<Contract> getContractsByTenant(User tenant) {
        return contractRepository.findByTenant(tenant);
    }

    @Override
    public List<Contract> getContractsByOwner(User owner) {
        return contractRepository.findByOwner(owner);
    }

    @Override
    public List<Contract> getAllContracts() {
        return contractRepository.findAll();
    }

    @Override
    public void renewContract(Long id, java.time.LocalDate newEndDate) {
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Contract not found"));
        contract.setEndDate(newEndDate);
        contract.setStatus(ContractStatus.ACTIVE);
        contractRepository.save(contract);
    }

    @Override
    public void terminateContract(Long id, String reason) {
        System.out.println(">>> Terminating Contract ID: " + id);
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Contract not found"));
        contract.setStatus(ContractStatus.TERMINATED);
        contract.setDisputeNote(reason);
        contractRepository.save(contract);
        
        Room room = contract.getRoom();
        System.out.println(">>> Setting Room ID: " + room.getId() + " to AVAILABLE");
        room.setStatus(RoomStatus.AVAILABLE);
        roomRepository.save(room);
    }

    @Override
    public void markAsDisputed(Long id, String reason) {
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Contract not found"));
        contract.setStatus(ContractStatus.DISPUTED);
        contract.setDisputeNote(reason);
        contractRepository.save(contract);
    }

    @Override
    public void resolveDispute(Long id, boolean terminate) {
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Contract not found"));
        
        if (terminate) {
            contract.setStatus(ContractStatus.TERMINATED);
            contract.setDisputeNote(contract.getDisputeNote() + " | [Admin: Đã hủy hợp đồng]");
            
            Room room = contract.getRoom();
            room.setStatus(RoomStatus.AVAILABLE);
            roomRepository.save(room);
        } else {
            contract.setStatus(ContractStatus.ACTIVE);
            contract.setDisputeNote(contract.getDisputeNote() + " | [Admin: Bác bỏ tranh chấp]");
        }
        
        contractRepository.save(contract);
    }
}
