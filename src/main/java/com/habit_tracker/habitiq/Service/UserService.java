package com.habit_tracker.habitiq.Service;

import com.habit_tracker.habitiq.Entity.User;
import com.habit_tracker.habitiq.Enum.User.AccountStatusEnum;
import com.habit_tracker.habitiq.Repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService; // ✅ inject EmailService

    private Map<Long, String> otpStore = new HashMap<>();

    private Random random = new Random();

    // ---------------- REGISTER ----------------
    public User register(User user) {
        userRepository.findByEmail(user.getEmail())
                .ifPresent(u -> { throw new RuntimeException("Email already exists"); });

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setAccountStatus(AccountStatusEnum.PENDING);
        user.setCreatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        // generate OTP
        String otp = generateOtp();
        otpStore.put(savedUser.getUserId(), otp);

        // send OTP email
        try {
            emailService.sendOtpEmail(savedUser.getEmail(), otp);
            System.out.println("Generated OTP for user " + savedUser.getEmail() + ": " + otp);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send OTP email: " + e.getMessage());
        }

        return savedUser;
    }

    // ---------------- LOGIN ----------------
    public User login(String email, String password, HttpSession session) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email not registered"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        if (user.getAccountStatus() != AccountStatusEnum.ACTIVE) {
            throw new RuntimeException("Account not verified. Please verify OTP.");
        }

        user.setLastLogin(LocalDateTime.now());
        return userRepository.save(user);
    }

    // ---------------- OTP ----------------
    public boolean validateOtp(String enteredOtp, Long userId) {
        String correctOtp = otpStore.get(userId);
        if (correctOtp != null && correctOtp.equals(enteredOtp)) {
            otpStore.remove(userId);
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            user.setAccountStatus(AccountStatusEnum.ACTIVE);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public void resendOtp(Long userId) {
        String otp = generateOtp();
        otpStore.put(userId, otp);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        try {
            emailService.sendOtpEmail(user.getEmail(), otp);
            System.out.println("Resent OTP for user " + user.getEmail() + ": " + otp);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to resend OTP email: " + e.getMessage());
        }
    }

    private String generateOtp() {
        int otp = 100000 + random.nextInt(900000); // 6-digit OTP
        return String.valueOf(otp);
    }
}