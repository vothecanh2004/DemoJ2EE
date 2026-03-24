package com.damh.qlnt.controller.admin;

import com.damh.qlnt.service.ContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/contracts")
@RequiredArgsConstructor
public class AdminContractController {

    private final ContractService contractService;

    @GetMapping
    public String listContracts(Model model) {
        model.addAttribute("contracts", contractService.getAllContracts());
        return "admin/contracts/list";
    }

    @PostMapping("/resolve/{id}")
    public String resolveDispute(@PathVariable Long id, @RequestParam boolean terminate) {
        contractService.resolveDispute(id, terminate);
        return "redirect:/admin/contracts";
    }
    @GetMapping("/export/{id}")
    public String exportContract(@PathVariable Long id, Model model) {
        model.addAttribute("contract", contractService.getContractById(id)
                .orElseThrow(() -> new IllegalArgumentException("Contract not found")));
        return "admin/contracts/export";
    }
}
