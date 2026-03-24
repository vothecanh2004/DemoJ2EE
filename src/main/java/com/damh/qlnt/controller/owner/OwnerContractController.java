package com.damh.qlnt.controller.owner;

import com.damh.qlnt.entity.Contract;
import com.damh.qlnt.entity.Room;
import com.damh.qlnt.entity.User;
import com.damh.qlnt.repository.RoomRepository;
import com.damh.qlnt.repository.UserRepository;
import com.damh.qlnt.service.ContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/owner/contracts")
@RequiredArgsConstructor
public class OwnerContractController {

    private final ContractService contractService;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    @GetMapping
    public String listContracts(Model model, Principal principal) {
        User owner = userRepository.findByUsername(principal.getName()).orElseThrow();
        model.addAttribute("contracts", contractService.getContractsByOwner(owner));
        return "owner/contracts/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model, Principal principal) {
        User owner = userRepository.findByUsername(principal.getName()).orElseThrow();
        List<Room> rooms = roomRepository.findByOwner(owner);
        model.addAttribute("rooms", rooms);
        return "owner/contracts/form";
    }

    @PostMapping("/create")
    public String createContract(@RequestParam String tenantUsername,
                                 @RequestParam Long roomId,
                                 @RequestParam String startDate,
                                 @RequestParam String endDate,
                                 @RequestParam(required = false) Double startElectricity,
                                 @RequestParam(required = false) Double startWater,
                                 Principal principal,
                                 RedirectAttributes redirectAttributes) {
        try {
            User owner = userRepository.findByUsername(principal.getName()).orElseThrow();
            Optional<User> tenantOpt = userRepository.findByUsername(tenantUsername);
            if (tenantOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy người thuê với tài khoản này.");
                return "redirect:/owner/contracts/create";
            }
            User tenant = tenantOpt.get();
            Room room = roomRepository.findById(roomId).orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phòng"));
            if (room.getStatus() != com.damh.qlnt.entity.RoomStatus.AVAILABLE) {
                redirectAttributes.addFlashAttribute("error", "Phòng này đã được thuê hoặc không còn trống.");
                return "redirect:/owner/contracts/create";
            }

            Contract contract = Contract.builder()
                    .owner(owner)
                    .tenant(tenant)
                    .room(room)
                    .startDate(LocalDate.parse(startDate))
                    .endDate(LocalDate.parse(endDate))
                    .startElectricity(startElectricity)
                    .startWater(startWater)
                    .build();

            contractService.createContract(contract);
            redirectAttributes.addFlashAttribute("success", "Tạo hợp đồng thành công!");
            return "redirect:/owner/contracts";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
            return "redirect:/owner/contracts/create";
        }
    }

    @PostMapping("/renew/{id}")
    public String renewContract(@PathVariable Long id, @RequestParam String newEndDate, RedirectAttributes redirectAttributes) {
        try {
            contractService.renewContract(id, LocalDate.parse(newEndDate));
            redirectAttributes.addFlashAttribute("success", "Gia hạn hợp đồng thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/owner/contracts";
    }

    @PostMapping("/terminate/{id}")
    public String terminate(@PathVariable Long id, @RequestParam String reason, RedirectAttributes redirectAttributes) {
        try {
            contractService.terminateContract(id, reason);
            redirectAttributes.addFlashAttribute("success", "Đã kết thúc hợp đồng!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/owner/contracts";
    }
}
