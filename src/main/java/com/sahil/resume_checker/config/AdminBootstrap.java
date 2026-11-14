package com.sahil.resume_checker.config;

import com.sahil.resume_checker.entity.User;
import com.sahil.resume_checker.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminBootstrap implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminBootstrap(UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        String email = System.getenv("BOOTSTRAP_ADMIN_EMAIL");
        String password = System.getenv("BOOTSTRAP_ADMIN_PASSWORD");

        if (email == null || email.isBlank()
                || password == null || password.isBlank()) {
            System.out.println("Admin bootstrap skipped: credentials not configured.");
            return;
        }

        if (userRepository.existsByEmail(email)) {
            System.out.println("Admin account already exists.");
            return;
        }

        User admin = new User();

        admin.setName("System Admin");
        admin.setEmail(email);
        admin.setPassword(passwordEncoder.encode(password));
        admin.setRole("ADMIN");

        userRepository.save(admin);

        System.out.println("Initial admin account created.");
    }
}