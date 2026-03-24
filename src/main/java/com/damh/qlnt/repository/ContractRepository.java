package com.damh.qlnt.repository;

import com.damh.qlnt.entity.Contract;
import com.damh.qlnt.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {
    List<Contract> findByTenant(User tenant);
    List<Contract> findByOwner(User owner);
}
