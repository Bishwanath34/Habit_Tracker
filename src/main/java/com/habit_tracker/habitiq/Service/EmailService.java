package com.habit_tracker.habitiq.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    private final Random random = new Random();

    // Generate 6-digit OTP
    public String generateOtp() {
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    // Send OTP email
    public void sendOtpEmail(String toEmail, String otp) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(toEmail);
        helper.setSubject("HabitIQ – Email Verification OTP");
        helper.setText(
                "<h3>Verify your HabitIQ account</h3>"
                        + "<p>Your OTP code is: <b>" + otp + "</b></p>"
                        + "<p>This code will expire in 5 minutes.</p>",
                true
        );

        mailSender.send(message);
    }
}