package com.habit_tracker.habitiq.Service;

import com.habit_tracker.habitiq.Entity.ReminderSetting;
import com.habit_tracker.habitiq.Entity.User;
import com.habit_tracker.habitiq.Repository.ReminderSettingRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReminderSettingService {

    @Autowired
    private ReminderSettingRepository repository;

    public ReminderSetting getSetting(User user) {
        return repository.findByUser(user)
                .orElseGet(() -> {
                    ReminderSetting setting = new ReminderSetting();
                    setting.setUser(user);
                    return repository.save(setting);
                });
    }

    @Transactional
    public ReminderSetting updateSetting(User user,
                                         String frequency,
                                         String preferredTime,
                                         boolean enabled) {
        ReminderSetting setting = getSetting(user);
        setting.setFrequency(Enum.valueOf(com.habit_tracker.habitiq.Enum.HabitEnum.FrequencyEnum.class, frequency));
        setting.setPreferredTime(Enum.valueOf(com.habit_tracker.habitiq.Enum.HabitEnum.PreferredTimeOfDayEnum.class, preferredTime));
        setting.setEnabled(enabled);
        return repository.save(setting);
    }
}