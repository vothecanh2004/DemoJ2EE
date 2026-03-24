package com.damh.qlnt.config;

import com.damh.qlnt.entity.Role;
import com.damh.qlnt.entity.User;
import com.damh.qlnt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final com.damh.qlnt.repository.RoomRepository roomRepository;
    private final com.damh.qlnt.repository.AppointmentRepository appointmentRepository;
    private final com.damh.qlnt.repository.ContractRepository contractRepository;
    private final com.damh.qlnt.repository.PostRepository postRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Create Admin account if it doesn't exist
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .fullName("Quản Trị Viên")
                    .email("admin@qlnt.com")
                    .phone("0123456789")
                    .role(Role.ADMIN)
                    .enabled(true)
                    .build();
            userRepository.save(admin);
            System.out.println(">>> Đã khởi tạo tài khoản Admin mặc định: admin / admin123");
        }
        
        // Create a Demo Owner account if it doesn't exist
        if (userRepository.findByUsername("owner").isEmpty()) {
            User owner = User.builder()
                    .username("owner")
                    .password(passwordEncoder.encode("owner123"))
                    .fullName("Chủ Trọ Demo")
                    .email("owner@qlnt.com")
                    .phone("0987654321")
                    .role(Role.OWNER)
                    .reputationScore(95)
                    .enabled(true)
                    .build();
            userRepository.save(owner);
            System.out.println(">>> Đã khởi tạo tài khoản Owner mặc định: owner / owner123");
            
            // Create a Demo Tenant
            User tenant = User.builder()
                    .username("user")
                    .password(passwordEncoder.encode("user123"))
                    .fullName("Người Thuê Demo")
                    .email("user@qlnt.com")
                    .phone("0123999888")
                    .role(Role.USER)
                    .enabled(true)
                    .build();
            userRepository.save(tenant);

            // Create Sample Rooms
            com.damh.qlnt.entity.Room room1 = com.damh.qlnt.entity.Room.builder()
                    .title("Phòng Trọ Cao Cấp Quận 1")
                    .description("Phòng đẹp, đầy đủ tiện nghi, gần trung tâm.")
                    .price(new java.math.BigDecimal("5000000"))
                    .area(25.0)
                    .address("123 Lê Lợi, Quận 1, TP.HCM")
                    .status(com.damh.qlnt.entity.RoomStatus.AVAILABLE)
                    .approvalStatus(com.damh.qlnt.entity.ApprovalStatus.APPROVED)
                    .owner(owner)
                    .build();
            roomRepository.save(room1);

            com.damh.qlnt.entity.Room room2 = com.damh.qlnt.entity.Room.builder()
                    .title("Phòng Trọ Giá Rẻ Bình Thạnh")
                    .description("Phòng mới xây, thoáng mát, khu an ninh.")
                    .price(new java.math.BigDecimal("3000000"))
                    .area(20.0)
                    .address("456 Phan Văn Trị, Bình Thạnh")
                    .status(com.damh.qlnt.entity.RoomStatus.AVAILABLE)
                    .approvalStatus(com.damh.qlnt.entity.ApprovalStatus.PENDING)
                    .owner(owner)
                    .build();
            roomRepository.save(room2);

            // Create Sample Appointment
            com.damh.qlnt.entity.Appointment apt = com.damh.qlnt.entity.Appointment.builder()
                    .tenant(tenant)
                    .owner(owner)
                    .room(room1)
                    .appointmentDate(java.time.LocalDateTime.now().plusDays(2))
                    .status(com.damh.qlnt.entity.AppointmentStatus.PENDING)
                    .build();
            appointmentRepository.save(apt);

            // Create Sample Contract
            com.damh.qlnt.entity.Contract contract = com.damh.qlnt.entity.Contract.builder()
                    .tenant(tenant)
                    .owner(owner)
                    .room(room1)
                    .startDate(java.time.LocalDate.now())
                    .endDate(java.time.LocalDate.now().plusMonths(12))
                    .status(com.damh.qlnt.entity.ContractStatus.ACTIVE)
                    .build();
            contractRepository.save(contract);
            
            // Create Sample Review Post (Pending)
            com.damh.qlnt.entity.Post review = com.damh.qlnt.entity.Post.builder()
                    .author(tenant)
                    .content("Chủ trọ rất nhiệt tình, phòng sạch sẽ như mô tả!")
                    .type(com.damh.qlnt.entity.PostType.REVIEW)
                    .status(com.damh.qlnt.entity.PostStatus.PENDING)
                    .createdAt(java.time.LocalDateTime.now())
                    .build();
            postRepository.save(review);
        }
    }
}
