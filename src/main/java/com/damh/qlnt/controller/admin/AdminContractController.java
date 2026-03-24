package com.damh.qlnt.controller.admin;

import com.damh.qlnt.service.ContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
