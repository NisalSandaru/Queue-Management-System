package com.nisal.Queue.Management.System.config;

import com.nisal.Queue.Management.System.entity.UserEntity;
import com.nisal.Queue.Management.System.enums.UserRole;
import com.nisal.Queue.Management.System.enums.UserStatus;
import com.nisal.Queue.Management.System.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        // Check if admin already exists
        if (!userRepository.existsByRole(UserRole.ROLE_ADMIN)) {

            UserEntity e = userRepository.findByEmail("admin@system.com");

            if (e != null) {
                if (e.getRole() != UserRole.ROLE_ADMIN) {
                    e.setRole(UserRole.ROLE_ADMIN);
                    e.setStatus(UserStatus.ACTIVE);
                    userRepository.save(e);
                }
            }else {

                UserEntity admin = new UserEntity();
                admin.setFirstName("admin");
                admin.setLastName("admin");
                admin.setPhone(null);
                admin.setStatus(UserStatus.ACTIVE);
                admin.setEmail("admin@system.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRole(UserRole.ROLE_ADMIN);

                userRepository.save(admin);

                System.out.println("Default Admin Created: admin@system.com / admin123");
            }
        } else {
            System.out.println("Admin already exists. Skipping default admin creation.");
        }
    }
}
