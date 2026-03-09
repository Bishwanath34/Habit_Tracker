package com.habit_tracker.habitiq.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // ================= SEND OTP EMAIL =================
    public void sendOtpEmail(String toEmail, String otp) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(toEmail);
        helper.setSubject("HabitIQ – Email Verification OTP");

        String content = """
                <div style="font-family:Arial,sans-serif">
                    <h2>Welcome to HabitIQ</h2>
                    <p>Please verify your email using the OTP below:</p>
                """
                + "<h1 style=\"color:#4CAF50;\">" + otp + "</h1>"
                + """
                    <p>This OTP will expire in 5 minutes.</p>
                    <br>
                    <p>Thank you for using HabitIQ 🚀</p>
                </div>
                """;

        helper.setText(content, true);

        mailSender.send(message);
    }

    // ================= SEND REMINDER EMAIL =================
    public void sendReminderEmail(String toEmail) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(toEmail);
        helper.setSubject("HabitIQ Reminder ⏰");

        String content = """
                <div style="font-family:Arial,sans-serif">
                    <h2>⏰ Habit Reminder</h2>
                    <p>Don't forget to complete your habits today!</p>
                    <p>Consistency builds success 💪</p>
                    <br>
                    <p>Open HabitIQ and track your progress.</p>
                </div>
                """;

        helper.setText(content, true);

        mailSender.send(message);
    }
}
