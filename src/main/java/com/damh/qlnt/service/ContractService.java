package com.damh.qlnt.service;

import com.damh.qlnt.entity.Contract;
import com.damh.qlnt.entity.User;
import java.util.List;
import java.util.Optional;

public interface ContractService {
    Contract createContract(Contract contract);
    Optional<Contract> getContractById(Long id);
    List<Contract> getContractsByTenant(User tenant);
    List<Contract> getContractsByOwner(User owner);
    List<Contract> getAllContracts();
    
    // Lifecycle
    void renewContract(Long id, java.time.LocalDate newEndDate);
    void terminateContract(Long id, String reason);
    void markAsDisputed(Long id, String reason);
    void resolveDispute(Long id, boolean terminate);
}
