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
                    .email("owner2@qlnt.com") // Changed email to be unique just in case
                    .phone("0987654321")
                    .role(Role.OWNER)
                    .enabled(true)
                    .build();
            userRepository.save(owner);
            System.out.println(">>> Đã khởi tạo tài khoản Owner mặc định: owner / owner123");
        }
    }
}
