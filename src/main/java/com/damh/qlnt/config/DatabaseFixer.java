package com.damh.qlnt.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DatabaseFixer {

    private final JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void fixDatabase() {
        System.out.println(">>> STARTING DATABASE FIXER...");
        try {
            // Fix approval_status in rooms table
            jdbcTemplate.execute("ALTER TABLE rooms MODIFY COLUMN approval_status VARCHAR(50)");
            System.out.println(">>> SUCCESS: rooms.approval_status altered to VARCHAR(50)");
            
            // Fix status in posts table
            jdbcTemplate.execute("ALTER TABLE posts MODIFY COLUMN status VARCHAR(50)");
            System.out.println(">>> SUCCESS: posts.status altered to VARCHAR(50)");
            
        } catch (Exception e) {
            System.err.println(">>> ERROR during database fix: " + e.getMessage());
        }
    }
}
