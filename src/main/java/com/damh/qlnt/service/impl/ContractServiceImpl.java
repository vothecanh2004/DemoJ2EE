package com.damh.qlnt.service.impl;

import com.damh.qlnt.entity.Contract;
import com.damh.qlnt.entity.ContractStatus;
import com.damh.qlnt.entity.User;
import com.damh.qlnt.repository.ContractRepository;
import com.damh.qlnt.service.ContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContractServiceImpl implements ContractService {

    private final ContractRepository contractRepository;

    @Override
    public Contract createContract(Contract contract) {
        contract.setStatus(ContractStatus.ACTIVE);
        return contractRepository.save(contract);
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
    public void terminateContract(Long id, String reason) {
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Contract not found"));
        contract.setStatus(ContractStatus.TERMINATED);
        contract.setDisputeNote(reason);
        contractRepository.save(contract);
    }

    @Override
    public void markAsDisputed(Long id, String reason) {
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Contract not found"));
        contract.setStatus(ContractStatus.DISPUTED);
        contract.setDisputeNote(reason);
        contractRepository.save(contract);
    }
}
