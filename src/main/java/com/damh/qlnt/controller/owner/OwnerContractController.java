package com.damh.qlnt.controller.owner;

import com.damh.qlnt.entity.User;
import com.damh.qlnt.repository.UserRepository;
import com.damh.qlnt.service.ContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/owner/contracts")
@RequiredArgsConstructor
public class OwnerContractController {

    private final ContractService contractService;
    private final UserRepository userRepository;

    @GetMapping
    public String listContracts(Model model, Principal principal) {
        User owner = userRepository.findByUsername(principal.getName()).orElseThrow();
        model.addAttribute("contracts", contractService.getContractsByOwner(owner));
        return "owner/contracts/list";
    }

    @PostMapping("/terminate/{id}")
    public String terminate(@PathVariable Long id, @RequestParam String reason) {
        contractService.terminateContract(id, reason);
        return "redirect:/owner/contracts";
    }
}
