package com.habit_tracker.habitiq.Service;

import com.sendgrid.SendGrid;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.Method;
import com.sendgrid.Mail;
import com.sendgrid.Email;
import com.sendgrid.Content;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailService {

    @Value("${SENDGRID_API_KEY}")
    private String sendGridApiKey;

    @Value("${EMAIL_FROM}")
    private String mailFrom;

    // ================= SEND OTP EMAIL =================
    public void sendOtpEmail(String toEmail, String otp) throws IOException {
        Email from = new Email(mailFrom);
        Email to = new Email(toEmail);
        String subject = "HabitIQ – Email Verification OTP";
        Content content = new Content("text/html",
                "<div style=\"font-family:Arial,sans-serif\">" +
                        "<h2>Welcome to HabitIQ</h2>" +
                        "<p>Please verify your email using the OTP below:</p>" +
                        "<h1 style=\"color:#4CAF50;\">" + otp + "</h1>" +
                        "<p>This OTP will expire in 5 minutes.</p>" +
                        "<p>Thank you for using HabitIQ 🚀</p>" +
                        "</div>"
        );

        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());
        Response response = sg.api(request);

        System.out.println("SendGrid OTP status: " + response.getStatusCode());
    }

    // ================= SEND REMINDER EMAIL =================
    public void sendReminderEmail(String toEmail, String messageText) throws IOException {
        Email from = new Email(mailFrom);
        Email to = new Email(toEmail);
        String subject = "HabitIQ Reminder ⏰";
        Content content = new Content("text/html",
                "<div style=\"font-family:Arial,sans-serif\">" +
                        "<h2>⏰ Habit Reminder</h2>" +
                        "<p>" + messageText + "</p>" +
                        "<p>Open HabitIQ and track your progress.</p>" +
                        "</div>"
        );

        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());
        Response response = sg.api(request);

        System.out.println("SendGrid Reminder status: " + response.getStatusCode());
    }
}
