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

        for(ReminderSetting setting : settings) {
            if(!setting.isEnabled()) continue;

            boolean sendNow = switch(setting.getPreferredTime()) {
                case Morning -> now.getHour() >= 7 && now.getHour() < 10;
                case Afternoon -> now.getHour() >= 12 && now.getHour() < 15;
                case Evening -> now.getHour() >= 18 && now.getHour() < 21;
            };

            if(sendNow) {
                try {
                    String email = setting.getUser().getEmail();
                    emailService.sendReminderEmail(email, "Don't forget to complete your habits today! Consistency builds success 💪");
                } catch(MessagingException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
