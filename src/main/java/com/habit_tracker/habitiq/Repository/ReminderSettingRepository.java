package com.habit_tracker.habitiq.Repository;

import com.habit_tracker.habitiq.Entity.ReminderSetting;
import com.habit_tracker.habitiq.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReminderSettingRepository extends JpaRepository<ReminderSetting, Long> {
    Optional<ReminderSetting> findByUser(User user);
}