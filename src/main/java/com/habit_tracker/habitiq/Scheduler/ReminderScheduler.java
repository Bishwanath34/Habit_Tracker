package com.habit_tracker.habitiq.Scheduler;

import com.habit_tracker.habitiq.Entity.ReminderSetting;
import com.habit_tracker.habitiq.Repository.ReminderSettingRepository;
import com.habit_tracker.habitiq.Service.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;

@Component
public class ReminderScheduler {

    @Autowired
    private ReminderSettingRepository settingRepository;

    @Autowired
    private EmailService emailService;

    // Runs every hour
    @Scheduled(cron = "0 0 * * * ?")
    public void sendReminders() {

        List<ReminderSetting> settings = settingRepository.findAll();

        LocalTime now = LocalTime.now();

        for (ReminderSetting setting : settings) {

            if (!setting.isEnabled()) {
                continue;
            }

            boolean sendNow = false;

            switch (setting.getPreferredTime()) {

                case Morning:
                    sendNow = now.getHour() >= 7 && now.getHour() < 10;
                    break;

                case Afternoon:
                    sendNow = now.getHour() >= 12 && now.getHour() < 15;
                    break;

                case Evening:
                    sendNow = now.getHour() >= 18 && now.getHour() < 21;
                    break;
            }

            if (sendNow) {
                try {

                    String email = setting.getUser().getEmail();

                    emailService.sendReminderEmail(email);

                    System.out.println("Reminder email sent to: " + email);

                } catch (MessagingException e) {

                    System.out.println("Failed to send reminder email");
                    e.printStackTrace();
                }
            }
        }
    }
}